package io.github.rlshep.bjcp2015beerstyles.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class CreateBjcpDatabase {
    private static final String LOCALE = "en_US";
    private static final String TAG = "CreateDatabase";

    public void onCreate(SQLiteDatabase db, Context dbContext) {
        List<String> queries = new ArrayList<String>();
        queries.add("CREATE TABLE " + BjcpContract.TABLE_META + "(" + BjcpContract.COLUMN_LOCALE + " TEXT DEFAULT '" + LOCALE + "')");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_CAT + " TEXT, " + BjcpContract.COLUMN_NAME + " TEXT, " + BjcpContract.COLUMN_REVISION + " NUMBER, " + BjcpContract.COLUMN_LANG + " TEXT," + BjcpContract.COLUMN_ORDER + " INTEGER" + " );");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_SUB_CAT + " TEXT, " + BjcpContract.COLUMN_NAME + " TEXT, " + BjcpContract.COLUMN_TAPPED + " BOOLEAN, " + BjcpContract.COLUMN_ORDER + " INTEGER," + " FOREIGN KEY(" + BjcpContract.COLUMN_CAT_ID + ") REFERENCES " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_SECTION + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_SUB_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_HEADER + " TEXT, " + BjcpContract.COLUMN_BODY + " TEXT, " + BjcpContract.COLUMN_ORDER + " INTEGER, FOREIGN KEY(" + BjcpContract.COLUMN_CAT_ID + ") REFERENCES " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + "), FOREIGN KEY(" + BjcpContract.COLUMN_SUB_CAT_ID + " ) REFERENCES " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_VITALS + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_SUB_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_OG_START + " TEXT, " + BjcpContract.COLUMN_OG_END + " TEXT, " + BjcpContract.COLUMN_FG_START + " TEXT, " + BjcpContract.COLUMN_FG_END + " TEXT, " + BjcpContract.COLUMN_IBU_START + " TEXT, " + BjcpContract.COLUMN_IBU_END + " TEXT, " + BjcpContract.COLUMN_SRM_START + " TEXT, " + BjcpContract.COLUMN_SRM_END + " TEXT, " + BjcpContract.COLUMN_ABV_START + " TEXT, " + BjcpContract.COLUMN_ABV_END + " TEXT, FOREIGN KEY(" + BjcpContract.COLUMN_SUB_CAT_ID + " ) REFERENCES " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");

        try {
            db.beginTransaction();

            for (String query : queries) {
                db.execSQL(query);
            }

            // Load database from xml file.
            addCategories(db, new LoadDataFromXML().loadXmlFromFile(dbContext));
            addMetaData(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, Context dbContext, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_VITALS);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_META);

        onCreate(db, dbContext);
    }

    private void addCategories(SQLiteDatabase db, List<Category> categories) {
        for (Category category : categories) {
            addCategory(db, category);
        }
    }

    private void addCategory(SQLiteDatabase db, Category category) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_CAT, category.get_category());
        values.put(BjcpContract.COLUMN_NAME, category.get_name());
        values.put(BjcpContract.COLUMN_REVISION, category.get_revision());
        values.put(BjcpContract.COLUMN_LANG, category.get_language());
        values.put(BjcpContract.COLUMN_ORDER, category.get_orderNumber());

        //Write category to database.
        long id = db.insert(BjcpContract.TABLE_CATEGORY, null, values);

        //Insert sub-tables if available.
        for (SubCategory subCategory : category.get_subCategories()) {
            subCategory.set_categoryId(id);
            addSubCategory(db, subCategory);
        }

        for (Section section : category.get_sections()) {
            section.set_categoryId(id);
            addSection(db, section);
        }
    }

    private void addSubCategory(SQLiteDatabase db, SubCategory subCategory) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_SUB_CAT, subCategory.get_subCategory());
        values.put(BjcpContract.COLUMN_NAME, subCategory.get_name());
        values.put(BjcpContract.COLUMN_CAT_ID, subCategory.get_categoryId());
        values.put(BjcpContract.COLUMN_TAPPED, subCategory.is_tapped());
        values.put(BjcpContract.COLUMN_ORDER, subCategory.get_orderNumber());

        //Write category to database.
        long id = db.insert(BjcpContract.TABLE_SUB_CATEGORY, null, values);

        //Insert sub-tables if available.
        if (null != subCategory.get_vitalStatistics()) {
            subCategory.get_vitalStatistics().set_subCategoryId(id);
            addVitalStatistics(db, subCategory.get_vitalStatistics());
        }

        for (Section section : subCategory.get_sections()) {
            section.set_subCategoryId(id);
            addSection(db, section);
        }
    }

    private void addVitalStatistics(SQLiteDatabase db, VitalStatistics vitalStatistics) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_SUB_CAT_ID, vitalStatistics.get_subCategoryId());
        values.put(BjcpContract.COLUMN_OG_START, vitalStatistics.get_ogStart());
        values.put(BjcpContract.COLUMN_OG_END, vitalStatistics.get_ogEnd());
        values.put(BjcpContract.COLUMN_FG_START, vitalStatistics.get_fgStart());
        values.put(BjcpContract.COLUMN_FG_END, vitalStatistics.get_fgEnd());
        values.put(BjcpContract.COLUMN_IBU_START, vitalStatistics.get_ibuStart());
        values.put(BjcpContract.COLUMN_IBU_END, vitalStatistics.get_ibuEnd());
        values.put(BjcpContract.COLUMN_SRM_START, vitalStatistics.get_srmStart());
        values.put(BjcpContract.COLUMN_SRM_END, vitalStatistics.get_srmEnd());
        values.put(BjcpContract.COLUMN_ABV_START, vitalStatistics.get_abvStart());
        values.put(BjcpContract.COLUMN_ABV_END, vitalStatistics.get_abvEnd());

        //Write category to database.
        db.insert(BjcpContract.TABLE_VITALS, null, values);
    }


    private void addSection(SQLiteDatabase db, Section section) {
        ContentValues values = new ContentValues();

        // Only want to tie to Category if SubCategory is missing.
        if (section.get_subCategoryId() > 0) {
            values.put(BjcpContract.COLUMN_SUB_CAT_ID, section.get_subCategoryId());
        }
        else {
            values.put(BjcpContract.COLUMN_CAT_ID, section.get_categoryId());
        }

        values.put(BjcpContract.COLUMN_HEADER, section.get_header());
        values.put(BjcpContract.COLUMN_BODY, section.get_body());
        values.put(BjcpContract.COLUMN_ORDER, section.get_orderNumber());

        //Write category to database.
        db.insert(BjcpContract.TABLE_SECTION, null, values);
    }

    private void addMetaData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_LOCALE, LOCALE);

        db.insert(BjcpContract.TABLE_META, null, values);
    }
}
