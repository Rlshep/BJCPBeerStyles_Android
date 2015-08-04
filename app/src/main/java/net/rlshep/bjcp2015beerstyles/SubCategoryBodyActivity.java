package net.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.Section;


public class SubCategoryBodyActivity extends AppCompatActivity {
    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String subCategoryId = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_body);
        dbHandler = new BjcpDataHelper(this, BjcpDataHelper.DATABASE_NAME, null, 1);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            this.setTitle(extras.getString("SUB_CATEGORY") + " - " + extras.getString("SUB_CATEGORY_NAME"));
            subCategoryId = extras.getString("SUB_CATEGORY_ID");
        }

        TextView sectionsText = (TextView)findViewById(R.id.sectionsText);
        sectionsText.setText(Html.fromHtml(getSectionsBody(subCategoryId)));
    }

    private String getSectionsBody(String subCategoryId) {
        String body = "";

        for (Section section : dbHandler.getSubCategorySections(subCategoryId)) {
            body += "<big><b> " + section.get_header() + "</b></big><br>";
            body += section.get_body() + "<br><br>";
        }

        return body;
    }
}
