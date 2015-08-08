package net.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.Section;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;
import net.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import net.rlshep.bjcp2015beerstyles.listeners.GestureListener;

import java.util.List;


public class SubCategoryBodyActivity extends AppCompatActivity {
    private static final String VITAL_HEADER = "Vital Statistics";
    private static final String SRM_PREFIX = "srm_";
    private BjcpDataHelper dbHandler;
    private GestureDetector gestureDetector;
    private String categoryId = "";
    private String subCategoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_body);
        dbHandler = BjcpDataHelper.getInstance(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String title = extras.getString("SUB_CATEGORY") + " - " + extras.getString("SUB_CATEGORY_NAME");
            Toolbar toolbar = (Toolbar) findViewById(R.id.scbToolbar);
            toolbar.setTitle(title);

            subCategoryId = extras.getString("SUB_CATEGORY_ID");
            categoryId = extras.getString("CATEGORY_ID");
        }

        TextView sectionsTextView = (TextView) findViewById(R.id.sectionsText);
        sectionsTextView.setText(Html.fromHtml(getSectionsBody(subCategoryId) + getVitalStatistics(subCategoryId)));

        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean eventReturn;
        boolean eventConsumed = gestureDetector.onTouchEvent(event);

        if (eventConsumed) {
            if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                changeSubCategory(-1);
            } else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                changeSubCategory(1);
            }

            eventReturn = true;
        }
        else {
            eventReturn = super.dispatchTouchEvent(event);
        }

        return eventReturn;
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
        }
        else {
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
            }
            else {
                hideSrmImages();
            }
        }

        return vitals;
    }

    private void showSrmImages(VitalStatistics vitalStatistics) {
        ImageView imgSrmBegin = (ImageView) findViewById(R.id.imgSrmBegin);
        ImageView imgSrmEnd = (ImageView) findViewById(R.id.imgSrmEnd);

        imgSrmBegin.setImageResource(getResources().getIdentifier(SRM_PREFIX + vitalStatistics.get_srmStart(), "drawable", getPackageName()));
        imgSrmEnd.setImageResource(getResources().getIdentifier(SRM_PREFIX + vitalStatistics.get_srmEnd(), "drawable", getPackageName()));
    }

    private void hideSrmImages() {
        ImageView imgSrmBegin = (ImageView) findViewById(R.id.imgSrmBegin);
        ImageView imgSrmEnd = (ImageView) findViewById(R.id.imgSrmEnd);

        imgSrmBegin.setVisibility(View.GONE);
        imgSrmEnd.setVisibility(View.GONE);
    }

    private void changeSubCategory(int i) {
        List<SubCategory> subCategories = dbHandler.getSubCategories(categoryId);
        SubCategory subCategory = dbHandler.getSubCategory(subCategoryId);
        int newOrder =  subCategory.get_orderNumber() + i;

        if (0 <= newOrder && subCategories.size() > newOrder) {
            for (SubCategory sc : subCategories) {
                if (newOrder == sc.get_orderNumber()) {
                    loadSubCategoryBody(sc);
                }
            }
        }
    }

    private void loadSubCategoryBody(SubCategory subCategory) {
        Intent i = new Intent(this, SubCategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (new Long(subCategory.get_categoryId())).toString());
        i.putExtra("SUB_CATEGORY_ID", (new Long(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(i);
    }
}
