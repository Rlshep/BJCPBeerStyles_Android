package io.github.rlshep.bjcp2015beerstyles.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class BjcpDataHelper extends BaseDataHelper {
    private static BjcpDataHelper instance;

    protected BjcpDataHelper(Activity context) {
        super(context);
    }

    // Use the application context, which will ensure that you don't accidentally leak an Activity's context. See this article for more information: http://bit.ly/6LRzfx
    public static synchronized BjcpDataHelper getInstance(Activity context) {
        if (instance == null) {
            instance = new BjcpDataHelper(context);
        }

        return instance;
    }

    public Category getCategory(String categoryId) {
        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CAT + ", " + BjcpContract.COLUMN_NAME + ", " + BjcpContract.COLUMN_ORDER + " FROM " + BjcpContract.TABLE_CATEGORY + " WHERE " + BjcpContract.COLUMN_ID + " = " + categoryId;
        return getCategories(query).get(0);
    }

    public List<Category> getAllCategories() {
        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CAT + ", " + BjcpContract.COLUMN_NAME + ", " + BjcpContract.COLUMN_ORDER + " FROM " + BjcpContract.TABLE_CATEGORY + " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND " + BjcpContract.COLUMN_REVISION + " = " + Category.CURRENT_REVISION + " ORDER BY " + BjcpContract.COLUMN_ORDER;
        return getCategories(query);
    }

    public List<Category> getCategoriesByIds(List<Long> ids) {
        List<Category> categories = new ArrayList<>();

        if (null != ids && !ids.isEmpty()) {
            categories = getCategories(getCategoriesByIdsQuery(ids));
        }

        return categories;
    }

    private String getCategoriesByIdsQuery(List<Long> ids) {
        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CAT + ", " + BjcpContract.COLUMN_NAME + ", " + BjcpContract.COLUMN_ORDER + " FROM " + BjcpContract.TABLE_CATEGORY + " WHERE " + BjcpContract.COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";

        return query;
    }

    private String getIdsQuery(List<Long> ids) {
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < ids.size(); i++) {
            if (0 < i) {
                query.append(", ");
            }
            query.append(ids.get(i));
        }

        return query.toString();
    }

    private List<Category> getCategories(String query) {
        List<Category> categories = new ArrayList<Category>();
        Category category;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                category = new Category(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_ID)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_CAT)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME)));
                category.set_orderNumber(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ORDER)));
                categories.add(category);

            }
            c.moveToNext();
        }

        c.close();

        return categories;
    }

    public List<Section> getCategorySections(String categoryId) {
        List<Section> sections = new ArrayList<Section>();
        Section section;

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM " + BjcpContract.TABLE_SECTION + " S WHERE S." + BjcpContract.COLUMN_CAT_ID + " = " + categoryId + " ORDER BY S." + BjcpContract.COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            section = new Section();
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                section.set_id(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                section.set_header(c.getString(c.getColumnIndex(BjcpContract.COLUMN_HEADER)));
                section.set_body(c.getString(c.getColumnIndex(BjcpContract.COLUMN_BODY)));
            }
            c.moveToNext();
            sections.add(section);
        }

        c.close();

        return sections;
    }

    public List<SubCategory> getSubCategories(long categoryId) {
        return getSubCategories((Long.valueOf(categoryId)).toString());
    }

    public List<SubCategory> getSubCategories(String categoryId) {
        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + ", SC." + BjcpContract.COLUMN_ORDER + ", SC." + BjcpContract.COLUMN_CAT_ID + " FROM " + BjcpContract.TABLE_SUB_CATEGORY + " SC WHERE SC." + BjcpContract.COLUMN_CAT_ID + " = " + categoryId + " ORDER BY SC." + BjcpContract.COLUMN_ORDER;

        return getSubCategoriesByQuery(query);
    }

    public List<SubCategory> getOnTapSubCategories() {
        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + ", SC." + BjcpContract.COLUMN_ORDER + ", SC." + BjcpContract.COLUMN_CAT_ID + " FROM " + BjcpContract.TABLE_SUB_CATEGORY + " SC JOIN " + BjcpContract.TABLE_CATEGORY + " C ON C." + BjcpContract.COLUMN_ID + " = SC." + BjcpContract.COLUMN_CAT_ID + " WHERE C." + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND C." + BjcpContract.COLUMN_REVISION + " = " + Category.CURRENT_REVISION + " AND SC." + BjcpContract.COLUMN_TAPPED + " = 1 ORDER BY SC." + BjcpContract.COLUMN_SUB_CAT;

        return getSubCategoriesByQuery(query);
    }

    public SubCategory getSubCategory(String subCategoryId) {
        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + ", SC." + BjcpContract.COLUMN_ORDER + ", SC." + BjcpContract.COLUMN_CAT_ID + " FROM " + BjcpContract.TABLE_SUB_CATEGORY + " SC WHERE SC." + BjcpContract.COLUMN_ID + " = " + subCategoryId;

        return getSubCategoriesByQuery(query).get(0);
    }

    public List<SubCategory> getSubCategoriesByIds(List<Long> ids) {
        List<SubCategory> subCategories = new ArrayList<>();

        if (null != ids && !ids.isEmpty()) {
            subCategories = getSubCategoriesByQuery(getSubCategoriesByIdsQuery(ids));
        }

        return subCategories;
    }

    private String getSubCategoriesByIdsQuery(List<Long> ids) {
        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + ", SC." + BjcpContract.COLUMN_ORDER + ", SC." + BjcpContract.COLUMN_CAT_ID + " FROM " + BjcpContract.TABLE_SUB_CATEGORY + " SC WHERE SC." + BjcpContract.COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";

        return query;
    }

    private List<SubCategory> getSubCategoriesByQuery(String query) {
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        SubCategory subCategory;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                subCategory = new SubCategory(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_ID)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_SUB_CAT)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME)));
                subCategory.set_orderNumber(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ORDER)));
                subCategory.set_categoryId(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_CAT_ID)));

                subCategories.add(subCategory);
            }
            c.moveToNext();
        }

        c.close();

        return subCategories;
    }

    public List<Section> getSubCategorySections(String subCategoryId) {
        List<Section> sections = new ArrayList<Section>();
        Section section;

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM " + BjcpContract.TABLE_SECTION + " S " + "WHERE S." + BjcpContract.COLUMN_SUB_CAT_ID + " = " + subCategoryId + " ORDER BY S." + BjcpContract.COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            section = new Section();
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                section.set_id(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                section.set_header(c.getString(c.getColumnIndex(BjcpContract.COLUMN_HEADER)));
                section.set_body(c.getString(c.getColumnIndex(BjcpContract.COLUMN_BODY)));
            }
            c.moveToNext();
            sections.add(section);
        }

        c.close();

        return sections;
    }

    public VitalStatistics getVitalStatistics(String subCategoryId) {
        VitalStatistics vitalStatistics = null;

        String query = "SELECT V." + BjcpContract.COLUMN_ID + ", V." + BjcpContract.COLUMN_OG_START + ", V." + BjcpContract.COLUMN_OG_END + ", V." + BjcpContract.COLUMN_FG_START + ", V." + BjcpContract.COLUMN_FG_END + ", V." + BjcpContract.COLUMN_IBU_START + ", V." + BjcpContract.COLUMN_IBU_END + ", V." + BjcpContract.COLUMN_SRM_START + ", V." + BjcpContract.COLUMN_SRM_END + ", V." + BjcpContract.COLUMN_ABV_START + ", V." + BjcpContract.COLUMN_ABV_END + " FROM " + BjcpContract.TABLE_VITALS + " V WHERE V." + BjcpContract.COLUMN_SUB_CAT_ID + " = " + subCategoryId;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        // Should only be one.
        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                vitalStatistics = new VitalStatistics();
                vitalStatistics.set_id(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                vitalStatistics.set_ogStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_OG_START)));
                vitalStatistics.set_ogEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_OG_END)));
                vitalStatistics.set_fgStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_FG_START)));
                vitalStatistics.set_fgEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_FG_END)));
                vitalStatistics.set_ibuStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_IBU_START)));
                vitalStatistics.set_ibuEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_IBU_END)));
                vitalStatistics.set_srmStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_SRM_START)));
                vitalStatistics.set_srmEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_SRM_END)));
                vitalStatistics.set_abvStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_ABV_START)));
                vitalStatistics.set_abvEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_ABV_END)));
            }
            c.moveToNext();
        }

        c.close();

        return vitalStatistics;
    }

    public void updateSubCategoryUntapped(SubCategory subCategory) {
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        subCategories.add(subCategory);

        updateSubCategoriesUntapped(subCategories);
    }

    public void updateSubCategoriesUntapped(List<SubCategory> subCategories) {
        ContentValues cv = new ContentValues();

        for (SubCategory subCategory : subCategories) {
            cv.put(BjcpContract.COLUMN_TAPPED, (subCategory.is_tapped() ? 1 : 0));
            getWrite().update(BjcpContract.TABLE_SUB_CATEGORY, cv, (BjcpContract.COLUMN_ID + " = " + subCategory.get_id()), null);
        }
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();
        SearchResult searchResult;
        String query = "SELECT " + BjcpContract.COLUMN_RESULT_ID + ", " + BjcpContract.COLUMN_TABLE_NAME + " FROM " + BjcpContract.TABLE_FTS_SEARCH + " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND " + BjcpContract.COLUMN_REVISION + " = " + Category.CURRENT_REVISION + " AND " + BjcpContract.COLUMN_BODY + " MATCH '" + keyword + "*' ORDER BY " + BjcpContract.COLUMN_RESULT_ID;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_RESULT_ID)) != null) {
                searchResult = new SearchResult();
                searchResult.set_resultId(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_RESULT_ID)));
                searchResult.set_TableName(c.getString(c.getColumnIndex(BjcpContract.COLUMN_TABLE_NAME)));
                searchResult.set_query(keyword);

                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }
}
