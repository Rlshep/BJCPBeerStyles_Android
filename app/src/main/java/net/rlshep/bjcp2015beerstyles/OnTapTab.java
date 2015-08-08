package net.rlshep.bjcp2015beerstyles;

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

import net.rlshep.bjcp2015beerstyles.adapters.CategoriesListAdapter;
import net.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class OnTapTab extends Fragment {
    private BjcpDataHelper dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.on_tap_tab,container,false);

        dbHandler = BjcpDataHelper.getInstance(getActivity());
        setListView(v);

        return v;
    }

    private void setListView(View view) {
        List listView = new ArrayList();

        listView.addAll(dbHandler.getOnTapSubCategories());

        ListAdapter subCategoryAdapter = new CategoriesListAdapter(getActivity(), listView);
        ListView onTapListView = (ListView) view.findViewById(R.id.onTapListView);
        onTapListView.setAdapter(subCategoryAdapter);

        onTapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory subCategory = (SubCategory) parent.getItemAtPosition(position);
                loadSubCategoryBody(subCategory);
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
}