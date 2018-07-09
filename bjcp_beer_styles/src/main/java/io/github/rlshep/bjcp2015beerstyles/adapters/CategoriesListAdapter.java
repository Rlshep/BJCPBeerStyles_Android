package io.github.rlshep.bjcp2015beerstyles.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.formatters.StringFormatter;

public class CategoriesListAdapter extends ArrayAdapter {
    private String searchedText = "";
    private ArrayList<Integer> selectedIds = new ArrayList<Integer>();
    private boolean selectEnabled = true;

    @SuppressWarnings("unchecked")
    public CategoriesListAdapter(Context context, List listValues) {
        super(context, R.layout.categories_list_row, listValues);
    }

    @SuppressWarnings("unchecked")
    public CategoriesListAdapter(Context context, List listValues, String searchedText) {
        super(context, R.layout.categories_list_row, listValues);
        this.searchedText = searchedText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listRowView = inflater.inflate(R.layout.categories_list_row, parent, false);
        TextView rowText;
        Object item = getItem(position);

        //Used for on tap only
        listRowView.setBackgroundColor((selectedIds.contains(position) ? listRowView.getResources().getColor(R.color.pressed_color) : listRowView.getResources().getColor(android.R.color.transparent)));

        if (item instanceof Section) {
            Section section = (Section) item;
            rowText = (TextView) listRowView.findViewById(R.id.catSectionText);
            rowText.setText(Html.fromHtml(StringFormatter.getHighlightedText(section.getBody(), searchedText)));
//TODO: FIX
            rowText.setTextIsSelectable(true);
        }
        if (item instanceof Category) {
            Category category = (Category) item;
            rowText = (TextView) listRowView.findViewById(R.id.catListText);
            rowText.setText(category.getTruncatedCategoryCode() + " - " + category.getName());
        } else if (item instanceof String) {
            rowText = (TextView) listRowView.findViewById(R.id.catSectionText);
            rowText.setText((String) item);
//TODO: FIX
            rowText.setTextIsSelectable(true);
        }

        return listRowView;
    }

    public void setSelectedIds(ArrayList<Integer> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public boolean isEnabled(int position) {
        return selectEnabled;
    }

    public void setSelectEnabled(boolean enabled) {
        selectEnabled = enabled;
    }
}
