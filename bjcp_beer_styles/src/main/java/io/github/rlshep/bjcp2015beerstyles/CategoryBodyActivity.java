package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;
import io.github.rlshep.bjcp2015beerstyles.helpers.activity.CategoryBodyHelper;
import io.github.rlshep.bjcp2015beerstyles.helpers.activity.ColorHelper;
import io.github.rlshep.bjcp2015beerstyles.listeners.GestureListener;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.ZERO;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_SRM;


public class CategoryBodyActivity extends BjcpActivity {

    private static final String SRM_PREFIX = "srm_";
    private GestureDetector gestureDetector;
    private String categoryId = "";
    private PreferencesHelper preferencesHelper;
    private CategoryBodyHelper categoryBodyHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_category_body);
        Bundle extras = getIntent().getExtras();
        String searchedText = "";
        String title = "";

        if (extras != null) {
            if (StringUtils.isEmpty(extras.getString("CATEGORY"))) {
                title = extras.getString("CATEGORY") + " - " + extras.getString("CATEGORY_NAME");
            } else {
                title = extras.getString("CATEGORY_NAME");
            }

            setupToolbar(R.id.scbToolbar, title, false, true);
            categoryId = extras.getString("CATEGORY_ID");

            if (StringUtils.isEmpty(extras.getString("SEARCHED_TEXT"))) {
                searchedText = "";
            } else {
                searchedText = StringFormatter.removeDoubleSingleQuotes(extras.getString("SEARCHED_TEXT"));
            }
        }

        preferencesHelper = new PreferencesHelper(this);
        categoryBodyHelper = new CategoryBodyHelper(this, categoryId);
        setBody(searchedText);
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        boolean eventReturn = false;

        try {
            boolean eventConsumed = gestureDetector.onTouchEvent(event);

            if (eventConsumed) {
                if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                    changeCategory(-1);
                } else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                    changeCategory(1);
                }
            } else {
                eventReturn = super.dispatchTouchEvent(event);
            }
        } catch (Exception e) {
            // I don't know why this is happening. setSpan out of index.
            Log.e("CategoryBodyActivity", e.getMessage());
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
        setMainText(searchedText);
        setColors();
    }

    private void setMainText(String searchedText) {
        TextView sectionsTextView = findViewById(R.id.sectionsText);
        sectionsTextView.setText(Html.fromHtml(StringFormatter.getHighlightedText(categoryBodyHelper.getMainText(), searchedText)));
        sectionsTextView.setMovementMethod(LinkMovementMethod.getInstance());   //Make links actually work.
    }

    private void setColors() {
        List<VitalStatistics> vitalStatistics = BjcpDataHelper.getInstance(this).getVitalStatistics(categoryId);
        int i = 1;

        for (VitalStatistics vitalStatistic : vitalStatistics) {
            if (XML_SRM.equals(vitalStatistic.getHeaderTarget()) || !preferencesHelper.isBrewersAssociation()) {
                setColor(vitalStatistic, i);
                i++;
            }
        }
    }

    private void setColor(VitalStatistics vitalStatistics, int i) {
        TextView srmText = getSrmTextView("srmText", i);
        ColorHelper colorHelper = new ColorHelper(this, categoryId);

        srmText.setText(Html.fromHtml(colorHelper.getColorVerbiage(vitalStatistics)));
        srmText.setVisibility(View.VISIBLE);
        setColorBoxes(vitalStatistics, i);
    }


    private void setColorBoxes(VitalStatistics vitalStatistics, int i) {
        TextView srmTextStart = getSrmTextView("srmBoxStart", i);
        TextView srmTextEnd = getSrmTextView("srmBoxEnd", i);
        String startSrm = ((Integer) Double.valueOf(Math.floor(vitalStatistics.getSrmStart())).intValue()).toString();
        String endSrm = ((Integer) Double.valueOf(Math.ceil(vitalStatistics.getSrmEnd())).intValue()).toString();

        if ((null != startSrm) && !ZERO.equals(startSrm)) {
            srmTextStart.setBackgroundColor(getResources().getColor(getResources().getIdentifier(SRM_PREFIX + startSrm, "color", getPackageName())));
            srmTextStart.setVisibility(View.VISIBLE);
        }

        if ((null != endSrm) && !ZERO.equals(endSrm)) {
            srmTextEnd.setBackgroundColor(getResources().getColor(getResources().getIdentifier(SRM_PREFIX + endSrm, "color", getPackageName())));
            srmTextEnd.setVisibility(View.VISIBLE);
        }
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
}