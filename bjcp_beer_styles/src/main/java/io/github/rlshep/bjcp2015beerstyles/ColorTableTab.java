package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

        if (preferencesHelper.isEBC()) {
            ((TextView)v.findViewById(R.id.color_amt_1)).setText(getString(R.string.ebc) + " " + MetricConverter.getEBC(2));
            ((TextView)v.findViewById(R.id.color_amt_2)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(4));
            ((TextView)v.findViewById(R.id.color_amt_3)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(6));
            ((TextView)v.findViewById(R.id.color_amt_4)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(9));
            ((TextView)v.findViewById(R.id.color_amt_5)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(14));
            ((TextView)v.findViewById(R.id.color_amt_6)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(17));
            ((TextView)v.findViewById(R.id.color_amt_7)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(18));
            ((TextView)v.findViewById(R.id.color_amt_8)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(22));
            ((TextView)v.findViewById(R.id.color_amt_9)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(30));
            ((TextView)v.findViewById(R.id.color_amt_10)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(35));
            ((TextView)v.findViewById(R.id.color_amt_11)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(37));
            ((TextView)v.findViewById(R.id.color_amt_12)).setText(getString(R.string.ebc) + " "+ MetricConverter.getEBC(40));
        } else {
            ((TextView)v.findViewById(R.id.color_amt_1)).setText(getString(R.string.srm)  + " 2" );
            ((TextView)v.findViewById(R.id.color_amt_2)).setText(getString(R.string.srm)  + " 4");
            ((TextView)v.findViewById(R.id.color_amt_3)).setText(getString(R.string.srm)  + " 6");
            ((TextView)v.findViewById(R.id.color_amt_4)).setText(getString(R.string.srm)  + " 9");
            ((TextView)v.findViewById(R.id.color_amt_5)).setText(getString(R.string.srm)  + " 14");
            ((TextView)v.findViewById(R.id.color_amt_6)).setText(getString(R.string.srm)  + " 17");
            ((TextView)v.findViewById(R.id.color_amt_7)).setText(getString(R.string.srm)  + " 18");
            ((TextView)v.findViewById(R.id.color_amt_8)).setText(getString(R.string.srm)  + " 22");
            ((TextView)v.findViewById(R.id.color_amt_9)).setText(getString(R.string.srm)  + " 30");
            ((TextView)v.findViewById(R.id.color_amt_10)).setText(getString(R.string.srm)  + " 35");
            ((TextView)v.findViewById(R.id.color_amt_11)).setText(getString(R.string.srm)  + " 37");
            ((TextView)v.findViewById(R.id.color_amt_12)).setText(getString(R.string.srm) + " 40");
        }
    }
}
