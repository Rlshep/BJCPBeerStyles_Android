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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String searchedText = "";
        String vitalsQuery = "";

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
        setListView(getSearchResults(searchedText, vitalsQuery), searchedText);
    }

    private void setListView(List<SearchResult> searchResults, String searchedText) {
        List<Long> categoryIds = new ArrayList<>();

        for (SearchResult searchResult : searchResults) {
            searchedText = searchResult.getQuery();

            if (BjcpContract.TABLE_CATEGORY.equalsIgnoreCase(searchResult.getTableName())) {
                categoryIds.add(searchResult.getResultId());
            }
        }

        setListViewCategories(BjcpDataHelper.getInstance(this).getCategoriesByIds(categoryIds), searchedText);
    }

    @SuppressWarnings("unchecked")
    private void setListViewCategories(List<Category> categories, String searchedText) {
        List listItems = getFullList(categories, searchedText);

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

    private List getFullList(List<Category> categories, String searchedText) {
        List listItems = new ArrayList();

        if (categories.isEmpty()) {
            listItems.add(getString(R.string.no_search_results));
        } else {
            listItems.addAll(sortByPriority(categories, searchedText));
        }

        return listItems;
    }

    // Bringing categories and subcategories who have the search criteria in the name to the top.
    @SuppressWarnings("unchecked")
    private List sortByPriority(List<Category> categories, String searchedText) {
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

    private List<SearchResult> getSearchResults(String searchedText, String vitalsQuery) {
        List<SearchResult> searchResults = new ArrayList<>();

        if (!StringUtils.isEmpty(searchedText) && !StringUtils.isEmpty(vitalsQuery)) {  // Only what is in both keyword and vitals
            List<SearchResult> keywordResults = BjcpDataHelper.getInstance(this).search(StringFormatter.addDoubleSingleQuotes(searchedText));
            List<SearchResult> vitalsResults = BjcpDataHelper.getInstance(this).searchVitals(vitalsQuery);

            searchResults = intersection(keywordResults, vitalsResults);
        } else if (!StringUtils.isEmpty(searchedText)) {    // Only keyword
            searchResults = BjcpDataHelper.getInstance(this).search(StringFormatter.addDoubleSingleQuotes(searchedText));
        } else {     // Only vitals
            searchResults = BjcpDataHelper.getInstance(this).searchVitals(vitalsQuery);
        }

        return searchResults;
    }

    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}
