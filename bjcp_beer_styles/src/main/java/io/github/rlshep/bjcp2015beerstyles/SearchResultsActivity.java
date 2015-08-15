package io.github.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpContract;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;

public class SearchResultsActivity extends AppCompatActivity {
    private BjcpDataHelper dbHandler;
    private String searchedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setupToolbar();
        dbHandler = BjcpDataHelper.getInstance(this);
        setListView(dbHandler.getSearchResults());
    }

    private void setListView(List<SearchResult> searchResults) {
        List<Long> categoryIds = new ArrayList<>();
        List<Long> subCategoryIds = new ArrayList<>();

        for (SearchResult searchResult : searchResults) {
            searchedText = searchResult.get_query();

            if (BjcpContract.TABLE_CATEGORY.equalsIgnoreCase(searchResult.get_TableName())) {
                categoryIds.add(searchResult.get_resultId());
            } else if (BjcpContract.TABLE_SUB_CATEGORY.equalsIgnoreCase(searchResult.get_TableName())) {
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
                    loadSubCategoryBody(subCategory);
                }
                else {
                    Category category = (Category) item;
                    loadSubCategoryList(category);
                }
            }
        });
    }

    private void loadSubCategoryBody(SubCategory subCategory) {
        Intent i = new Intent(this, SubCategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(subCategory.get_categoryId())).toString());
        i.putExtra("SUB_CATEGORY_ID", (Long.valueOf(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());
        i.putExtra("SEARCHED_TEXT", searchedText);

        startActivity(i);
    }

    private void loadSubCategoryList(Category category) {
        Intent i = new Intent(this, SubCategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.get_id())).toString());
        i.putExtra("CATEGORY", category.get_category());
        i.putExtra("CATEGORY_NAME", category.get_name());
        i.putExtra("SEARCHED_TEXT", searchedText);

        startActivity(i);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.srToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
