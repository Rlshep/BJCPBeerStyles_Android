package io.github.rlshep.bjcp2015beerstyles.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class BaseDataHelper extends SQLiteOpenHelper {
    private static final String TAG = "LoadDatabase";
    private static final int DATABASE_VERSION = 1;
    private Activity dbContext;
    private SQLiteDatabase db;

    protected BaseDataHelper(Activity context) {
        super(context, BjcpConstants.DATABASE_NAME, null, DATABASE_VERSION);

        this.dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }

        super.close();
    }

    protected SQLiteDatabase getRead() {
        if (null == this.db || !this.db.isOpen()) {
            openReadDataBase();
        }

        return db;
    }

    protected SQLiteDatabase getWrite() {
        if (isDbOpenAndReadOnly()) {
            db.close();
        }

        if (null == this.db || !this.db.isOpen()) {
            openWriteDataBase();
        }

        return db;
    }

    private void openReadDataBase() {
        try {
            createDataBase();
            this.db = SQLiteDatabase.openDatabase(dbContext.getFilesDir().getPath() + "/../databases/" + BjcpConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (IOException e) {
            new ExceptionHandler(this.dbContext).uncaughtException(e);
        }
    }

    private void openWriteDataBase() {
        try {
            createDataBase();
            this.db = SQLiteDatabase.openDatabase(dbContext.getFilesDir().getPath() + "/../databases/" + BjcpConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            new ExceptionHandler(this.dbContext).uncaughtException(e);
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    private void createDataBase() throws IOException {
        if (!checkDataBase()) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.db = this.getReadableDatabase();
            copyDataBase();
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
            String myPath = dbContext.getFilesDir().getPath() + "/../databases/" + BjcpConstants.DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
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
        InputStream myInput = dbContext.getAssets().open(BjcpConstants.DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = dbContext.getFilesDir().getPath() + "/../databases/" + BjcpConstants.DATABASE_NAME;

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

    private boolean isDbOpenAndReadOnly() {
        return null != this.db && this.db.isOpen() && db.isReadOnly();
    }
}
