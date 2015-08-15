package io.github.rlshep.bjcp2015beerstyles.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.SearchResult;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class BjcpDataHelper extends SQLiteOpenHelper {
    private static final String TAG = "LoadDatabase";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BjcpBeerStyles.db";

    private Context dbContext;
    private SQLiteDatabase db;
    private static BjcpDataHelper instance;

    private BjcpDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.dbContext = context;
    }

    public static synchronized BjcpDataHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new BjcpDataHelper(context);
        }

        return instance;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    private void createDataBase() throws IOException {
        if (!checkDataBase()) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.db = this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = dbContext.getFilesDir().getPath() + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = dbContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = dbContext.getFilesDir().getPath() + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        new CreateBjcpDatabase().onUpgrade(db, this.dbContext, 1, 1);
    }

    /* Start business queries *********************************************************************/

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
            categories = getCategories(getCategoriesByIdsQuery(ids).toString());
        }

        return categories;
    }

    private String getCategoriesByIdsQuery(List<Long> ids) {
        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CAT + ", "  + BjcpContract.COLUMN_NAME + ", "
                + BjcpContract.COLUMN_ORDER + " FROM " + BjcpContract.TABLE_CATEGORY + " WHERE " + BjcpContract.COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";

        return query.toString();
    }

    private String getIdsQuery(List<Long> ids) {
        StringBuilder query = new StringBuilder();

        for(int i=0; i < ids.size(); i++) {
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
        getDb();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
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
        getDb();

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM " + BjcpContract.TABLE_SECTION + " S WHERE S." + BjcpContract.COLUMN_CAT_ID + " = " + categoryId + " ORDER BY S." + BjcpContract.COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
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
            subCategories = getSubCategoriesByQuery(getSubCategoriesByIdsQuery(ids).toString());
        }

        return subCategories;
    }

    private String getSubCategoriesByIdsQuery(List<Long> ids) {
        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + ", SC." + BjcpContract.COLUMN_ORDER + ", SC." + BjcpContract.COLUMN_CAT_ID + " FROM " + BjcpContract.TABLE_SUB_CATEGORY + " SC WHERE SC." + BjcpContract.COLUMN_ID + " IN(" + getIdsQuery(ids) + ")";

        return query.toString();
    }

    private List<SubCategory> getSubCategoriesByQuery(String query) {
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        SubCategory subCategory;
        getDb();

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
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
        getDb();

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM " + BjcpContract.TABLE_SECTION + " S " + "WHERE S." + BjcpContract.COLUMN_SUB_CAT_ID + " = " + subCategoryId + " ORDER BY S." + BjcpContract.COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
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
        getDb();

        String query = "SELECT V." + BjcpContract.COLUMN_ID + ", V." + BjcpContract.COLUMN_OG_START + ", V." + BjcpContract.COLUMN_OG_END + ", V." + BjcpContract.COLUMN_FG_START + ", V." + BjcpContract.COLUMN_FG_END + ", V." + BjcpContract.COLUMN_IBU_START + ", V." + BjcpContract.COLUMN_IBU_END + ", V." + BjcpContract.COLUMN_SRM_START + ", V." + BjcpContract.COLUMN_SRM_END + ", V." + BjcpContract.COLUMN_ABV_START + ", V." + BjcpContract.COLUMN_ABV_END + " FROM " + BjcpContract.TABLE_VITALS + " V WHERE V." + BjcpContract.COLUMN_SUB_CAT_ID + " = " + subCategoryId;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);

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

        getWriteableDb();

        for (SubCategory subCategory : subCategories) {
            cv.put(BjcpContract.COLUMN_TAPPED, (subCategory.is_tapped() ? 1 : 0));
            db.update(BjcpContract.TABLE_SUB_CATEGORY, cv, (BjcpContract.COLUMN_ID + " = " + subCategory.get_id()), null);
        }
    }

    //TODO: IMPLEMENT
    public boolean search(String query) {
        // DELETE ALL OLD
        // QUERY AND ADD TO SEARCH RESULT TABLE
        return true;
    }

    public List<SearchResult> getSearchResults() {
        List<SearchResult> searchResults = new ArrayList<>();
        SearchResult searchResult;
        getDb();

        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_RESULT_ID + ", " + BjcpContract.COLUMN_TABLE_NAME + ", " + BjcpContract.COLUMN_QUERY + " FROM " + BjcpContract.TABLE_SEARCH_RESULTS + " WHERE 1 = 1 ORDER BY " + BjcpContract.COLUMN_RESULT_ID;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                searchResult = new SearchResult();
                searchResult.set_id(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_ID)));
                searchResult.set_resultId(c.getInt(c.getColumnIndex(BjcpContract.COLUMN_RESULT_ID)));
                searchResult.set_TableName(c.getString(c.getColumnIndex(BjcpContract.COLUMN_TABLE_NAME)));
                searchResult.set_query(c.getString(c.getColumnIndex(BjcpContract.COLUMN_QUERY)));

                searchResults.add(searchResult);
            }
        }

        c.close();

        return searchResults;
    }

    public void deleteSearchResults() {
        getWriteableDb();
        db.delete(BjcpContract.TABLE_SEARCH_RESULTS, "1 = 1", null);
    }

    private void getDb() {
        if (null == this.db || !this.db.isOpen()) {
            openReadDataBase();
        }
    }

    private void getWriteableDb() {
        if (isDbOpenAndReadOnly()) {
            db.close();
        }

        if (null == this.db || !this.db.isOpen()) {
            openWriteDataBase();
        }
    }

    private boolean isDbOpenAndReadOnly() {
        return null != this.db && this.db.isOpen() && db.isReadOnly();
    }


    private void openReadDataBase() {
        try {
            createDataBase();
            this.db = SQLiteDatabase.openDatabase(dbContext.getFilesDir().getPath() + DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void openWriteDataBase() {
        try {
            createDataBase();
            this.db = SQLiteDatabase.openDatabase(dbContext.getFilesDir().getPath() + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
