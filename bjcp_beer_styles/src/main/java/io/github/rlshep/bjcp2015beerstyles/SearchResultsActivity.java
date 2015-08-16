package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpContract;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;

public class SearchResultsActivity extends BjcpActivity {
    private BjcpDataHelper dbHandler;
    private String searchedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        dbHandler = BjcpDataHelper.getInstance(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            searchedText = extras.getString("SEARCHED_TEXT");
        }
// TODO: Fix someday. Can't format titles in Android v4.1 and lower. http://stackoverflow.com/questions/7658725/android-java-lang-illegalargumentexception-invalid-payload-item-type/25674354#25674354
//        String title = getString(R.string.title_activity_search_results) + " <small>" + getString(R.string.title_activity_search_results_small) + " '" + searchedText + "'</small>";
        String title = getString(R.string.title_activity_search_results) + " " + getString(R.string.title_activity_search_results_small) + " '" + searchedText + "'";
        setupToolbar(R.id.srToolbar, title, false, true);
        setListView(dbHandler.search(searchedText));
    }

    private void setListView(List<SearchResult> searchResults) {
        List<Long> categoryIds = new ArrayList<>();
        List<Long> subCategoryIds = new ArrayList<>();

        for (SearchResult searchResult : searchResults) {
            searchedText = searchResult.get_query();

            if (BjcpContract.TABLE_CATEGORY.equalsIgnoreCase(searchResult.get_TableName())) {
                categoryIds.add(searchResult.get_resultId());
            }
            else if (BjcpContract.TABLE_SUB_CATEGORY.equalsIgnoreCase(searchResult.get_TableName())) {
                subCategoryIds.add(searchResult.get_resultId());
            }
        }

        setListView(dbHandler.getCategoriesByIds(categoryIds), dbHandler.getSubCategoriesByIds(subCategoryIds));
    }

    @SuppressWarnings("unchecked")
    private void setListView(List<Category> categories, List<SubCategory> subCategories) {
        List listItems = getFullList(categories, subCategories);

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(this, listItems);
        ListView listView = (ListView) this.findViewById(R.id.searchResults);
        listView.setAdapter(subCategoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof SubCategory) {
                    SubCategory subCategory = (SubCategory) item;
                    BjcpController.loadSubCategoryBody((Activity) view.getContext(), subCategory, searchedText);
                }
                else {
                    Category category = (Category) item;
                    BjcpController.loadSubCategoryList((Activity) view.getContext(), category, searchedText);
                }
            }
        });
    }

    private List getFullList(List<Category> categories, List<SubCategory> subCategories) {
        List listItems;

        if (categories.isEmpty() && subCategories.isEmpty()) {
            listItems = new ArrayList();
            listItems.add(getString(R.string.no_search_results));
        } else {
            listItems = sortByPriority(categories, subCategories);
        }

        return listItems;
    }

    // Bringing categories and subcategories who have the search criteria in the name to the top.
    @SuppressWarnings("unchecked")
    private List sortByPriority(List<Category> categories, List<SubCategory> subCategories) {
        List sorted = new ArrayList();
        List<Category> catRemaining = new ArrayList<>();
        List<SubCategory> subCatRemaining = new ArrayList<>();

        for (Category category : categories) {
            if (category.get_name().toUpperCase().contains(searchedText.toUpperCase())) {
                sorted.add(category);
            } else {
                catRemaining.add(category);
            }
        }

        for (SubCategory subCategory : subCategories) {
            if (subCategory.get_name().toUpperCase().contains(searchedText.toUpperCase())) {
                sorted.add(subCategory);
            }else {
                subCatRemaining.add(subCategory);
            }
        }

        sorted.addAll(catRemaining);
        sorted.addAll(subCatRemaining);

        return sorted;
    }
}