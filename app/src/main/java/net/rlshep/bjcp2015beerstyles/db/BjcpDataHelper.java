package net.rlshep.bjcp2015beerstyles.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.rlshep.bjcp2015beerstyles.domain.Category;
import net.rlshep.bjcp2015beerstyles.domain.Section;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;
import net.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BjcpDataHelper extends SQLiteOpenHelper{
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BjcpBeerStyles.db";
    protected Context dbContext;

    public BjcpDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);

        this.dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> queries = new ArrayList<String>();
        queries.add("CREATE TABLE " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  BjcpContract.COLUMN_CAT + " TEXT, " + BjcpContract.COLUMN_NAME + " TEXT, " + BjcpContract.COLUMN_REVISION  + " NUMBER, " + BjcpContract.COLUMN_LANG + " TEXT," + BjcpContract.COLUMN_ORDER + " INTEGER" + " );");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_SUB_CAT + " TEXT, " + BjcpContract.COLUMN_NAME + " TEXT, " + BjcpContract.COLUMN_TAPPED  + " BOOLEAN, FOREIGN KEY(" +  BjcpContract.COLUMN_CAT_ID + ") REFERENCES " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_SECTION + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_SUB_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_HEADER + " TEXT, " + BjcpContract.COLUMN_BODY + " TEXT, " + BjcpContract.COLUMN_ORDER + " INTEGER, FOREIGN KEY(" + BjcpContract.COLUMN_CAT_ID + ") REFERENCES " + BjcpContract.TABLE_CATEGORY + "(" + BjcpContract.COLUMN_ID + "), FOREIGN KEY(" + BjcpContract.COLUMN_SUB_CAT_ID + " ) REFERENCES " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");
        queries.add("CREATE TABLE " + BjcpContract.TABLE_VITALS + "(" + BjcpContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BjcpContract.COLUMN_SUB_CAT_ID + " INTEGER, " + BjcpContract.COLUMN_OG_START + " TEXT, " + BjcpContract.COLUMN_OG_END + " TEXT, " + BjcpContract.COLUMN_FG_START + " TEXT, " + BjcpContract.COLUMN_FG_END + " TEXT, " + BjcpContract.COLUMN_IBU_START + " TEXT, " + BjcpContract.COLUMN_IBU_END + " TEXT, " + BjcpContract.COLUMN_SRM_START + " TEXT, " + BjcpContract.COLUMN_SRM_END + " TEXT, " + BjcpContract.COLUMN_ABV_START + " TEXT, " + BjcpContract.COLUMN_ABV_END + " TEXT, FOREIGN KEY(" + BjcpContract.COLUMN_SUB_CAT_ID + " ) REFERENCES " + BjcpContract.TABLE_SUB_CATEGORY + "(" + BjcpContract.COLUMN_ID + "));");

        for (String query : queries) {
            db.execSQL(query);
        }

        // Load database from xml file.
        //TODO: FIGURE OUT HOW TO HANDLE THESE EXCEPTIONS.
        try {
            addCategories(new LoadDataFromXML().loadXmlFromFile(dbContext));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_SECTION);
        db.execSQL("DROP TABLE IF EXISTS " + BjcpContract.TABLE_VITALS);

        onCreate(db);
        db.close();
    }

    private void addCategories(List<Category> categories) {
        for (Category category : categories) {
            addCategory(category);
        }
    }

    private void addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_CAT, category.get_category());
        values.put(BjcpContract.COLUMN_NAME, category.get_name());
        values.put(BjcpContract.COLUMN_REVISION, category.get_revision());
        values.put(BjcpContract.COLUMN_LANG, category.get_language());
        values.put(BjcpContract.COLUMN_ORDER, category.get_orderNumber());

        //Write category to database.
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(BjcpContract.TABLE_CATEGORY, null, values);

        //Insert sub-tables if available.
        for (SubCategory subCategory : category.get_subCategories()) {
            subCategory.set_categoryId(id);
            addSubCategory(subCategory);
        }

        for (Section section : category.get_sections()) {
            section.set_categoryId(id);
            addSection(section);
        }
    }

    private void addSubCategory(SubCategory subCategory) {
        ContentValues values = new ContentValues();
        values.put(BjcpContract.COLUMN_SUB_CAT, subCategory.get_subCategory());
        values.put(BjcpContract.COLUMN_NAME, subCategory.get_name());
        values.put(BjcpContract.COLUMN_CAT_ID, subCategory.get_categoryId());
        values.put(BjcpContract.COLUMN_TAPPED, subCategory.is_tapped());

        //Write category to database.
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(BjcpContract.TABLE_SUB_CATEGORY, null, values);

        //Insert sub-tables if available.
        if (null != subCategory.get_vitalStatistics()) {
            subCategory.get_vitalStatistics().set_subCategoryId(id);
            addVitalStatistics(subCategory.get_vitalStatistics());
        }

        for (Section section : subCategory.get_sections()) {
            section.set_subCategoryId(id);
            addSection(section);
        }
    }

    private void addVitalStatistics(VitalStatistics vitalStatistics) {
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
        SQLiteDatabase db = getWritableDatabase();
        db.insert(BjcpContract.TABLE_VITALS, null, values);
    }


    private void addSection(Section section) {
        ContentValues values = new ContentValues();

        // Only want to tie to Category if SubCategory is missing.
        if (section.get_subCategoryId() > 0) {
            values.put(BjcpContract.COLUMN_SUB_CAT_ID, section.get_subCategoryId());
        } else {
            values.put(BjcpContract.COLUMN_CAT_ID, section.get_categoryId());
        }

        values.put(BjcpContract.COLUMN_HEADER, section.get_header());
        values.put(BjcpContract.COLUMN_BODY, section.get_body());
        values.put(BjcpContract.COLUMN_ORDER, section.get_orderNumber());

        //Write category to database.
        SQLiteDatabase db = getWritableDatabase();
        db.insert(BjcpContract.TABLE_SECTION, null, values);
    }

    /*********************************** Create Tables End ************************************/

    /*********************************** Start business queries ******************************/

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        SQLiteDatabase db = getWritableDatabase();


        String query = "SELECT " + BjcpContract.COLUMN_ID + ", " + BjcpContract.COLUMN_CAT + ", " + BjcpContract.COLUMN_NAME + " FROM "
                + BjcpContract.TABLE_CATEGORY + " WHERE " + BjcpContract.COLUMN_LANG + " = '" + Category.LANG_ENGLISH + "' AND " + BjcpContract.COLUMN_REVISION +
                " = " + Category.CURRENT_REVISION + " ORDER BY " + BjcpContract.COLUMN_ORDER;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                categories.add(new Category(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_ID)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_CAT)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME))));
             }
            c.moveToNext();
        }

        db.close();

        return categories;
    }

    public List<Section> getCategorySections(String categoryId) {
        List<Section> sections = new ArrayList<Section>();
        Section section;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM "+ BjcpContract.TABLE_CATEGORY + " C JOIN " + BjcpContract.TABLE_SECTION
                + " S ON S." + BjcpContract.COLUMN_CAT_ID + "= C." + BjcpContract.COLUMN_ID + " WHERE C." + BjcpContract.COLUMN_ID + " = '" + categoryId
                + "' ORDER BY S." + BjcpContract.COLUMN_ORDER;

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

        db.close();

        return sections;
    }

    public List<SubCategory> getSubCategories(String categoryId) {
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT SC." + BjcpContract.COLUMN_ID + ", SC." + BjcpContract.COLUMN_SUB_CAT + ", SC." + BjcpContract.COLUMN_NAME + " FROM "
                + BjcpContract.TABLE_CATEGORY + " C JOIN " + BjcpContract.TABLE_SUB_CATEGORY + " SC ON SC." + BjcpContract.COLUMN_CAT_ID
                + "= C." + BjcpContract.COLUMN_ID + " WHERE C." + BjcpContract.COLUMN_ID + " = '" + categoryId + "' ORDER BY SC." + BjcpContract.COLUMN_SUB_CAT ;

        //Cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(BjcpContract.COLUMN_ID)) != null) {
                subCategories.add(new SubCategory(c.getLong(c.getColumnIndex(BjcpContract.COLUMN_ID)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_SUB_CAT)), c.getString(c.getColumnIndex(BjcpContract.COLUMN_NAME))));
            }
            c.moveToNext();
        }

        db.close();

        return subCategories;
    }

    public List<Section> getSubCategorySections(String subCategoryId) {
        List<Section> sections = new ArrayList<Section>();
        Section section;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT S." + BjcpContract.COLUMN_ID + ", S." + BjcpContract.COLUMN_BODY + ", S." + BjcpContract.COLUMN_HEADER + " FROM "+ BjcpContract.TABLE_SUB_CATEGORY + " SC JOIN " + BjcpContract.TABLE_SECTION
                + " S ON S." + BjcpContract.COLUMN_SUB_CAT_ID + "= SC." + BjcpContract.COLUMN_ID + " WHERE SC." + BjcpContract.COLUMN_ID + " = '" + subCategoryId
                + "' ORDER BY S." + BjcpContract.COLUMN_ORDER;

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

        db.close();

        return sections;
    }
}
