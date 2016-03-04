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
import io.github.rlshep.bjcp2015beerstyles.domain.Category;

public class BookmarkedTab extends Fragment {
    private View view;
    private Menu menu;
    private ArrayList<Integer> selectedIds = new ArrayList<Integer>();
    private CategoriesListAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.bookmarked_tab, container, false);
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

        ListView listView = (ListView) getActivity().findViewById(R.id.bookmarkedListView);
        if (0 < listView.getAdapter().getCount() && listView.getItemAtPosition(0) instanceof Category) {
            addSelectAllIcon();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_delete:
                removeFromBookmarked();
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
        listItems.addAll(BjcpDataHelper.getInstance(getActivity()).getBookmarkedCategories());

        if (listItems.isEmpty()) {
            listItems.add(getString(R.string.on_tap_empty));
        }

        categoryAdapter = new CategoriesListAdapter(getActivity(), listItems);
        ListView bookmarkedListView = (ListView) view.findViewById(R.id.bookmarkedListView);
        bookmarkedListView.setAdapter(categoryAdapter);

        bookmarkedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Category) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    BjcpController.loadCategoryBody(getActivity(), category);
                    removeAllSelected();
                }
            }
        });

        bookmarkedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Category) {
                    if (selectedIds.contains(position)) {
                        removeSelected(position);
                    } else {
                        addSelectedItem(position);
                    }
                }

                return true;
            }
        });
    }

    private void removeFromBookmarked() {
        ListView listView = (ListView) getActivity().findViewById(R.id.bookmarkedListView);

        for (Integer selectedId : selectedIds) {
            Category category = (Category) listView.getItemAtPosition(selectedId);
            category.setBookmarked(false);
            BjcpDataHelper.getInstance(getActivity()).updateCategoryBookmarked(category);
        }

        setListView();
        removeAllSelected();
    }

    private void addDeleteIcon() {
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        deleteItem.setVisible(true);
    }

    private void removeDeleteIcon() {
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setVisible(false);
    }

    private void addSelectAllIcon() {
        MenuItem selectAllItem = menu.findItem(R.id.action_select_all);
        selectAllItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        selectAllItem.setVisible(true);
    }

    private void checkSelectAllNeedRemoved() {
        ListView listView = (ListView) getActivity().findViewById(R.id.bookmarkedListView);
        if (0 >= listView.getAdapter().getCount() || !(listView.getItemAtPosition(0) instanceof Category)) {
            removeSelectAllIcon();
        }
    }

    private void removeSelectAllIcon() {
        MenuItem selectAllItem = menu.findItem(R.id.action_select_all);
        selectAllItem.setVisible(false);
    }

    private void onSelectAllPressed() {
        ListView listView = (ListView) getActivity().findViewById(R.id.bookmarkedListView);
        if (listView.getAdapter().getCount() == selectedIds.size()) {
            removeAllSelected();
        } else {
            selectAllItems();
        }
    }

    private void selectAllItems() {
        ListView listView = (ListView) getActivity().findViewById(R.id.bookmarkedListView);
        CategoriesListAdapter categoryAdapter = (CategoriesListAdapter) listView.getAdapter();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            listView.setItemChecked(i, true);
            selectedIds.add(i);
        }

        addDeleteIcon();
        categoryAdapter.setSelectedIds(selectedIds);
        categoryAdapter.notifyDataSetChanged();
    }

    private void addSelectedItem(Integer position) {
        selectedIds.add(position);
        addDeleteIcon();
        categoryAdapter.setSelectedIds(selectedIds);
        categoryAdapter.notifyDataSetChanged();
    }

    private void removeSelected(Integer position) {
        selectedIds.remove(position);
        categoryAdapter.setSelectedIds(selectedIds);
        categoryAdapter.notifyDataSetChanged();

        if (selectedIds.isEmpty()) {
            removeDeleteIcon();
        }
    }

    private void removeAllSelected() {
        selectedIds = new ArrayList<Integer>();
        categoryAdapter.setSelectedIds(selectedIds);
        categoryAdapter.notifyDataSetChanged();
        removeDeleteIcon();
        checkSelectAllNeedRemoved();
    }
}