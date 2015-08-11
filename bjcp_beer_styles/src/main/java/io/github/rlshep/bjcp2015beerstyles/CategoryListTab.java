package io.github.rlshep.bjcp2015beerstyles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;

public class CategoryListTab extends Fragment {
    private BjcpDataHelper dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_list_tab, container, false);

        setupCategoryListView(v);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();

        dbHandler.close();
    }

    private void setupCategoryListView(View v) {
        dbHandler = BjcpDataHelper.getInstance(getActivity());

        // Only call when debugging new database changes
//        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 1, 1);

        ListAdapter categoryAdapter = new CategoriesListAdapter(getActivity(), dbHandler.getAllCategories());
        ListView categoryListView = (ListView) v.findViewById(R.id.categoryListView);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Category) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    loadSubCategoryList(category);
                }
            }
        });

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean consumed = false;

                if (parent.getItemAtPosition(position) instanceof Category) {
                    addAllSubCategoriesToOnTap((Category) parent.getItemAtPosition(position));
                    consumed = true;
                }

                return consumed;
            }
        });
    }

    private void loadSubCategoryList(Category category) {
        Intent i = new Intent(getActivity(), SubCategoryListActivity.class);

        i.putExtra("CATEGORY_ID", (Long.valueOf(category.get_id())).toString());
        i.putExtra("CATEGORY", category.get_category());
        i.putExtra("CATEGORY_NAME", category.get_name());

        startActivity(i);
    }

    private void addAllSubCategoriesToOnTap(Category category) {
        List<SubCategory> subCategories = dbHandler.getSubCategories(category.get_id());

        for (SubCategory subCategory : subCategories) {
            subCategory.set_tapped(true);
        }

        dbHandler.updateSubCategoriesUntapped(subCategories);

        Toast.makeText(getActivity().getApplicationContext(), R.string.on_tap_success, Toast.LENGTH_SHORT).show();
    }
}