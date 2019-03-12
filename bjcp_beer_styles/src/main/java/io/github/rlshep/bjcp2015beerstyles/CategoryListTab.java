package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
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
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;

public class CategoryListTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_list_tab, container, false);

        setupCategoryListView(v);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        BjcpDataHelper.getInstance((BjcpActivity)getActivity()).close();
    }

    private void setupCategoryListView(View view) {
        ListAdapter categoryAdapter = new CategoriesListAdapter(getActivity(), BjcpDataHelper.getInstance((BjcpActivity)getActivity()).getAllCategories());
        ListView categoryListView = (ListView) view.findViewById(R.id.categoryListView);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Category) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    BjcpController.loadCategoryList((Activity) view.getContext(), category);
                }
            }
        });

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean consumed = false;

                if (parent.getItemAtPosition(position) instanceof Category) {
                    addAllCategoriesToBookmarked((Category) parent.getItemAtPosition(position));
                    consumed = true;
                }

                return consumed;
            }
        });
    }

    private void addAllCategoriesToBookmarked(Category category) {
        List<Category> categories = BjcpDataHelper.getInstance((BjcpActivity)getActivity()).getCategoriesByParent(category.getId());

        for (Category cat : categories) {
            cat.setBookmarked(true);
        }

        BjcpDataHelper.getInstance((BjcpActivity)getActivity()).updateCategoriesBookmarked(categories);
        Toast.makeText(getActivity().getApplicationContext(), R.string.on_tap_success, Toast.LENGTH_SHORT).show();
    }
}