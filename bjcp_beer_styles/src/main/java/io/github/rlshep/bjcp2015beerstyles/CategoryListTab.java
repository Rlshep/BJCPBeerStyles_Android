package io.github.rlshep.bjcp2015beerstyles;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.helpers.BookmarkHelper;

public class CategoryListTab extends Fragment {
    private View v;
    private Parcelable state = null;
    private BookmarkHelper bookmarkHelper = new BookmarkHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.category_list_tab, container, false);

        setupCategoryListView(v);

        return v;
    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        ListView categoryListView = v.findViewById(R.id.categoryListView);
        state = categoryListView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        BjcpDataHelper.getInstance((BjcpActivity)getActivity()).close();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != v) {
            setupCategoryListView(v);
        }
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
                    bookmarkHelper.addAllCategoriesToBookmarked((Activity) view.getContext(), (Category) parent.getItemAtPosition(position));
                    consumed = true;
                }

                return consumed;
            }
        });

        // Restore previous state (including selected item index and scroll position)
        if(state != null) {
            categoryListView.onRestoreInstanceState(state);
        }
    }
}