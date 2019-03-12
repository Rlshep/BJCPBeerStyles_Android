package io.github.rlshep.bjcp2015beerstyles;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

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

    public String getLanguage() {
        String language = "en";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Locale primaryLocale = this.getApplicationContext().getResources().getConfiguration().getLocales().get(0);
            language = primaryLocale.getLanguage();
        }

        return language;
    }


    public String getCountry() {
        String country = "US";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Locale primaryLocale = this.getApplicationContext().getResources().getConfiguration().getLocales().get(0);
            country = primaryLocale.getCountry();
        }

        return country;
    }
}
