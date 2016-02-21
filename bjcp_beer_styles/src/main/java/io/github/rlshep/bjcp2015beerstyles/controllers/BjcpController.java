package io.github.rlshep.bjcp2015beerstyles.controllers;

import android.app.Activity;
import android.content.Intent;

import io.github.rlshep.bjcp2015beerstyles.AboutActivity;
import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.CrashActivity;
import io.github.rlshep.bjcp2015beerstyles.SearchResultsActivity;
import io.github.rlshep.bjcp2015beerstyles.CategoryListActivity;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class BjcpController {

    public static void loadCategoryList(Activity activity, Category category) {
        loadCategoryList(activity, category, "");
    }

    public static void loadCategoryList(Activity activity, Category category, String searchedText) {
        Intent i = new Intent(activity, CategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.getId())).toString());
        i.putExtra("CATEGORY", category.getCategory());
        i.putExtra("CATEGORY_NAME", category.getName());
        i.putExtra("SEARCHED_TEXT", searchedText);

        activity.startActivity(i);
    }

    public static void loadCategoryBody(Activity activity, Category category) {
        loadCategoryBody(activity, category, "");
    }

    public static void loadCategoryBody(Activity activity, Category category, String searchedText) {
        Intent i = new Intent(activity, CategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.getId())).toString());
        i.putExtra("CATEGORY", category.getCategory());
        i.putExtra("CATEGORY_NAME", category.getName());
        i.putExtra("SEARCHED_TEXT", searchedText);

        activity.startActivity(i);
    }

    public static void startSearchResultsActivity(Activity activity, String keyword) {
        Intent i = new Intent(activity, SearchResultsActivity.class);
        i.putExtra("SEARCHED_TEXT", keyword);
        activity.startActivity(i);
    }

    public static void startAboutActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    public static void startCrashActivity(Activity activity, String error) {
        Intent intent = new Intent(activity, CrashActivity.class);
        intent.putExtra(ExceptionHandler.EXTRA_ERROR, error.toString());
        activity.startActivity(intent);
    }
}
