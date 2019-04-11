package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

public class ColorTableTab extends Fragment {
    private View view;

    public ColorTableTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_color_table_tab, container, false);

        setupTextValues(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != view) {
            setupTextValues(view);
        }
    }

    private void setupTextValues(View v) {
        PreferencesHelper preferencesHelper = new PreferencesHelper(getActivity());
        String[] colorLabels = getResources().getStringArray(R.array.ticks_labels_srm);

        if (preferencesHelper.isEBC()) {
            ((TextView)v.findViewById(R.id.color_amt_1)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[0]));
            ((TextView)v.findViewById(R.id.color_amt_2)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[1]));
            ((TextView)v.findViewById(R.id.color_amt_3)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[2]));
            ((TextView)v.findViewById(R.id.color_amt_4)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[3]));
            ((TextView)v.findViewById(R.id.color_amt_5)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[4]));
            ((TextView)v.findViewById(R.id.color_amt_6)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[5]));
            ((TextView)v.findViewById(R.id.color_amt_7)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[6]));
            ((TextView)v.findViewById(R.id.color_amt_8)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[7]));
            ((TextView)v.findViewById(R.id.color_amt_9)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[8]));
            ((TextView)v.findViewById(R.id.color_amt_10)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[9]));
            ((TextView)v.findViewById(R.id.color_amt_11)).setText(getString(R.string.ebc)  + " " + (int)MetricConverter.getEBC(colorLabels[10]));
        } else {
            ((TextView)v.findViewById(R.id.color_amt_1)).setText(getString(R.string.srm)  + " " + colorLabels[0]);
            ((TextView)v.findViewById(R.id.color_amt_2)).setText(getString(R.string.srm)  + " " + colorLabels[1]);
            ((TextView)v.findViewById(R.id.color_amt_3)).setText(getString(R.string.srm)  + " " + colorLabels[2]);
            ((TextView)v.findViewById(R.id.color_amt_4)).setText(getString(R.string.srm)  + " " + colorLabels[3]);
            ((TextView)v.findViewById(R.id.color_amt_5)).setText(getString(R.string.srm)  + " " + colorLabels[4]);
            ((TextView)v.findViewById(R.id.color_amt_6)).setText(getString(R.string.srm)  + " " + colorLabels[5]);
            ((TextView)v.findViewById(R.id.color_amt_7)).setText(getString(R.string.srm)  + " " + colorLabels[6]);
            ((TextView)v.findViewById(R.id.color_amt_8)).setText(getString(R.string.srm)  + " " + colorLabels[7]);
            ((TextView)v.findViewById(R.id.color_amt_9)).setText(getString(R.string.srm)  + " " + colorLabels[8]);
            ((TextView)v.findViewById(R.id.color_amt_10)).setText(getString(R.string.srm)  + " " + colorLabels[9]);
            ((TextView)v.findViewById(R.id.color_amt_11)).setText(getString(R.string.srm)  + " " + colorLabels[10]);
        }
    }
}
