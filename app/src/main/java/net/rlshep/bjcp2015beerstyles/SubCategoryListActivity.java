package net.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;


public class SubCategoryListActivity extends AppCompatActivity {
    BjcpDataHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String categoryNumber = "";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
        dbHandler = new BjcpDataHelper(this, BjcpDataHelper.DATABASE_NAME, null, 1);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            this.setTitle(extras.getString("CATEGORY_NAME"));
            categoryNumber = extras.getString("CATEGORY_NUM");
        }

        setNotesText(categoryNumber);
        setListView(categoryNumber);
    }

    private void setNotesText(String categoryNumber) {
        String notes = dbHandler.getCategoryNotes(categoryNumber);
        TextView notesText = (TextView) findViewById(R.id.categoryNotes);
        notesText.setText(notes);
    }

    private void setListView(String categoryNumber) {
        ListAdapter subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getSubCategories(categoryNumber));
        ListView subCategoryListView = (ListView) findViewById(R.id.subCategoryListView);
        subCategoryListView.setAdapter(subCategoryAdapter);
    }
}
