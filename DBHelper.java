package com.soi.modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static final String DB_NAME = "letfriends.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static DBHelper mDBConnection;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = "/data/data/"
                + context.getApplicationContext().getPackageName()
                + "/databases/";
    }

    /**
     * getting Instance
     * @param context
     * @return DBHelper
     */
    public static synchronized void initInstance(Context context) {
        if (mDBConnection == null) {
            mDBConnection = new DBHelper(context);
        }
    }
    public static synchronized DBHelper getDBHelperInstance() {
        return mDBConnection;
    }
    /**
     * Creates an empty database on the system and rewrites it with your own database.
     **/
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.i("dbexists", "Db is already created");
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            Log.i("dbexists", "Db doesn't exist yet");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open("letfriends.db");
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Open the database
     * @throws android.database.SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Close the database if exist
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * Call on creating data base for example for creating tables at run time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    /**
     * can used for drop tables then call onCreate(db) function to create tables again - upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // ----------------------- CRUD Functions ------------------------------

    /**
     * This function used to select the records from DB.
     * @param tableName
     * @param tableColumns
     * @param whereClase
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return A Cursor object, which is positioned before the first entry.
     */
    public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
                                      String whereClase, String whereArgs[], String groupBy,
                                      String having, String orderBy) {
        return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
                groupBy, having, orderBy);
    }

    /**
     * select records from db and return in list
     * @param tableName
     * @param tableColumns
     * @param whereClase
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return ArrayList<ArrayList<String>>
     */
    public ArrayList<ArrayList<String>> selectRecordsFromDBList(String tableName, String[] tableColumns,
                                                                String whereClase, String whereArgs[], String groupBy,
                                                                String having, String orderBy) {

        ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
        ArrayList<String> list = null;
        Cursor cursor = myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
                groupBy, having, orderBy);
        if (cursor.moveToFirst()) {
            do {
                list = new ArrayList<String>();
                for(int i=0; i<cursor.getColumnCount(); i++){
                    list.add( cursor.getString(i) );
                }
                retList.add(list);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return retList;

    }

    /**
     * This function used to insert the Record in DB.
     * @param tableName
     * @param nullColumnHack
     * @param initialValues
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertRecordsInDB(String tableName, String nullColumnHack,
                                  ContentValues initialValues) {
        return myDataBase.insert(tableName, nullColumnHack, initialValues);
    }

    /**
     * This function used to update the Record in DB.
     * @param tableName
     * @param initialValues
     * @param whereClause
     * @param whereArgs
     * @return true / false on updating one or more records
     */
    public boolean updateRecordInDB(String tableName,
                                    ContentValues initialValues, String whereClause, String whereArgs[]) {
        return myDataBase.update(tableName, initialValues, whereClause,
                whereArgs) > 0;
    }

    /**
     * This function used to update the Record in DB.
     * @param tableName
     * @param initialValues
     * @param whereClause
     * @param whereArgs
     * @return 0 in case of failure otherwise return no of row(s) are updated
     */
    public int updateRecordsInDB(String tableName,
                                 ContentValues initialValues, String whereClause, String whereArgs[]) {
        return myDataBase.update(tableName, initialValues, whereClause, whereArgs);
    }

    /**
     * This function used to delete the Record in DB.
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return 0 in case of failure otherwise return no of row(s) are deleted.
     */
    public int deleteRecordInDB(String tableName, String whereClause,
                                String[] whereArgs) {
        return myDataBase.delete(tableName, whereClause, whereArgs);
    }

    // --------------------- Select Raw Query Functions ---------------------

    /**
     * apply raw Query
     * @param query
     * @param selectionArgs
     * @return Cursor
     */
    public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
        return myDataBase.rawQuery(query, selectionArgs);
    }

    /**
     * apply raw query and return result in list
     * @param query
     * @param selectionArgs
     * @return ArrayList<ArrayList<String>>
     */
    public ArrayList<ArrayList<String>> selectRecordsFromDBList(String query, String[] selectionArgs) {
        ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
        ArrayList<String> list = null;
        Cursor cursor = myDataBase.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                list = new ArrayList<String>();
                for(int i=0; i<cursor.getColumnCount(); i++){
                    list.add( cursor.getString(i) );
                }
                retList.add(list);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return retList;
    }

}
