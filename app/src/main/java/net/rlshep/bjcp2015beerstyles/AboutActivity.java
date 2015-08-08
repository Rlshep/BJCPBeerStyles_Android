package net.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.scbToolbar);
        toolbar.setTitle(getString(R.string.title_activity_about));

    }

    private void setBodyText() {
        String body = "<big><b>" + getString(R.string.about_version) + "<big><b><br><br>";

        TextView sectionsTextView = (TextView) findViewById(R.id.sectionsText);
        sectionsTextView.setText(Html.fromHtml(body));
    }
}
