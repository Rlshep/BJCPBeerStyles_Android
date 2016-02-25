package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class SearchResultsActivity extends BjcpActivity {
    private String searchedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            searchedText = extras.getString("SEARCHED_TEXT");
        }

        String title = getString(R.string.title_activity_search_results) + " " + getString(R.string.title_activity_search_results_small) + " '" + searchedText + "'";
        setupToolbar(R.id.srToolbar, title, false, true);
        setListView(BjcpDataHelper.getInstance(this).search(searchedText));
    }

    private void setListView(List<SearchResult> searchResults) {
        List<Long> categoryIds = new ArrayList<>();

        for (SearchResult searchResult : searchResults) {
            searchedText = searchResult.getQuery();

            if (BjcpContract.TABLE_CATEGORY.equalsIgnoreCase(searchResult.getTableName())) {
                categoryIds.add(searchResult.getResultId());
            }
        }

        setListViewCategories(BjcpDataHelper.getInstance(this).getCategoriesByIds(categoryIds));
    }

    @SuppressWarnings("unchecked")
    private void setListViewCategories(List<Category> categories) {
        List listItems = getFullList(categories);

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(this, listItems);
        ListView listView = (ListView) this.findViewById(R.id.searchResults);
        listView.setAdapter(subCategoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Category) {
                    Category category = (Category) item;

                    if (category.isParent()) {
                        BjcpController.loadCategoryList((Activity) view.getContext(), category, searchedText);
                    } else {
                        BjcpController.loadCategoryBody((Activity) view.getContext(), category, searchedText);
                    }
                }
            }
        });
    }

    private List getFullList(List<Category> categories) {
        List listItems = new ArrayList();

        if (categories.isEmpty()) {
            listItems.add(getString(R.string.no_search_results));
        } else {
            listItems.addAll(categories);
            sortByPriority(categories);
        }

        return listItems;
    }

    // Bringing categories and subcategories who have the search criteria in the name to the top.
    @SuppressWarnings("unchecked")
    private List sortByPriority(List<Category> categories) {
        List sorted = new ArrayList();
        List<Category> catRemaining = new ArrayList<>();
        Collections.sort(categories);

        for (Category category : categories) {
            if (category.getName().toUpperCase().contains(searchedText.toUpperCase())) {
                sorted.add(category);
            } else {
                catRemaining.add(category);
            }
        }
        //TODO: Sort Introductions and Appendix last

        sorted.addAll(catRemaining);

        return sorted;
    }
}
