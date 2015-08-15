package io.github.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;
import io.github.rlshep.bjcp2015beerstyles.listeners.GestureListener;


public class SubCategoryListActivity extends AppCompatActivity {
    private BjcpDataHelper dbHandler;
    private GestureDetector gestureDetector;
    private String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
        dbHandler = BjcpDataHelper.getInstance(this);
        String searchedText = "";

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String title = extras.getString("CATEGORY") + " - " + extras.getString("CATEGORY_NAME");
            setupToolbar(title);

            categoryId = extras.getString("CATEGORY_ID");
            searchedText = extras.getString("SEARCHED_TEXT");
        }

        gestureDetector = new GestureDetector(this, new GestureListener());
        setListView(categoryId, searchedText);
    }

    @SuppressWarnings("unchecked")
    private void setListView(String categoryId, String searchedText) {
        List listView = new ArrayList();

        listView.addAll(dbHandler.getCategorySections(categoryId));
        listView.addAll(dbHandler.getSubCategories(categoryId));

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(this, listView, searchedText);
        ListView subCategoryListView = (ListView) findViewById(R.id.subCategoryListView);
        subCategoryListView.setAdapter(subCategoryAdapter);

        subCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof SubCategory) {
                    SubCategory subCategory = (SubCategory) parent.getItemAtPosition(position);
                    loadSubCategoryBody(subCategory);
                }
            }
        });

        subCategoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean consumed = false;

                if (parent.getItemAtPosition(position) instanceof SubCategory) {
                    addSubCategoryToOnTap((SubCategory) parent.getItemAtPosition(position));
                    consumed = true;
                }

                return consumed;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        boolean eventReturn;
        boolean eventConsumed = gestureDetector.onTouchEvent(event);

        if (eventConsumed) {
            if (GestureListener.SWIPE_LEFT.equals(GestureListener.currentGesture)) {
                changeCategory(-1);
            } else if (GestureListener.SWIPE_RIGHT.equals(GestureListener.currentGesture)) {
                changeCategory(1);
            }

            eventReturn = true;
        }
        else {
            eventReturn = super.dispatchTouchEvent(event);
        }

        return eventReturn;
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sclToolbar);
        toolbar.setTitle(StringFormatter.getFormattedTitle(title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addSubCategoryToOnTap(SubCategory subCategory) {
        subCategory.set_tapped(true);
        dbHandler.updateSubCategoryUntapped(subCategory);
        Toast.makeText(getApplicationContext(), R.string.on_tap_success, Toast.LENGTH_SHORT).show();
    }

    private void changeCategory(int i) {
        List<Category> categories = dbHandler.getAllCategories();
        Category category = dbHandler.getCategory(categoryId);
        int newOrder =  category.get_orderNumber() + i;

        if (0 <= newOrder && categories.size() > newOrder) {
            for (Category c : categories) {
                if (newOrder == c.get_orderNumber()) {
                    loadSubCategoryList(c);
                }
            }
        }
    }

    private void loadSubCategoryList(Category category) {
        Intent i = new Intent(this, SubCategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.get_id())).toString());
        i.putExtra("CATEGORY", category.get_category());
        i.putExtra("CATEGORY_NAME", category.get_name());

        startActivity(i);
    }

    private void loadSubCategoryBody(SubCategory subCategory) {
        Intent i = new Intent(this, SubCategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(subCategory.get_categoryId())).toString());
        i.putExtra("SUB_CATEGORY_ID", (Long.valueOf(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());

        startActivity(i);
    }
}
