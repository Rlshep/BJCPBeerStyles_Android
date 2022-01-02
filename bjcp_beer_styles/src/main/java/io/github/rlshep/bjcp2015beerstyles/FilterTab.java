package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appyvet.materialrangebar.RangeBar;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistic;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;
import io.github.rlshep.bjcp2015beerstyles.helpers.SearchHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_ABV;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_IBU;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_SRM;


public class FilterTab extends Fragment {

    private List<VitalStatistic> vitalStatistics = new ArrayList<>();
    private ArrayAdapter<String> searchSuggestionAdapter;
    private View view;
    private static final double MAX_ABV = 100.0;
    private static final int MAX_IBU = 200;
    private static final int MAX_SRM = 41;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.filter_tab, container, false);

        initVitalStatistics();
        setupSearchText(view);
        setupRangeBars(view);
        setupButtons(view);
        setupUI(view);
        setupColors(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != view) {
            setupColors(view);
            setupSearchText(view);
        }
    }

    private void initVitalStatistics() {
        VitalStatistic abv = new VitalStatistic();
        abv.setType(XML_ABV);
        abv.setLow(0.0);
        abv.setHigh(MAX_ABV);
        vitalStatistics.add(abv);

        VitalStatistic ibu = new VitalStatistic();
        ibu.setType(XML_IBU);
        ibu.setLow(0);
        ibu.setHigh(MAX_IBU);
        vitalStatistics.add(ibu);

        VitalStatistic srm = new VitalStatistic();
        srm.setType(XML_SRM);
        srm.setLow(0.0);
        srm.setHigh(MAX_SRM);
        vitalStatistics.add(srm);
    }

    private void setupSearchText(View view) {
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = view.findViewById(R.id.editSearch);
        String[] searchSuggestions = new SearchHelper().getSearchSuggestions((BjcpActivity)getActivity());  // Get the string array
        searchSuggestionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, searchSuggestions);
        textView.setAdapter(searchSuggestionAdapter);
    }

    private void setupRangeBars(View view) {
        RangeBar rangebarIbu = view.findViewById(R.id.rangebar_ibu);
        RangeBar rangebarAbv = view.findViewById(R.id.rangebar_abv);
        RangeBar rangebarColor = view.findViewById(R.id.rangebar_color);

        rangebarIbu.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                setVitalLow(XML_IBU, Double.parseDouble(leftPinValue));
                setVitalHigh(XML_IBU, Double.parseDouble(rightPinValue));
            }

            @Override
            public void onTouchEnded(RangeBar rb) {
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {
            }
        });

        rangebarAbv.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                setVitalLow(XML_ABV, Double.parseDouble(leftPinValue));
                setVitalHigh(XML_ABV, Double.parseDouble(rightPinValue));
            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {
            }

        });

        rangebarColor.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                setVitalLow(XML_SRM, Double.parseDouble(leftPinValue));
                setVitalHigh(XML_SRM, Double.parseDouble(rightPinValue));
            }

            @Override
            public void onTouchEnded(RangeBar rb) {
                rb.setLeftSelectorColor(rb.getTickColor(rb.getLeftIndex()));
                rb.setRightSelectorColor(rb.getTickColor(rb.getRightIndex()));
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {
            }
        });
    }

    private void setupButtons(View v) {
        Button search = v.findViewById(R.id.filterSearch);
        Button reset = v.findViewById(R.id.filterReset);
        RangeBar rangebarIbu = v.findViewById(R.id.rangebar_ibu);
        RangeBar rangebarAbv = v.findViewById(R.id.rangebar_abv);
        RangeBar rangebarColor = v.findViewById(R.id.rangebar_color);
        AutoCompleteTextView editSearch = v.findViewById(R.id.editSearch);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateMaxVitals();

                BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance((BjcpActivity) getActivity());
                BjcpController.startSearchResultsActivity(getActivity(), editSearch.getText().toString(), bjcpDataHelper.getSearchVitalStatisticsQuery(vitalStatistics));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initVitalStatistics();

                rangebarIbu.setRangePinsByValue(0, 100);
                rangebarAbv.setRangePinsByValue(0, 10);
                rangebarColor.setRangePinsByValue(1, 41);
                editSearch.setText("");
            }
        });
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof AutoCompleteTextView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void setupColors(View v) {
        TextView colorText = v.findViewById(R.id.color_text);
        RangeBar rangebarColor = v.findViewById(R.id.rangebar_color);
        PreferencesHelper preferencesHelper = new PreferencesHelper(getActivity());
        String[] colorLabels = getResources().getStringArray(R.array.ticks_labels_srm);

        if (preferencesHelper.isEBC()) {
            for (int i=0; i<colorLabels.length; i++) {
                colorLabels[i] = (Integer.valueOf((int)MetricConverter.getEBC(Double.parseDouble(colorLabels[i])))).toString();
            }

            colorText.setText(R.string.ebc);
        } else {
            colorText.setText(R.string.srm);
        }

        rangebarColor.setTickBottomLabels(colorLabels);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (null != activity.getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Adjust search vitals account for higher values than range bar allows.
     */
    private void updateMaxVitals() {
        RangeBar rangeBarIbu = view.findViewById(R.id.rangebar_ibu);
        RangeBar rangeBarAbv = view.findViewById(R.id.rangebar_abv);
        RangeBar rangeBarColor = view.findViewById(R.id.rangebar_color);

        for (VitalStatistic vitalStatistic : vitalStatistics) {
            if (XML_IBU.equals(vitalStatistic.getType())) {
                if (vitalStatistic.getHigh() == Math.round(rangeBarIbu.getTickEnd())) {
                    vitalStatistic.setHigh(MAX_IBU);
                }
            } else if (XML_ABV.equals(vitalStatistic.getType())) {
                if (vitalStatistic.getHigh() == Math.round(rangeBarAbv.getTickEnd())) {
                    vitalStatistic.setHigh(MAX_ABV);
                }
            } else if (XML_SRM.equals(vitalStatistic.getType())) {
                if (vitalStatistic.getLow() == Math.round(rangeBarColor.getTickStart())) {
                    vitalStatistic.setHigh(MAX_IBU);
                }
            }
        }
    }

    private void setVitalLow(String type, double value) {
        for (VitalStatistic vitalStatistic : vitalStatistics) {
            if (XML_IBU.equals(type)) {
                vitalStatistic.setLow(value);
            } else if (XML_ABV.equals(type)) {
                vitalStatistic.setLow(value);
            } else if (XML_SRM.equals(type)) {
                vitalStatistic.setLow(value);
            }
        }
    }

    private void setVitalHigh(String type, double value) {
        for (VitalStatistic vitalStatistic : vitalStatistics) {
            if (XML_IBU.equals(type)) {
                vitalStatistic.setHigh(value);
            } else if (XML_ABV.equals(type)) {
                vitalStatistic.setHigh(value);
            } else if (XML_SRM.equals(type)) {
                vitalStatistic.setHigh(value);
            }
        }
    }
}
