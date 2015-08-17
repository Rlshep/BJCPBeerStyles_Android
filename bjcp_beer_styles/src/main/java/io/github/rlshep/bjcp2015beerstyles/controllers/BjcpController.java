package io.github.rlshep.bjcp2015beerstyles.controllers;

import android.app.Activity;
import android.content.Intent;

import io.github.rlshep.bjcp2015beerstyles.AboutActivity;
import io.github.rlshep.bjcp2015beerstyles.CrashActivity;
import io.github.rlshep.bjcp2015beerstyles.SearchResultsActivity;
import io.github.rlshep.bjcp2015beerstyles.SubCategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.SubCategoryListActivity;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class BjcpController {

    public static void loadSubCategoryList(Activity activity, Category category) {
        loadSubCategoryList(activity, category, "");
    }

    public static void loadSubCategoryList(Activity activity, Category category, String searchedText) {
        Intent i = new Intent(activity, SubCategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.get_id())).toString());
        i.putExtra("CATEGORY", category.get_category());
        i.putExtra("CATEGORY_NAME", category.get_name());
        i.putExtra("SEARCHED_TEXT", searchedText);

        activity.startActivity(i);
    }

    public static void loadSubCategoryBody(Activity activity, SubCategory subCategory) {
        loadSubCategoryBody(activity, subCategory, "");
    }

    public static void loadSubCategoryBody(Activity activity, SubCategory subCategory, String searchedText) {
        Intent i = new Intent(activity, SubCategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(subCategory.get_categoryId())).toString());
        i.putExtra("SUB_CATEGORY_ID", (Long.valueOf(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());
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
