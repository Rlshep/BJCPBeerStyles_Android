package io.github.rlshep.bjcp2015beerstyles;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;

public class ColorTableTab extends Fragment {

    public ColorTableTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color_table_tab, container, false);

        setupTextValues(v);

        return v;
    }

    private void setupTextValues(View v) {
        BjcpActivity ba = (BjcpActivity)v.getContext();
        if (MetricConverter.isMetric(ba.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE))) {
            ((TextView)v.findViewById(R.id.color_amt_1)).setText("EBC " + MetricConverter.getEBC(2));
            ((TextView)v.findViewById(R.id.color_amt_2)).setText("EBC " + MetricConverter.getEBC(4));
            ((TextView)v.findViewById(R.id.color_amt_3)).setText("EBC " + MetricConverter.getEBC(6));
            ((TextView)v.findViewById(R.id.color_amt_4)).setText("EBC " + MetricConverter.getEBC(9));
            ((TextView)v.findViewById(R.id.color_amt_5)).setText("EBC " + MetricConverter.getEBC(14));
            ((TextView)v.findViewById(R.id.color_amt_6)).setText("EBC " + MetricConverter.getEBC(17));
            ((TextView)v.findViewById(R.id.color_amt_7)).setText("EBC " + MetricConverter.getEBC(18));
            ((TextView)v.findViewById(R.id.color_amt_8)).setText("EBC " + MetricConverter.getEBC(22));
            ((TextView)v.findViewById(R.id.color_amt_9)).setText("EBC " + MetricConverter.getEBC(30));
            ((TextView)v.findViewById(R.id.color_amt_10)).setText("EBC " + MetricConverter.getEBC(35));
            ((TextView)v.findViewById(R.id.color_amt_11)).setText("EBC " + MetricConverter.getEBC(37));
            ((TextView)v.findViewById(R.id.color_amt_12)).setText("EBC " + MetricConverter.getEBC(40));
        }
    }
}
