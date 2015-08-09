package net.rlshep.bjcp2015beerstyles;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class OnTapTab extends Fragment {
    private BjcpDataHelper dbHandler;
    private View view;
    private Menu menu;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.view = inflater.inflate(R.layout.on_tap_tab,container,false);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_delete:
                removeFromOnTap();
                Toast.makeText(getActivity().getApplicationContext(), R.string.on_tap_delete, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListView() {
        List listItems = new ArrayList();
        dbHandler = BjcpDataHelper.getInstance(getActivity());

        listItems.addAll(dbHandler.getOnTapSubCategories());

        if (listItems.isEmpty()) {
            listItems.add(getString(R.string.on_tap_empty));
        }

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(getActivity(), listItems);
        ListView onTapListView = (ListView) view.findViewById(R.id.onTapListView);
        onTapListView.setAdapter(subCategoryAdapter);

        onTapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory subCategory = (SubCategory) parent.getItemAtPosition(position);
                loadSubCategoryBody(subCategory);
                removeDeleteIcon();
            }
        });

        onTapListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout onTapListView = (LinearLayout) view.findViewById(R.id.categorySectionRow);
                onTapListView.setSelected(true);

                selectedPosition = position;
                addDeleteIcon();

                return true;
            }
        });
    }

    private void loadSubCategoryBody(SubCategory subCategory) {
        Intent i = new Intent(getActivity(), SubCategoryBodyActivity.class);

        i.putExtra("CATEGORY_ID", (new Long(subCategory.get_categoryId())).toString());
        i.putExtra("SUB_CATEGORY_ID", (new Long(subCategory.get_id())).toString());
        i.putExtra("SUB_CATEGORY", subCategory.get_subCategory());
        i.putExtra("SUB_CATEGORY_NAME", subCategory.get_name());
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(i);
    }

    private void removeFromOnTap() {
        ListView listView = (ListView)getActivity().findViewById(R.id.onTapListView);
        SubCategory subCategory = (SubCategory)listView.getItemAtPosition(selectedPosition);
        subCategory.set_tapped(false);
        dbHandler.updateSubCategoryUntapped(subCategory);
        setListView();
        removeDeleteIcon();
    }

    private void addDeleteIcon() {
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        deleteItem.setVisible(true);
    }

    private void removeDeleteIcon() {
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setVisible(false);
        selectedPosition = -1;
    }
}