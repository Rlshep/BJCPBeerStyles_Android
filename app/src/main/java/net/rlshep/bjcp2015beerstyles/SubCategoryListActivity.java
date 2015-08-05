package net.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;

import java.util.ArrayList;
import java.util.List;


public class SubCategoryListActivity extends AppCompatActivity {
    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String categoryId = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
        dbHandler = BjcpDataHelper.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            this.setTitle(extras.getString("CATEGORY") + " - " + extras.getString("CATEGORY_NAME"));
            categoryId = extras.getString("CATEGORY_ID");
        }

        setListView(categoryId);
    }

    private void setListView(String categoryId) {
        List listView = new ArrayList();

        listView.addAll(dbHandler.getCategorySections(categoryId));
        listView.addAll(dbHandler.getSubCategories(categoryId));

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(this, listView);
        ListView subCategoryListView = (ListView) findViewById(R.id.subCategoryListView);
        subCategoryListView.setAdapter(subCategoryAdapter);

        subCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory subCategory = (SubCategory) parent.getItemAtPosition(position);
                loadSubCategoryBody(subCategory);
            }
        });
    }

    public void loadSubCategoryBody(SubCategory subCategory) {
        Intent i = new Intent(this, SubCategoryBodyActivity.class);

        i.putExtra("SUB_CATEGORY_ID", (new Long(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());

        startActivity(i);
    }
}
