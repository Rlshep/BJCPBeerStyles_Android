package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;
import io.github.rlshep.bjcp2015beerstyles.listeners.GestureListener;


public class SubCategoryBodyActivity extends BjcpActivity {
    private static final String VITAL_HEADER = "Vital Statistics";
    private static final String SRM_PREFIX = "srm_";
    private GestureDetector gestureDetector;
    private String categoryId = "";
    private String subCategoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_sub_category_body);
        Bundle extras = getIntent().getExtras();
        String searchedText = "";

        if (extras != null) {
            String title = extras.getString("SUB_CATEGORY") + " - " + extras.getString("SUB_CATEGORY_NAME");
            setupToolbar(R.id.scbToolbar, title, false, true);

            subCategoryId = extras.getString("SUB_CATEGORY_ID");
            categoryId = extras.getString("CATEGORY_ID");
            searchedText = extras.getString("SEARCHED_TEXT");
        }

        setText(searchedText);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        boolean eventReturn;
        boolean eventConsumed = gestureDetector.onTouchEvent(event);

        if (eventConsumed) {
            if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                changeSubCategory(-1);
            }
            else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                changeSubCategory(1);
            }

            eventReturn = true;
        }
        else {
            eventReturn = super.dispatchTouchEvent(event);
        }

        return eventReturn;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BjcpController.loadSubCategoryList(this, BjcpDataHelper.getInstance(this).getCategory(categoryId));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getSectionsBody(String subCategoryId) {
        String body = "";

        for (Section section : BjcpDataHelper.getInstance(this).getSubCategorySections(subCategoryId)) {
            body += "<big><b> " + section.get_header() + "</b></big><br>";
            body += section.get_body() + "<br><br>";
        }

        return body;
    }

    private String getVitalStatistics(String subCategoryId) {
        String vitals = "";

        VitalStatistics vitalStatistics = BjcpDataHelper.getInstance(this).getVitalStatistics(subCategoryId);

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

        imgSrmBegin.setImageResource(getResources().getIdentifier(SRM_PREFIX + getStartSrm(vitalStatistics), "drawable", getPackageName()));
        imgSrmEnd.setImageResource(getResources().getIdentifier(SRM_PREFIX + getEndSrm(vitalStatistics), "drawable", getPackageName()));

        // For disabled accessibility.
        imgSrmBegin.setContentDescription(getString(R.string.beginSrm) + vitalStatistics.get_srmStart());
        imgSrmEnd.setContentDescription(getString(R.string.endSrm) + vitalStatistics.get_srmEnd());
    }

    private void hideSrmImages() {
        ImageView imgSrmBegin = (ImageView) findViewById(R.id.imgSrmBegin);
        ImageView imgSrmEnd = (ImageView) findViewById(R.id.imgSrmEnd);

        imgSrmBegin.setVisibility(View.GONE);
        imgSrmEnd.setVisibility(View.GONE);
    }

    private void changeSubCategory(int i) {
        List<SubCategory> subCategories = BjcpDataHelper.getInstance(this).getSubCategories(categoryId);
        SubCategory subCategory = BjcpDataHelper.getInstance(this).getSubCategory(subCategoryId);
        int newOrder = subCategory.get_orderNumber() + i;

        if (0 <= newOrder && subCategories.size() > newOrder) {
            for (SubCategory sc : subCategories) {
                if (newOrder == sc.get_orderNumber()) {
                    BjcpController.loadSubCategoryBody(this, sc);
                }
            }
        }
    }

    private String getStartSrm(VitalStatistics vitalStatistics) {
        double floor = Math.floor(Double.parseDouble(vitalStatistics.get_srmStart()));
        return ((Integer) Double.valueOf(floor).intValue()).toString();
    }

    private String getEndSrm(VitalStatistics vitalStatistics) {
        double ceil = Math.ceil(Double.parseDouble(vitalStatistics.get_srmEnd()));
        return ((Integer) Double.valueOf(ceil).intValue()).toString();
    }

    private void setText(String searchedText) {
        String text = getSectionsBody(subCategoryId) + getVitalStatistics(subCategoryId);
        TextView sectionsTextView = (TextView) findViewById(R.id.sectionsText);
        sectionsTextView.setText(Html.fromHtml(StringFormatter.getHighlightedText(text, searchedText)));
    }
}
