package io.github.rlshep.bjcp2015beerstyles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appyvet.materialrangebar.RangeBar;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class FilterTab extends Fragment {

    private VitalStatistics vitalStatistics = new VitalStatistics();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_tab, container, false);

        initVitalStatistics();
        setupButtons(view);
        setupRangeBars(view);

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

    private void setupButtons(View v) {
        Button button = v.findViewById(R.id.filterSearch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance((BjcpActivity)getActivity());
                BjcpController.startSearchResultsActivity((BjcpActivity)getActivity(), "", bjcpDataHelper.getSearchVitalStatisticsQuery(vitalStatistics));
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
            public void onReleaseListener(RangeBar rangeBar, int leftPinIndex,
                                          int rightPinIndex, String leftPinValue, String rightPinValue) {

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
            public void onReleaseListener(RangeBar rangeBar, int leftPinIndex,
                                          int rightPinIndex, String leftPinValue, String rightPinValue) {

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
            public void onReleaseListener(RangeBar rangeBar, int leftPinIndex,
                                          int rightPinIndex, String leftPinValue, String rightPinValue) {
                rangeBar.setSelectorColorLeft(rangeBar.getTickColors(leftPinIndex));
                rangeBar.setSelectorColorRight(rangeBar.getTickColors(rightPinIndex));
            }
        });
    }
}
