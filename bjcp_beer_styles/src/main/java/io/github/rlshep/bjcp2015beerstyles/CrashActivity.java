package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.widget.TextView;

import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class CrashActivity extends BjcpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_crash);
        Bundle extras = getIntent().getExtras();
        setupToolbar(R.id.errorToolbar, getString(R.string.title_activity_crash) + getString(R.string.about_version), false, false);

        TextView errorText = (TextView) findViewById(R.id.errorText);

        errorText.setText(extras.getString(ExceptionHandler.EXTRA_ERROR));
    }
}
