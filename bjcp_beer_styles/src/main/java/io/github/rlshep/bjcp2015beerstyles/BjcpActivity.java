package io.github.rlshep.bjcp2015beerstyles;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.apache.commons.lang.StringUtils;

public abstract class BjcpActivity extends AppCompatActivity {
    protected void setupToolbar(int toolBarId, String title, boolean showIcon, boolean showUp) {
        Toolbar toolbar = findViewById(toolBarId);

        if (!StringUtils.isEmpty(title)) {
            toolbar.setTitle(title);
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
