package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();
        setBodyText();
    }

    private void setBodyText() {
        String body = "<big><b>" + getString(R.string.about_version) + "</big></b><br><br>";
        body += getString(R.string.about_bjcp_begin) + " <a href='http://bjcp.org/'>" + getString(R.string.about_bjcp_mid) + "</a> " + getString(R.string.about_bjcp_end) + "<br><br>";
        body += getString(R.string.about_author) + "<br><br>";
        body += getString(R.string.about_thanks_begin) + " <a href='https://github.com/seth-k/BJCP-styles-XML'>" + getString(R.string.about_thanks_mid) + "</a> " + getString(R.string.about_thanks_end) + "<br><br>";
        body += getString(R.string.about_report_begin) + " <a href='https://github.com/Rlshep/BJCP2015BeerStyles/issues'>" + getString(R.string.about_report_end) + "</a><br><br>";

        TextView sectionsTextView = (TextView) findViewById(R.id.aboutText);
        sectionsTextView.setText(Html.fromHtml(body));
        sectionsTextView.setMovementMethod(LinkMovementMethod.getInstance());   //Make links actually work.
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.scbToolbar);
        toolbar.setTitle(getString(R.string.title_activity_about));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.action_icon);
    }
}
