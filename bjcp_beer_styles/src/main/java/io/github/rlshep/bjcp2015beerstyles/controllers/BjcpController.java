package io.github.rlshep.bjcp2015beerstyles.controllers;

import android.app.Activity;
import android.content.Intent;

import org.apache.commons.lang.StringUtils;

import io.github.rlshep.bjcp2015beerstyles.AboutActivity;
import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.CategoryListActivity;
import io.github.rlshep.bjcp2015beerstyles.CrashActivity;
import io.github.rlshep.bjcp2015beerstyles.SearchResultsActivity;
import io.github.rlshep.bjcp2015beerstyles.SettingsActivity;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class BjcpController {

    public static void loadCategory(Activity activity, Category category) {
        loadCategory(activity, category, "");
    }

    public static void loadCategory(Activity activity, Category category, String searchedText) {
        if (0 < category.getChildCategories().size()) {
            loadCategoryList(activity, category, searchedText);
        } else {
            loadCategoryBody(activity, category, searchedText);
        }
    }

    public static void loadCategoryList(Activity activity, Category category) {
        loadCategoryList(activity, category, "");
    }

    public static void loadCategoryList(Activity activity, Category category, String searchedText) {
        Intent i = new Intent(activity, CategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.getId())).toString());
        i.putExtra("CATEGORY", (StringUtils.isEmpty(category.getTruncatedCategoryCode())) ? "" : category.getTruncatedCategoryCode());
        i.putExtra("CATEGORY_NAME", (StringUtils.isEmpty(category.getName())) ? "" : category.getName());
        i.putExtra("SEARCHED_TEXT", searchedText);

        activity.startActivity(i);
    }

    public static void loadCategoryBody(Activity activity, Category category) {
        loadCategoryBody(activity, category, "");
    }

    public static void loadCategoryBody(Activity activity, Category category, String searchedText) {
        Intent i = new Intent(activity, CategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.getId())).toString());
        i.putExtra("CATEGORY", (StringUtils.isEmpty(category.getTruncatedCategoryCode())) ? "" : category.getTruncatedCategoryCode());
        i.putExtra("CATEGORY_NAME", (StringUtils.isEmpty(category.getName())) ? "" : category.getName());
        i.putExtra("SEARCHED_TEXT", searchedText);

        activity.startActivity(i);
    }

    public static void startSearchResultsActivity(Activity activity, String keyword) {
        startSearchResultsActivity(activity, keyword, "");
    }

    public static void startSearchResultsActivity(Activity activity, String keyword, String query) {
        Intent i = new Intent(activity, SearchResultsActivity.class);
        i.putExtra("SEARCHED_TEXT", keyword);
        i.putExtra("VITALS_QUERY", query);
        activity.startActivity(i);
    }

    public static void startAboutActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    public static void startCrashActivity(Activity activity, String error) {
        Intent intent = new Intent(activity, CrashActivity.class);
        intent.putExtra(ExceptionHandler.EXTRA_ERROR, error);
        activity.startActivity(intent);
    }

    public static void startSettingsActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }
}
