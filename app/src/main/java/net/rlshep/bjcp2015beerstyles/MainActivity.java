package net.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.Category;


public class MainActivity extends AppCompatActivity {

    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new BjcpDataHelper(this, BjcpDataHelper.DATABASE_NAME, null, 1);
        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 1, 1);

        ListAdapter categoryAdapter = new CategoriesListAdapter(this, dbHandler.getAllCategories());
        ListView categoryListView = (ListView) findViewById(R.id.categoryListView);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category)parent.getItemAtPosition(position);
                loadSubCategoryList(category);
            }
        });
    }

    private void loadSubCategoryList(Category category) {
        Intent i = new Intent(this, SubCategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (new Long(category.get_id())).toString());
        i.putExtra("CATEGORY", category.get_category());
        i.putExtra("CATEGORY_NAME", category.get_name());

        startActivity(i);
    }
}
