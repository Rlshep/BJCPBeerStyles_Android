package net.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.Section;
import net.rlshep.bjcp2015beerstyles.domain.VitalStatistics;


public class SubCategoryBodyActivity extends AppCompatActivity {
    private static final String VITAL_HEADER = "Vital Statistics";
    private static final String SRM_PREFIX = "srm_";
    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String subCategoryId = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_body);
        dbHandler = BjcpDataHelper.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            this.setTitle(extras.getString("SUB_CATEGORY") + " - " + extras.getString("SUB_CATEGORY_NAME"));
            subCategoryId = extras.getString("SUB_CATEGORY_ID");
        }

        TextView sectionsText = (TextView)findViewById(R.id.sectionsText);
        sectionsText.setText(Html.fromHtml(getSectionsBody(subCategoryId) + getVitalStatistics(subCategoryId)));
    }

    private String getSectionsBody(String subCategoryId) {
        String body = "";

        for (Section section : dbHandler.getSubCategorySections(subCategoryId)) {
            body += "<big><b> " + section.get_header() + "</b></big><br>";
            body += section.get_body() + "<br><br>";
        }

        return body;
    }

    private String getVitalStatistics(String subCategoryId) {
        String vitals = "";

        VitalStatistics vitalStatistics = dbHandler.getVitalStatistics(subCategoryId);

        if (null == vitalStatistics) {
            hideSrmImages();
        } else {
            vitals += "<big><b> " + VITAL_HEADER + "</b></big>";

            if (null != vitalStatistics.get_ibuStart()) {
                vitals += "<br><b>IBUs:</b> " + vitalStatistics.get_ibuStart() + " - " + vitalStatistics.get_ibuEnd();
            }
            if (null != vitalStatistics.get_ogStart()) {
                vitals += "<br><b>OG:</b> " + vitalStatistics.get_ogStart() + " - " + vitalStatistics.get_ogEnd();
            }
            if (null != vitalStatistics.get_abvStart()) {
                vitals += "<br><b>ABV:</b> " + vitalStatistics.get_abvStart() + " - " + vitalStatistics.get_abvEnd();
            }
            if (null != vitalStatistics.get_fgStart()) {
                vitals += "<br><b>FG:</b> " + vitalStatistics.get_fgStart() + " - " + vitalStatistics.get_fgEnd();
            }
            if (null != vitalStatistics.get_srmStart()) {
                vitals += "<br><b>SRM:</b>";
                showSrmImages(vitalStatistics);
            } else {
                hideSrmImages();
            }
        }

        return vitals;
    }

    private void showSrmImages(VitalStatistics vitalStatistics) {
        ImageView imgSrmBegin = (ImageView)findViewById(R.id.imgSrmBegin);
        ImageView imgSrmEnd = (ImageView)findViewById(R.id.imgSrmEnd);

        imgSrmBegin.setImageResource(getResources().getIdentifier(SRM_PREFIX + vitalStatistics.get_srmStart(), "drawable", getPackageName()));
        imgSrmEnd.setImageResource(getResources().getIdentifier(SRM_PREFIX + vitalStatistics.get_srmEnd(), "drawable", getPackageName()));
    }

    private void hideSrmImages() {
        ImageView imgSrmBegin = (ImageView)findViewById(R.id.imgSrmBegin);
        ImageView imgSrmEnd = (ImageView)findViewById(R.id.imgSrmEnd);

        imgSrmBegin.setVisibility(View.GONE);
        imgSrmEnd.setVisibility(View.GONE);
    }
}
