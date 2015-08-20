package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class AboutActivity extends BjcpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_about);

        setupToolbar(R.id.scbToolbar, getString(R.string.title_activity_about), true, true);
        setBodyText();
    }

    private void setBodyText() {
        String body = "<big><b>" + getString(R.string.about_version) + "</big></b><br><br>";
        body += getString(R.string.about_bjcp_begin) + " <a href='http://bjcp.org/'>" + getString(R.string.about_bjcp_mid) + "</a> " + getString(R.string.about_bjcp_end) + "<br><br>";
        body += getString(R.string.about_author) + " " + getString(R.string.twitter_begin) + " <a href='https://twitter.com/rlshep'>@rlshep</a><br><br>";
        body += getString(R.string.about_thanks_begin) + " <a href='https://github.com/seth-k/BJCP-styles-XML'>" + getString(R.string.about_thanks_mid) + "</a> " + getString(R.string.about_thanks_end) + "<br><br>";
        body += getString(R.string.about_report_begin) + " <a href='https://github.com/Rlshep/BJCP2015BeerStyles/issues'>" + getString(R.string.about_report_end) + "</a><br><br>";

        TextView sectionsTextView = (TextView) findViewById(R.id.aboutText);
        sectionsTextView.setText(Html.fromHtml(body));
        sectionsTextView.setMovementMethod(LinkMovementMethod.getInstance());   //Make links actually work.
    }
}
