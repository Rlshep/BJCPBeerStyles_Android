package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.helpers.SearchHelper;


public class FilterTab extends Fragment {

    private VitalStatistics vitalStatistics = new VitalStatistics();
    private ArrayAdapter<String> searchSuggestionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_tab, container, false);

        initVitalStatistics();
        setupSearchText(view);
        setupRangeBars(view);
        setupButtons(view);
        setupUI(view);
        setupColors(view);

        return view;
    }

    private void initVitalStatistics() {
        vitalStatistics.setAbvStart(0.0);
        vitalStatistics.setAbvEnd(10);
        vitalStatistics.setIbuStart(0);
        vitalStatistics.setIbuEnd(100);
        vitalStatistics.setSrmStart(0);
        vitalStatistics.setSrmEnd(40);
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
                vitalStatistics.setIbuStart(Integer.parseInt(leftPinValue));
                vitalStatistics.setIbuEnd(Integer.parseInt(rightPinValue));
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
                vitalStatistics.setAbvStart(Double.parseDouble(leftPinValue));
                vitalStatistics.setAbvEnd(Double.parseDouble(rightPinValue));
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

                vitalStatistics.setSrmStart(Double.parseDouble(leftPinValue));
                vitalStatistics.setSrmEnd(Double.parseDouble(rightPinValue));
            }

            @Override
            public void onTouchEnded(RangeBar rb) {
                rb.setSelectorColorLeft(rb.getTickColors(rb.getLeftIndex()));
                rb.setSelectorColorRight(rb.getTickColors(rb.getRightIndex()));
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
        SharedPreferences sharedPreferences = (v.getContext()).getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        TextView colorText = v.findViewById(R.id.color_text);
        RangeBar rangebarColor = v.findViewById(R.id.rangebar_color);

        if (MetricConverter.isMetric(sharedPreferences)) {
            String[] colorLabels = getResources().getStringArray(R.array.ticks_labels_srm);

            for (int i=0; i<colorLabels.length; i++) {
                System.out.println(i + " orig=" + colorLabels[i] + ", converted=" + MetricConverter.getEBC(Double.parseDouble(colorLabels[i])));
                colorLabels[i] = (new Integer((int)MetricConverter.getEBC(Double.parseDouble(colorLabels[i])))).toString();
            }

            colorText.setText(R.string.ebc);
            rangebarColor.setTickBottomLabels(colorLabels);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (null != activity && null != activity.getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
