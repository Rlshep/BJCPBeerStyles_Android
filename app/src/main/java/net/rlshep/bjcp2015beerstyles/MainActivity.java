package net.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;


public class MainActivity extends AppCompatActivity {

    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new BjcpDataHelper(this, BjcpDataHelper.DATABASE_NAME, null, 1);
        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 1, 1);

        ListAdapter categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAllCategories());
        ListView categoryListView = (ListView) findViewById(R.id.categoryListView);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = String.valueOf(parent.getItemAtPosition(position));
                String categoryNumber = category.substring(0, category.indexOf('-') - 1);
                String categoryName = category.substring(category.indexOf('-') + 1, category.length());

                loadSubCategoryList(categoryNumber, categoryName);
            }
        });
    }

    private void loadSubCategoryList(String categoryNumber, String categoryName) {
        Intent i = new Intent(this, SubCategoryListActivity.class);
        i.putExtra("CATEGORY_NUM", categoryNumber);
        i.putExtra("CATEGORY_NAME", categoryName);
        startActivity(i);
    }
}
