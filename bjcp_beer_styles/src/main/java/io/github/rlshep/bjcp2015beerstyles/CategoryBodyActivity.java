package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;
import io.github.rlshep.bjcp2015beerstyles.listeners.GestureListener;


public class CategoryBodyActivity extends BjcpActivity {

    private static final String SRM_PREFIX = "srm_";
    private GestureDetector gestureDetector;
    private String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_category_body);
        Bundle extras = getIntent().getExtras();
        String searchedText = "";

        if (extras != null) {
            String title = extras.getString("CATEGORY") + " - " + extras.getString("CATEGORY_NAME");
            setupToolbar(R.id.scbToolbar, title, false, true);
            categoryId = extras.getString("CATEGORY_ID");
            searchedText = StringFormatter.removeDoubleSingleQuotes(extras.getString("SEARCHED_TEXT"));
        }

        setBody(searchedText);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        boolean eventReturn;
        boolean eventConsumed = gestureDetector.onTouchEvent(event);

        if (eventConsumed) {
            if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                changeCategory(-1);
            } else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                changeCategory(1);
            }

            eventReturn = true;
        } else {
            eventReturn = super.dispatchTouchEvent(event);
        }

        return eventReturn;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Category category = BjcpDataHelper.getInstance(this).getCategory(categoryId);
                BjcpController.loadCategoryList(this, BjcpDataHelper.getInstance(this).getCategory(((Long) category.getParentId()).toString()));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setBody(String searchedText) {
        List<VitalStatistics> vitalStatisticses = BjcpDataHelper.getInstance(this).getVitalStatistics(categoryId);
        setMainText(searchedText, vitalStatisticses);
        setSrms(vitalStatisticses);
    }

    private void setSrms(List<VitalStatistics> vitalStatisticses) {
        int i = 1;

        for (VitalStatistics vitalStatistics : vitalStatisticses) {
            if (null != vitalStatistics.getSrmStart()) {
                setSrm(vitalStatistics, i);
                i++;
            }
        }
    }

    private void setMainText(String searchedText, List<VitalStatistics> vitalStatisticses) {
        String text = getSectionsBody(categoryId) + getVitalStatistics(vitalStatisticses);

        TextView sectionsTextView = findViewById(R.id.sectionsText);
        sectionsTextView.setText(Html.fromHtml(StringFormatter.getHighlightedText(text, searchedText)));
        sectionsTextView.setMovementMethod(LinkMovementMethod.getInstance());   //Make links actually work.
    }

    private String getSectionsBody(String categoryId) {
        String body = "";
        for (Section section : BjcpDataHelper.getInstance(this).getCategorySections(categoryId)) {
            body += section.getBody();
        }

        return body;
    }

    private void setSrm(VitalStatistics vitalStatistics, int i) {
        TextView srmText = getSrmTextView("srmText", i);
        TextView srmTextStart = getSrmTextView("srmTextStart", i);
        TextView srmTextEnd = getSrmTextView("srmTextEnd", i);

        srmText.setText(Html.fromHtml(getColorVerbiage(vitalStatistics)));
        srmTextStart.setBackgroundColor(getResources().getColor(getResources().getIdentifier(SRM_PREFIX + getStartSrm(vitalStatistics), "color", getPackageName())));
        srmTextEnd.setBackgroundColor(getResources().getColor(getResources().getIdentifier(SRM_PREFIX + getEndSrm(vitalStatistics), "color", getPackageName())));

        srmTextStart.setVisibility(View.VISIBLE);
        srmTextEnd.setVisibility(View.VISIBLE);
        srmTextEnd.setVisibility(View.VISIBLE);
    }

    private String getVitalStatistics(List<VitalStatistics> vitalStatisticses) {
        String vitals = "";

        for (VitalStatistics vitalStatistics : vitalStatisticses) {
            if (null != vitalStatistics.getIbuStart()) {
                vitals += "<br><b>" + vitalStatistics.getHeader() + " IBUs:</b> " + vitalStatistics.getIbuStart() + " - " + vitalStatistics.getIbuEnd();
            }
            if (null != vitalStatistics.getOgStart()) {
                vitals += getOgVerbiage(vitalStatistics);
            }
            if (null != vitalStatistics.getFgStart()) {
                vitals += getFgVerbiage(vitalStatistics);
            }
            if (null != vitalStatistics.getAbvStart()) {
                vitals += "<br><b>" + vitalStatistics.getHeader() + " ABV:</b> " + vitalStatistics.getAbvStart() + " - " + vitalStatistics.getAbvEnd();
            }
        }

        return vitals;
    }
    
    private TextView getSrmTextView(String srm, int i) {
        int id = getResources().getIdentifier(srm + i, "id", getPackageName());
        return (TextView) findViewById(id);
    }

    private void changeCategory(int i) {
        Category category = BjcpDataHelper.getInstance(this).getCategory(categoryId);
        List<Category> subCategories = BjcpDataHelper.getInstance(this).getCategoriesByParent(category.getParentId());
        int newOrder = category.getOrderNumber() + i;

        for (Category sc : subCategories) {
            if (newOrder == sc.getOrderNumber()) {
                BjcpController.loadCategoryBody(this, sc);
            }
        }
    }

    private String getStartSrm(VitalStatistics vitalStatistics) {
        double floor = Math.floor(Double.parseDouble(vitalStatistics.getSrmStart()));
        return ((Integer) Double.valueOf(floor).intValue()).toString();
    }

    private String getEndSrm(VitalStatistics vitalStatistics) {
        double ceil = Math.ceil(Double.parseDouble(vitalStatistics.getSrmEnd()));
        return ((Integer) Double.valueOf(ceil).intValue()).toString();
    }

    private String getColorVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder colorVerbiage = new StringBuilder();
        colorVerbiage.append("<b>");
        colorVerbiage.append(vitalStatistics.getHeader());
        colorVerbiage.append(" ");

        if (super.isMetric()) {
            colorVerbiage.append(getString(R.string.ebc));
            colorVerbiage.append(": </b>");
            colorVerbiage.append(MetricConverter.getEBC(vitalStatistics.getSrmStart()));
            colorVerbiage.append(" - ");
            colorVerbiage.append(MetricConverter.getEBC(vitalStatistics.getSrmEnd()));
        } else {
            colorVerbiage.append(getString(R.string.srm));
            colorVerbiage.append(": </b>");
            colorVerbiage.append(vitalStatistics.getSrmStart());
            colorVerbiage.append(" - ");
            colorVerbiage.append(vitalStatistics.getSrmEnd());
        }

        return colorVerbiage.toString();
    }

    private String getOgVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder ogVerbiage = new StringBuilder();
        ogVerbiage.append("<br><b>");
        ogVerbiage.append(vitalStatistics.getHeader());
        ogVerbiage.append(" " );
        ogVerbiage.append(getString(R.string.og));
        ogVerbiage.append(":</b> ");
                
        if (super.isMetric()) {
            ogVerbiage.append(MetricConverter.getPlato(vitalStatistics.getOgStart()));
            ogVerbiage.append(getString(R.string.plato));
            ogVerbiage.append(" - ");
            ogVerbiage.append(MetricConverter.getPlato(vitalStatistics.getOgEnd()));
            ogVerbiage.append(getString(R.string.plato));
        } else {
             ogVerbiage.append(vitalStatistics.getOgStart());
             ogVerbiage.append(" - ");
             ogVerbiage.append(vitalStatistics.getOgEnd());
        }
        
        return ogVerbiage.toString();
    }


    private String getFgVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder fgVerbiage = new StringBuilder();
        fgVerbiage.append("<br><b>");
        fgVerbiage.append(vitalStatistics.getHeader());
        fgVerbiage.append(" " );
        fgVerbiage.append(getString(R.string.fg));
        fgVerbiage.append(":</b> ");

        if (super.isMetric()) {
            fgVerbiage.append(MetricConverter.getPlato(vitalStatistics.getFgStart()));
            fgVerbiage.append(getString(R.string.plato));
            fgVerbiage.append(" - ");
            fgVerbiage.append(MetricConverter.getPlato(vitalStatistics.getFgEnd()));
            fgVerbiage.append(getString(R.string.plato));
        } else {
            fgVerbiage.append(vitalStatistics.getFgStart());
            fgVerbiage.append(" - ");
            fgVerbiage.append(vitalStatistics.getFgEnd());
        }

        return fgVerbiage.toString();
    }
}