package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;

public class CrashActivity extends BjcpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//TODO: UNCOMMENT
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//        setContentView(R.layout.activity_crash);
//        Bundle extras = getIntent().getExtras();
//        setupToolbar(R.id.errorToolbar, getString(R.string.title_activity_crash), false, false);
//
//        TextView errorText = (TextView) findViewById(R.id.errorText);
//
//        errorText.setText(extras.getString(ExceptionHandler.EXTRA_ERROR));
    }
}
