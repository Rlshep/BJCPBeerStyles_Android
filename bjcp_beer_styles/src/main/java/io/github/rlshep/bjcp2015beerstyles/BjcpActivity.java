package io.github.rlshep.bjcp2015beerstyles;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

public abstract class BjcpActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected void setupToolbar(int toolBarId, String title, boolean showIcon, boolean showUp) {
        toolbar = findViewById(toolBarId);

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

    protected Toolbar getToolbar() {
        return toolbar;
    }


    protected void setAppLanguage(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
