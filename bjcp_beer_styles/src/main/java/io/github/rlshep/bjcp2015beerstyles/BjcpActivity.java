package io.github.rlshep.bjcp2015beerstyles;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.apache.commons.lang.StringUtils;

import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;

public abstract class BjcpActivity extends AppCompatActivity {
    protected void setupToolbar(int toolBarId, String title, boolean showIcon, boolean showUp) {
        Toolbar toolbar = (Toolbar) findViewById(toolBarId);

        if (!StringUtils.isEmpty(title)) {
            toolbar.setTitle(StringFormatter.getFormattedTitle(title));
        }

        setSupportActionBar(toolbar);

        if (showUp) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (showIcon) {
            getSupportActionBar().setIcon(R.drawable.action_icon);
        }
    }
}
