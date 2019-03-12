package io.github.rlshep.bjcp2015beerstyles.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

//TODO: FIX
// I/chatty: uid=10424(u0_a424) FinalizerDaemon identical 42 lines
//        W/SQLiteConnectionPool: A SQLiteConnection object for database '+data+user+0+io_github_rlshep_bjcp2015beerstyles+files+__+databases+BjcpBeerStyles_db' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.
public class BaseDataHelper extends SQLiteOpenHelper {
    private static final String DB_PATH = "/../databases/";
    protected BjcpActivity dbContext;
    private SQLiteDatabase db;

    protected BaseDataHelper(BjcpActivity activity) {
        super(activity, BjcpConstants.DATABASE_NAME, null, BjcpConstants.DATABASE_VERSION);

        this.dbContext = activity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }

        super.close();
    }

    protected SQLiteDatabase getRead() {
        if (!isDbOpen()) {
            setOrCreateDatabase(SQLiteDatabase.OPEN_READONLY);
        }

        return this.db;
    }

    protected SQLiteDatabase getWrite() {
        if (isDbReadOnly()) {
            db.close();
        }

        if (!isDbOpen()) {
            setOrCreateDatabase(SQLiteDatabase.OPEN_READWRITE);
        }

        return db;
    }

    private boolean isDbOpen() {
        return null != this.db && this.db.isOpen();
    }


    private boolean isDbReadOnly() {
        return isDbOpen() && db.isReadOnly();
    }

    private boolean isFileDbExists() {
        File file = new File(dbContext.getFilesDir().getPath() + DB_PATH + BjcpConstants.DATABASE_NAME);

        return file.exists();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    private void setOrCreateDatabase(int dbType) {
        this.db = getDb(dbType);

        if (isDatabaseCopyNeeded()) {
            if (this.db != null) {
                this.db.close();    //close to reopen as new db.
            }

            copyDataBase();
            this.db = getDb(dbType);  //reopen new db
        }
    }

    private SQLiteDatabase getDb(int dbType) {
        SQLiteDatabase db = null;

        if (isFileDbExists()) {
            db = SQLiteDatabase.openDatabase(dbContext.getFilesDir().getPath() + DB_PATH + BjcpConstants.DATABASE_NAME, null, dbType);
        }

        return db;
    }

    private boolean isDatabaseCopyNeeded() {
        boolean needCopied = false;

        if (this.db == null || BjcpConstants.DATABASE_VERSION != this.db.getVersion()) {
            needCopied = true;
        }

        return needCopied;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() {
        try {
            //Open your local db as the input stream
            InputStream myInput = dbContext.getAssets().open(BjcpConstants.DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = dbContext.getFilesDir().getPath() + DB_PATH + BjcpConstants.DATABASE_NAME;

            //Close database. Issue with OnePlus devices.
            this.getReadableDatabase().close();

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
        } catch (IOException e) {
            new ExceptionHandler(this.dbContext).uncaughtException(e);
        }
    }
}
