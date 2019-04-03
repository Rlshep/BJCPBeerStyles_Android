package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class FilterTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_tab, container, false);

        setupButtons(view);

        return view;
    }

    private void setupButtons(View v) {
        Button button = v.findViewById(R.id.filterSearch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance((BjcpActivity)getActivity());
                BjcpController.startSearchResultsActivity((BjcpActivity)getActivity(), "", bjcpDataHelper.getSearchVitalStatisticsQuery(getSearchVitalStatistics()));
            }

            private VitalStatistics getSearchVitalStatistics() {
                VitalStatistics vitalStatistics = new VitalStatistics();

                vitalStatistics.setAbvStart(0.0);
                vitalStatistics.setAbvEnd(15.0);
                vitalStatistics.setOgStart(1.050);
                vitalStatistics.setOgEnd(1.200);
                vitalStatistics.setFgStart(0.900);
                vitalStatistics.setFgEnd(1.030);
                vitalStatistics.setAbvStart(5.0);
                vitalStatistics.setAbvEnd(10);
                vitalStatistics.setIbuStart(10);
                vitalStatistics.setIbuEnd(70);
                vitalStatistics.setSrmStart(0);
                vitalStatistics.setSrmEnd(35);

                return vitalStatistics;
            }
        });
    }


}
