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

        String title = getString(R.string.title_activity_search_results) + " <small>" + getString(R.string.title_activity_search_results_small) + " '" + searchedText + "'</small>";
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
        List listItems = new ArrayList();
        listItems.addAll(categories);
        listItems.addAll(subCategories);

        if (listItems.isEmpty()) {
            listItems.add(getString(R.string.no_search_results));
        }

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
}
