package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import io.github.rlshep.bjcp2015beerstyles.controllers.BjcpController;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;

public class OnTapTab extends Fragment {
    private View view;
    private Menu menu;
    private ArrayList<Integer> selectedIds = new ArrayList<Integer>();
    private CategoriesListAdapter subCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.on_tap_tab, container, false);
        setListView();
        return view;
    }

    @Override
    // TODO: Come up with better way to reload when tapped items change than reloading every time.
    public void onResume() {
        super.onResume();
        setListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);

        ListView listView = (ListView) getActivity().findViewById(R.id.onTapListView);
        if (0 < listView.getAdapter().getCount()) {
            addSelectAllIcon();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_delete:
                removeFromOnTap();
                Toast.makeText(getActivity().getApplicationContext(), R.string.on_tap_delete, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_select_all:
                onSelectAllPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    private void setListView() {
        List listItems = new ArrayList();
        listItems.addAll(BjcpDataHelper.getInstance(getActivity()).getOnTapSubCategories());
        subCategoryAdapter = new CategoriesListAdapter(getActivity(), listItems);

        if (listItems.isEmpty()) {
            listItems.add(getString(R.string.on_tap_empty));
            subCategoryAdapter.setSelectEnabled(false);
        }


        ListView onTapListView = (ListView) view.findViewById(R.id.onTapListView);
        onTapListView.setAdapter(subCategoryAdapter);

        onTapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory subCategory = (SubCategory) parent.getItemAtPosition(position);
                BjcpController.loadSubCategoryBody(getActivity(), subCategory);
                removeAllSelected();
            }
        });

        onTapListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedIds.contains(position)) {
                    removeSelected(position);
                } else {
                    addSelectedItem(position);
                }

                return true;
            }
        });
    }

    private void removeFromOnTap() {
        ListView listView = (ListView) getActivity().findViewById(R.id.onTapListView);

        for (Integer selectedId : selectedIds) {
            SubCategory subCategory = (SubCategory) listView.getItemAtPosition(selectedId);
            subCategory.set_tapped(false);
            BjcpDataHelper.getInstance(getActivity()).updateSubCategoryUntapped(subCategory);
        }

        setListView();
        removeAllSelected();
        removeSelectAllIcon();
    }

    private void addDeleteIcon() {
        if (isSelected() && !isFavoritesEmpty()) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            deleteItem.setVisible(true);
        }
    }

    private void removeDeleteIcon() {
        if (!isSelected()) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setVisible(false);
        }
    }

    private void addSelectAllIcon() {
        if (!isFavoritesEmpty()) {
            MenuItem selectAllItem = menu.findItem(R.id.action_select_all);
            selectAllItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            selectAllItem.setVisible(true);
        }
    }

    private void removeSelectAllIcon() {
        if (isFavoritesEmpty()) {
            MenuItem selectAllItem = menu.findItem(R.id.action_select_all);
            selectAllItem.setVisible(false);
        }
    }

    private boolean isFavoritesEmpty() {
        List listItems = new ArrayList();
        listItems.addAll(BjcpDataHelper.getInstance(getActivity()).getOnTapSubCategories());

        return listItems.isEmpty();
    }

    private void onSelectAllPressed() {
        ListView listView = (ListView) getActivity().findViewById(R.id.onTapListView);
        if (listView.getAdapter().getCount() == selectedIds.size()) {
            removeAllSelected();
        } else {
            selectAllItems();
        }
    }

    private void selectAllItems() {
        ListView listView = (ListView) getActivity().findViewById(R.id.onTapListView);
        CategoriesListAdapter subCategoryAdapter = (CategoriesListAdapter) listView.getAdapter();
        selectedIds = new ArrayList<Integer>(); //reset selection

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, true);
            selectedIds.add(i);
        }

        addDeleteIcon();
        subCategoryAdapter.setSelectedIds(selectedIds);
        subCategoryAdapter.notifyDataSetChanged();
    }

    private void addSelectedItem(Integer position) {
        selectedIds.add(position);
        addDeleteIcon();
        subCategoryAdapter.setSelectedIds(selectedIds);
        subCategoryAdapter.notifyDataSetChanged();
    }

    private void removeSelected(Integer position) {
        selectedIds.remove(position);
        subCategoryAdapter.setSelectedIds(selectedIds);
        subCategoryAdapter.notifyDataSetChanged();

        if (selectedIds.isEmpty()) {
            removeDeleteIcon();
        }
    }

    private void removeAllSelected() {
        selectedIds = new ArrayList<Integer>();
        subCategoryAdapter.setSelectedIds(selectedIds);
        subCategoryAdapter.notifyDataSetChanged();
        removeDeleteIcon();
    }

    private boolean isSelected() {
        return !selectedIds.isEmpty();
    }
}