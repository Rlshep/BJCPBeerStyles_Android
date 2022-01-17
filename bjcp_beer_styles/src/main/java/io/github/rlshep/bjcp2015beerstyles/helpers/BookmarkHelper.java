package io.github.rlshep.bjcp2015beerstyles.helpers;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;

public class BookmarkHelper {

    public void addAllCategoriesToBookmarked(Activity activity, Category category) {
        List<Category> categories = getAllChildCategories(category);
        BjcpDataHelper.getInstance((BjcpActivity)activity).updateCategoriesBookmarked(categories);
        Toast.makeText(activity.getApplicationContext(), R.string.on_tap_success, Toast.LENGTH_SHORT).show();
    }

    private List<Category> getAllChildCategories(Category category) {
        List<Category> categories = new ArrayList<>();

        if (0 < category.getChildCategories().size()) {
            for (Category cat : category.getChildCategories()) {
                categories.addAll(getAllChildCategories(cat));
            }
        } else {
            category.setBookmarked(true);
            categories.add(category);
        }

        return categories;
    }
}
