package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.commons.lang.StringUtils;

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
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;

public class SearchResultsActivity extends BjcpActivity {
    protected String searchedText = "";
    protected String vitalsQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        String title = getString(R.string.title_activity_search_results);

        if (extras != null) {
            searchedText = extras.getString("SEARCHED_TEXT");
            vitalsQuery = extras.getString("VITALS_QUERY");
        }

        if (!StringUtils.isEmpty(searchedText)) {
             title +=  " " + getString(R.string.title_activity_search_results_small) + " '" + searchedText + "'";
        }

        setupToolbar(R.id.srToolbar, title, false, true);
        setListView(getSearchResults());
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
            listItems.addAll(sortByPriority(categories));
        }

        return listItems;
    }

    // Bringing categories and subcategories who have the search criteria in the name to the top.
    @SuppressWarnings("unchecked")
    private List sortByPriority(List<Category> categories) {
        List<Category> sorted = new ArrayList<>();
        List<Category> catRemaining = new ArrayList<>();
        List<Category> catIntros = new ArrayList<>();
        List<Category> catAppendixes = new ArrayList<>();

        Collections.sort(categories);

        for (Category category : categories) {
            if (!StringUtils.isEmpty(searchedText) && category.getName().toUpperCase().contains(searchedText.toUpperCase())) {
                sorted.add(category);
            } else if (category.getCategoryCode().startsWith("I")) {
                catIntros.add(category);
            } else if (category.getCategoryCode().startsWith("A")) {
                catAppendixes.add(category);
            } else {
                catRemaining.add(category);
            }
        }

        sorted.addAll(catRemaining);
        sorted.addAll(catIntros);
        sorted.addAll(catAppendixes);

        return sorted;
    }

    private List<SearchResult> getSearchResults() {
        List<SearchResult> searchResults;

        if (StringUtils.isEmpty(vitalsQuery)) {
            searchResults = BjcpDataHelper.getInstance(this).search(StringFormatter.addDoubleSingleQuotes(searchedText));
        }  else {
            searchResults = BjcpDataHelper.getInstance(this).searchVitals(vitalsQuery);
        }

        return searchResults;
    }
}
