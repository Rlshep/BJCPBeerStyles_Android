package io.github.rlshep.bjcp2015beerstyles.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class BjcpDataHelper extends BaseDataHelper {
    private static BjcpDataHelper instance;

    private static final String CATEGORY_SELECT = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CATEGORY_CODE + ", " + BjcpContract.COLUMN_NAME + ", " + BjcpContract.COLUMN_ORDER + ", " + BjcpContract.COLUMN_PARENT_ID + "," + BjcpContract.COLUMN_BOOKMARKED + "," + BjcpContract.COLUMN_REVISION + "," + BjcpContract.COLUMN_LANG + " FROM " + BjcpContract.TABLE_CATEGORY + " C ";
    private static final String CATEGORY_TOP_WHERE = " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND " + BjcpContract.COLUMN_REVISION + " = " + Category.CURRENT_REVISION;

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
        String query = CATEGORY_SELECT + " WHERE " + BjcpContract.COLUMN_ID + " = " + categoryId;
        return getCategories(query).get(0);
    }

    public List<Category> getAllCategories() {
        String query = CATEGORY_SELECT + CATEGORY_TOP_WHERE + " AND " + BjcpContract.COLUMN_PARENT_ID + " IS NULL ORDER BY " + BjcpContract.COLUMN_ORDER;
        return getCategories(query);
    }

    public List<Category> getCategoriesByIds(List<Long> ids) {
        List<Category> categories = new ArrayList<>();
        String sql;

        if (null != ids && !ids.isEmpty()) {
            sql = CATEGORY_SELECT + " WHERE " + BjcpContract.COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";
            categories = getCategories(sql);
        }

        return categories;
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
                category = new Category(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_ID)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_CATEGORY_CODE)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME)));
                category.setOrderNumber(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ORDER)));
                category.setParentId(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_PARENT_ID)));
                category.setBookmarked(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_BOOKMARKED)) > 0);
                category.setRevision(c.getDouble(c.getColumnIndex(BjcpContract.COLUMN_REVISION)));
                category.setLanguage((c.getString(c.getColumnIndex((BjcpContract.COLUMN_LANG)))));
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
                section.setId(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                section.setHeader(c.getString(c.getColumnIndex(BjcpContract.COLUMN_HEADER)));
                section.setBody(c.getString(c.getColumnIndex(BjcpContract.COLUMN_BODY)));
            }
            c.moveToNext();
            sections.add(section);
        }

        c.close();

        return sections;
    }

    public List<Category> getCategoriesByParent(long parentId) {
        return getCategoriesByParent((Long.valueOf(parentId)).toString());
    }

    public List<Category> getCategoriesByParent(String parentId) {
        String query = CATEGORY_SELECT + " WHERE " + BjcpContract.COLUMN_PARENT_ID + " = " + parentId;
        return getCategories(query);
    }

    public List<Category> getBookmarkedCategories() {
        String query = CATEGORY_SELECT + CATEGORY_TOP_WHERE + " AND " + BjcpContract.COLUMN_BOOKMARKED + " = 1 ORDER BY " + BjcpContract.COLUMN_CATEGORY_CODE;

        return getCategories(query);
    }

    public List<VitalStatistics> getVitalStatistics(String categoryId) {
        List<VitalStatistics> vitalStatisticses = new ArrayList<>();
        VitalStatistics vitalStatistics;

        String query = "SELECT V." + BjcpContract.COLUMN_ID + ", V." + BjcpContract.COLUMN_OG_START + ", V." + BjcpContract.COLUMN_OG_END + ", V." + BjcpContract.COLUMN_FG_START + ", V." + BjcpContract.COLUMN_FG_END + ", V." + BjcpContract.COLUMN_IBU_START + ", V." + BjcpContract.COLUMN_IBU_END + ", V." + BjcpContract.COLUMN_SRM_START + ", V." + BjcpContract.COLUMN_SRM_END + ", V." + BjcpContract.COLUMN_ABV_START + ", V." + BjcpContract.COLUMN_ABV_END + ", " + BjcpContract.COLUMN_HEADER + " FROM " + BjcpContract.TABLE_VITALS + " V WHERE V." + BjcpContract.COLUMN_CAT_ID + " = " + categoryId;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            vitalStatistics = new VitalStatistics();

            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                vitalStatistics.setId(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                vitalStatistics.setOgStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_OG_START)));
                vitalStatistics.setOgEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_OG_END)));
                vitalStatistics.setFgStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_FG_START)));
                vitalStatistics.setFgEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_FG_END)));
                vitalStatistics.setIbuStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_IBU_START)));
                vitalStatistics.setIbuEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_IBU_END)));
                vitalStatistics.setSrmStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_SRM_START)));
                vitalStatistics.setSrmEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_SRM_END)));
                vitalStatistics.setAbvStart(c.getString(c.getColumnIndex(BjcpContract.COLUMN_ABV_START)));
                vitalStatistics.setAbvEnd(c.getString(c.getColumnIndex(BjcpContract.COLUMN_ABV_END)));
                vitalStatistics.setHeader(c.getString(c.getColumnIndex(BjcpContract.COLUMN_HEADER)));
            }
            c.moveToNext();
            vitalStatisticses.add(vitalStatistics);
        }

        c.close();

        return vitalStatisticses;
    }

    public void updateCategoryBookmarked(Category category) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);

        updateCategoriesBookmarked(categories);
    }

    public void updateCategoriesBookmarked(List<Category> categories) {
        ContentValues cv = new ContentValues();

        for (Category category : categories) {
            cv.put(BjcpContract.COLUMN_BOOKMARKED, (category.isBookmarked() ? 1 : 0));
            getWrite().update(BjcpContract.TABLE_CATEGORY, cv, (BjcpContract.COLUMN_ID + " = " + category.getId()), null);
        }
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();
        List<String> keywords = searchSynonyms(keyword);

        for (String k : keywords) {
            searchResults.addAll(searchStyles(k));
        }

        return searchResults;
    }

    public List<String> searchSynonyms(String keyword) {
        ArrayList<String> searchResults = new ArrayList<>();
        String searchResult;
        String query = "SELECT " + BjcpContract.COLUMN_RIGHT + " FROM " + BjcpContract.TABLE_SYNONYMS + " WHERE  " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND UPPER(" + BjcpContract.COLUMN_LEFT + ") = UPPER('" + keyword + "')";

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_RIGHT)) != null) {
                searchResult = c.getString(c.getColumnIndex(BjcpContract.COLUMN_RIGHT));
                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }

    private List<SearchResult> searchStyles(String keyword) {
        SearchResult searchResult;
        List<SearchResult> searchResults = new ArrayList<>();
        String query = "SELECT " + BjcpContract.COLUMN_RESULT_ID + ", " + BjcpContract.COLUMN_TABLE_NAME + " FROM " + BjcpContract.TABLE_FTS_SEARCH + " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND " + BjcpContract.COLUMN_REVISION + " = " + Category.CURRENT_REVISION + " AND " + BjcpContract.COLUMN_BODY + " MATCH '" + keyword + "*' ORDER BY " + BjcpContract.COLUMN_RESULT_ID;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_RESULT_ID)) != null) {
                searchResult = new SearchResult();
                searchResult.setResultId(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_RESULT_ID)));
                searchResult.setTableName(c.getString(c.getColumnIndex(BjcpContract.COLUMN_TABLE_NAME)));
                searchResult.setQuery(keyword);

                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }

    public List<String> getAllSynonyms() {
        ArrayList<String> synonyms = new ArrayList<>();
        final String query = "SELECT " + BjcpContract.COLUMN_LEFT + " FROM " + BjcpContract.TABLE_SYNONYMS + " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "'";

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_LEFT)) != null) {
                synonyms.add(c.getString(c.getColumnIndex(BjcpContract.COLUMN_LEFT)));
            }
        }

        c.close();

        return synonyms;
    }

    public List<String> getAllCategoryNames() {
        ArrayList<String> names = new ArrayList<>();
        final String query = "SELECT " + BjcpContract.COLUMN_NAME + " FROM " + BjcpContract.TABLE_CATEGORY + " ORDER BY "+ BjcpContract.COLUMN_NAME;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME)) != null) {
                names.add(c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME)));
            }
            c.moveToNext();
        }

        c.close();

        return names;
    }
}
