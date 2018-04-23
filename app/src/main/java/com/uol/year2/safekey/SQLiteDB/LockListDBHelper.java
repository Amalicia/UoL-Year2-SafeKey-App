package com.uol.year2.safekey.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lottie on 19/03/2018.
 */

public class LockListDBHelper extends SQLiteOpenHelper {
    //Set file name
    private static final String DATABASE_NAME = "lock_list.db";
    //Set DB version number - must be incremented if any changes are made
    //TODO IF ANYTHING IS CHANGED - UPDATE VERSION NUMBER
    private static final int VERSION_NO = 6;

    //Create DB helper
    public  LockListDBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NO);
    }

    //SQL Create table statement
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCK_TABLE = "CREATE TABLE " +
                LockListContract.LockListEntry.TABLE_NAME + " (" +
                LockListContract.LockListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LockListContract.LockListEntry.COLUMN_LOCK_NAME + " TEXT NOT NULL, " +
                LockListContract.LockListEntry.COLUMN_LOCK_PW + " TEXT NOT NULL, " +
                LockListContract.LockListEntry.COLUMN_IP_ADDRESS + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_LOCK_TABLE);
    }

    //Upgrade function
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LockListContract.LockListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
