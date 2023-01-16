package io.github.rlshep.bjcp2015beerstyles.db;

import android.content.ContentValues;
import android.database.Cursor;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistic;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BA_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.DATABASE_VERSION;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_BODY;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_BOOKMARKED;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_CATEGORY_CODE;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_CAT_ID;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_HEADER;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_HIGH;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_ID;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_LANG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_LEFT;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_LOW;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_NAME;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_NOTES;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_ORDER;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_PARENT_ID;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_RESULT_ID;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_REVISION;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_RIGHT;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_TABLE_NAME;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_TAG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.COLUMN_TYPE;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_CATEGORY;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_FTS_SEARCH;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_SECTION;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_SYNONYMS;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_TAG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.TABLE_VITALS;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_ABV;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_IBU;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_SRM;

public class BjcpDataHelper extends BaseDataHelper {
    private static BjcpDataHelper instance;
    private static final String NULL = "null";
    private PreferencesHelper ph;

    private static final String CATEGORY_SELECT = "SELECT " + COLUMN_ID + ", " + COLUMN_CATEGORY_CODE + ", " + COLUMN_NAME + ", " + COLUMN_ORDER + ", " + COLUMN_PARENT_ID + "," + COLUMN_BOOKMARKED + "," + COLUMN_REVISION + "," + COLUMN_LANG + ", " + COLUMN_PARENT_ID + " FROM " + TABLE_CATEGORY + " C ";

    protected BjcpDataHelper(BjcpActivity activity) {
        super(activity);
        ph = new PreferencesHelper(activity);
    }


    // Use the application context, which will ensure that you don't accidentally leak an Activity's context. See this article for more information: http://bit.ly/6LRzfx
    public static synchronized BjcpDataHelper getInstance(BjcpActivity activity) {
        if (null == instance) {
            instance = new BjcpDataHelper(activity);
        } else {
            instance.setPreferencesHelper(new PreferencesHelper(activity));
        }

        return instance;
    }

    public PreferencesHelper getPreferencesHelper() {
        return ph;
    }

    public void setPreferencesHelper(PreferencesHelper preferencesHelper) {
        this.ph = preferencesHelper;
    }

    private String getCategoryTopWhere() {
        return " WHERE " + COLUMN_REVISION + " = '" + ph.getStyleType() + "' AND " + COLUMN_LANG + " = '" + ph.getLanguage() + "' ";
    }

    public Category getCategory(String categoryId) {
        String query = CATEGORY_SELECT + " WHERE " + COLUMN_ID + " = " + categoryId;
        return getCategories(query).get(0);
    }

    public List<Category> getAllCategories() {
        String query = CATEGORY_SELECT + getCategoryTopWhere() + " AND " + COLUMN_PARENT_ID + " IS NULL ORDER BY " + getOrderType();
        return getCategories(query);
    }

    public List<Category> getCategoriesByIds(List<Long> ids) {
        List<Category> categories = new ArrayList<>();
        String sql;

        if (null != ids && !ids.isEmpty()) {
            sql = CATEGORY_SELECT + " WHERE " + COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";
            categories = getCategories(sql);
        }

        return categories;
    }


    private List<Category> getChildCategories(long parentId) {
        String query = CATEGORY_SELECT + getCategoryTopWhere() + " AND " + COLUMN_PARENT_ID + " = " + parentId + " ORDER BY " + getOrderType();
        return getCategories(query);
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
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_ID)) != null) {
                category = new Category(ph.getStyleType());
                category.setId(c.getLong(c.getColumnIndexOrThrow(COLUMN_ID)));
                category.setName(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)));
                category.setOrderNumber(c.getInt(c.getColumnIndexOrThrow(COLUMN_ORDER)));
                category.setParentId(c.getLong(c.getColumnIndexOrThrow(COLUMN_PARENT_ID)));
                category.setBookmarked(c.getInt(c.getColumnIndexOrThrow(COLUMN_BOOKMARKED)) > 0);
                category.setRevision(c.getString(c.getColumnIndexOrThrow(COLUMN_REVISION)));
                category.setLanguage((c.getString(c.getColumnIndexOrThrow((COLUMN_LANG)))));
                category.setChildCategories(getChildCategories(c.getLong(c.getColumnIndexOrThrow(COLUMN_ID))));

                String categoryCode = c.getString(c.getColumnIndexOrThrow(COLUMN_CATEGORY_CODE));
                if (!StringUtils.isEmpty(categoryCode) && !NULL.equals(categoryCode)) {
                    category.setCategoryCode(categoryCode);
                }

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

        String query = "SELECT S." + COLUMN_ID + ", S." + COLUMN_BODY + ", S." + COLUMN_HEADER + " FROM " + TABLE_SECTION + " S WHERE S." + COLUMN_CAT_ID + " = " + categoryId + " ORDER BY S." + COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            section = new Section();
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_ID)) != null) {
                section.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
                section.setBody(c.getString(c.getColumnIndexOrThrow(COLUMN_BODY)));
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
        String query = CATEGORY_SELECT + " WHERE " + COLUMN_PARENT_ID + " = " + parentId + " ORDER BY C." + getOrderType();;
        return getCategories(query);
    }

    public List<Category> getBookmarkedCategories() {
        String query = CATEGORY_SELECT + getCategoryTopWhere() + " AND " + COLUMN_BOOKMARKED + " = 1 AND "  + COLUMN_REVISION + " = '" + ph.getStyleType() + "' AND " + COLUMN_LANG + " = '" + ph.getLanguage() + "' ORDER BY " + COLUMN_CATEGORY_CODE;

        return getCategories(query);
    }

    public List<VitalStatistic> getVitalStatistic(String categoryId) {
        List<VitalStatistic> vitalStatistics = new ArrayList<>();
        VitalStatistic vitalStatistic;

        String query = "SELECT V." + COLUMN_ID + ", V." + COLUMN_LOW + ", V." + COLUMN_HIGH + ", " + COLUMN_HEADER + ", " + COLUMN_TYPE + ", " + COLUMN_NOTES + " FROM " + TABLE_VITALS + " V WHERE V." + COLUMN_CAT_ID + " = " + categoryId + " ORDER BY " + COLUMN_TYPE + ", " + COLUMN_HEADER;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            vitalStatistic = new VitalStatistic();

            if (c.getString(c.getColumnIndexOrThrow(COLUMN_ID)) != null) {
                vitalStatistic.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
                vitalStatistic.setLow(c.getDouble(c.getColumnIndexOrThrow(COLUMN_LOW)));
                vitalStatistic.setHigh(c.getDouble(c.getColumnIndexOrThrow(COLUMN_HIGH)));
                vitalStatistic.setHeader(c.getString(c.getColumnIndexOrThrow(COLUMN_HEADER)));
                vitalStatistic.setType(c.getString(c.getColumnIndexOrThrow(COLUMN_TYPE)));
                vitalStatistic.setNotes(c.getString(c.getColumnIndexOrThrow(COLUMN_NOTES)));
            }
            c.moveToNext();
            vitalStatistics.add(vitalStatistic);
        }

        c.close();

        return vitalStatistics;
    }

    public void updateCategoryBookmarked(Category category) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);

        updateCategoriesBookmarked(categories);
    }

    public void updateCategoriesBookmarked(List<Category> categories) {
        ContentValues cv = new ContentValues();

        for (Category category : categories) {
            cv.put(COLUMN_BOOKMARKED, (category.isBookmarked() ? 1 : 0));
            getWrite().update(TABLE_CATEGORY, cv, (COLUMN_ID + " = " + category.getId()), null);
        }
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> searchResults = new ArrayList<>();
        List<String> keywords = searchSynonyms(keyword);

        if (keywords.isEmpty()) {
            keywords.add(keyword);
        }

        for (String k : keywords) {
            searchResults.addAll(searchStyles(k));
        }

        return searchResults;
    }

    public List<String> searchSynonyms(String keyword) {
        ArrayList<String> searchResults = new ArrayList<>();
        String searchResult;
        String query = "SELECT " + COLUMN_RIGHT + " FROM " + TABLE_SYNONYMS + " WHERE  " + COLUMN_LANG + " = '" + ph.getLanguage() + "' AND " + COLUMN_REVISION + " = '" + ph.getStyleType() + "' AND UPPER(" + COLUMN_LEFT + ") = UPPER('" + keyword + "')";

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_RIGHT)) != null) {
                searchResult = c.getString(c.getColumnIndexOrThrow(COLUMN_RIGHT));
                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }


    private List<SearchResult> searchStyles(String keyword) {
        SearchResult searchResult;
        List<SearchResult> searchResults = new ArrayList<>();
        String query = "SELECT " + COLUMN_RESULT_ID + ", " + COLUMN_TABLE_NAME + " FROM " + TABLE_FTS_SEARCH + " WHERE " + COLUMN_LANG + " = '" + ph.getLanguage() + "' AND " + COLUMN_REVISION + " = '" + ph.getStyleType() + "' AND " + COLUMN_BODY + " MATCH '\"" + keyword + "\"*' ORDER BY " + COLUMN_RESULT_ID;

        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_RESULT_ID)) != null) {
                searchResult = new SearchResult();
                searchResult.setResultId(c.getInt(c.getColumnIndexOrThrow(COLUMN_RESULT_ID)));
                searchResult.setTableName(c.getString(c.getColumnIndexOrThrow(COLUMN_TABLE_NAME)));
                searchResult.setQuery(keyword);

                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }

    public List<String> getAllSynonyms() {
        ArrayList<String> synonyms = new ArrayList<>();
        final String query = "SELECT DISTINCT " + COLUMN_LEFT + " FROM " + TABLE_SYNONYMS + " WHERE " + COLUMN_LANG + " = '" + ph.getLanguage() + "' AND " + COLUMN_REVISION + " = '" + ph.getStyleType() + "'";

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_LEFT)) != null) {
                synonyms.add(c.getString(c.getColumnIndexOrThrow(COLUMN_LEFT)));
            }
        }

        c.close();

        return synonyms;
    }

    public List<String> getAllCategoryNames() {
        ArrayList<String> names = new ArrayList<>();
        String language = ph.getLanguage();

        String query = "SELECT DISTINCT " + COLUMN_NAME + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_LANG + " = '" + language + "' AND " + COLUMN_REVISION + " = '" + ph.getStyleType() + "' ORDER BY " + COLUMN_NAME;

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)) != null) {
                names.add(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)));
            }
            c.moveToNext();
        }

        c.close();

        return names;
    }

    public boolean isCorrectDatabaseVersion() {
        return (DATABASE_VERSION == getRead().getVersion());
    }

    public String getSearchVitalStatisticsQuery(List<VitalStatistic> vitalStatistics, String keyword) {
        String ibuLow = "";
        String ibuHigh = "";
        String srmLow = "";
        String srmHigh = "";
        String abvLow = "";
        String abvHigh = "";

        for (VitalStatistic vitalStatistic : vitalStatistics) {
            if (XML_IBU.equals(vitalStatistic.getType())) {
                ibuLow = new Double(vitalStatistic.getLow()).toString();
                ibuHigh = new Double(vitalStatistic.getHigh()).toString();
            } else if (XML_SRM.equals(vitalStatistic.getType())) {
                srmLow = new Double(vitalStatistic.getLow()).toString();
                srmHigh = new Double(vitalStatistic.getHigh()).toString();
            } else if (XML_ABV.equals(vitalStatistic.getType())) {
                abvLow = new Double(vitalStatistic.getLow()).toString();
                abvHigh = new Double(vitalStatistic.getHigh()).toString();
            }
        }

        String query = "SELECT IBU." + COLUMN_CAT_ID +
                " FROM " + TABLE_CATEGORY +
                " C JOIN " + TABLE_VITALS + " IBU ON C." + COLUMN_ID + " = IBU." + COLUMN_CAT_ID +
                " AND IBU." + COLUMN_TYPE + " = '" + XML_IBU +
                "' JOIN " + TABLE_VITALS + " SRM ON C." + COLUMN_ID + " = SRM." + COLUMN_CAT_ID +
                " AND SRM." + COLUMN_TYPE + " = '" + XML_SRM +
                "' JOIN " + TABLE_VITALS + " ABV ON C." + COLUMN_ID + " = ABV." + COLUMN_CAT_ID +
                " AND ABV." + COLUMN_TYPE + " = '" + XML_ABV + "'";
                if (!StringUtils.isEmpty(keyword)) {
                    query = query + " JOIN " + TABLE_FTS_SEARCH + " TFS ON TFS." +
                        COLUMN_RESULT_ID + " = C." + COLUMN_ID + " AND TFS." + COLUMN_TABLE_NAME + " = 'CATEGORY' AND TFS." +
                        COLUMN_BODY + " MATCH '\"" + keyword + "\"*'";
                }
                query = query + " WHERE C." + COLUMN_LANG + " = '" + ph.getLanguage() + "' AND C." + COLUMN_REVISION + " = '" + ph.getStyleType() +
                "' AND IBU." + COLUMN_LOW + ">= " + ibuLow + " AND IBU." + COLUMN_HIGH + " <= " + ibuHigh +
                " AND SRM." + COLUMN_LOW + " >= " + srmLow + " AND SRM." + COLUMN_HIGH + " <= " + srmHigh +
                " AND ABV." + COLUMN_LOW + " >= " + abvLow + " AND ABV." + COLUMN_HIGH + " <= " + abvHigh +
                " ORDER BY C." + getOrderType();

        return query;
    }

    public List<SearchResult> searchVitals(String query) {
        SearchResult searchResult;
        List<SearchResult> searchResults = new ArrayList<>();

        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_CAT_ID)) != null) {
                searchResult = new SearchResult();
                searchResult.setResultId(c.getInt(c.getColumnIndexOrThrow(COLUMN_CAT_ID)));
                searchResult.setTableName(TABLE_CATEGORY);

                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }

    public List<String> getAllTags() {
        ArrayList<String> tags = new ArrayList<>();
        final String query = "SELECT DISTINCT T." + COLUMN_TAG + " FROM " + TABLE_TAG + " T JOIN " + TABLE_CATEGORY + " C ON C." + COLUMN_ID + " = T." + COLUMN_CAT_ID + " WHERE C." + COLUMN_LANG + " = '" + ph.getLanguage() + "'" + " AND C." + COLUMN_REVISION + " = '" + ph.getStyleType() + "'";

        //Cursor point to a location in your results
        Cursor c = getRead().rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndexOrThrow(COLUMN_TAG)) != null) {
                tags.add(c.getString(c.getColumnIndexOrThrow(COLUMN_TAG)));
            }
        }

        c.close();

        return tags;
    }

    private String getOrderType() {
        String order = COLUMN_ORDER;

        if (ph.getStyleType().equals(BA_2021)) {
            order = COLUMN_NAME;
        }

        return order;
    }
}