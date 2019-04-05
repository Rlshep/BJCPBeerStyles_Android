package io.github.rlshep.bjcp2015beerstyles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.appyvet.materialrangebar.RangeBar;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.helpers.SearchHelper;
import io.github.rlshep.bjcp2015beerstyles.view.ArrayAdapterSearchView;

//implements SearchView.OnQueryTextListener
public class FilterTab extends Fragment {

    private VitalStatistics vitalStatistics = new VitalStatistics();
    private ArrayAdapter<String> searchSuggestionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_tab, container, false);

        initVitalStatistics();
        setupSearch(view);
        setupRangeBars(view);
        setupButtons(view);

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

    private void setupSearch(View view) {
        ArrayAdapterSearchView filterSearch = view.findViewById(R.id.filterSearch);
        String[] searchSuggestions = new SearchHelper().getSearchSuggestions((BjcpActivity)getActivity());

        // Set adapter to get search suggestions.
        searchSuggestionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.find_view, searchSuggestions);
        filterSearch.setAdapter(searchSuggestionAdapter);

        filterSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterSearch.setText(searchSuggestionAdapter.getItem(position).toString());
//                onQueryTextSubmit(searchSuggestionAdapter.getItem(position).toString());
            }
        });
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
                SharedPreferences sharedPreferences = (view.getContext()).getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                if (MetricConverter.isMetric(sharedPreferences)) {
                    vitalStatistics.setSrmStart(MetricConverter.getSRM(Double.parseDouble(leftPinValue)));
                    vitalStatistics.setSrmEnd(MetricConverter.getSRM(Double.parseDouble(rightPinValue)));
                } else {
                    vitalStatistics.setSrmStart(Double.parseDouble(leftPinValue));
                    vitalStatistics.setSrmEnd(Double.parseDouble(rightPinValue));
                }
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
        EditText editSearch = v.findViewById(R.id.editSearch);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance((BjcpActivity) getActivity());

                //TODO: ADD SEARCH
                BjcpController.startSearchResultsActivity(getActivity(), "", bjcpDataHelper.getSearchVitalStatisticsQuery(vitalStatistics));
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
}
