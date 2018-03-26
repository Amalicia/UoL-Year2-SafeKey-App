package com.uol.year2.safekey.SQLiteDB;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lottie on 19/03/2018.
 */

public class TestData {
    public static void insertFakeData(SQLiteDatabase db) {
        //Check if DB exists
        if (db == null) return ;

        List<ContentValues> list = new ArrayList<ContentValues>();

        //Create Data and add to List
        ContentValues cv = new ContentValues();
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_NAME, "Front Door");
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_PW, "password");
        cv.put(LockListContract.LockListEntry.COLUMN_IP_ADDRESS, "1");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_NAME, "Back door");
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_PW, "password");
        cv.put(LockListContract.LockListEntry.COLUMN_IP_ADDRESS, "2");
        list.add(cv);

        cv = new ContentValues();
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_NAME, "Steve");
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_PW, "password");
        cv.put(LockListContract.LockListEntry.COLUMN_IP_ADDRESS, "3");
        list.add(cv);

        try
        {
            db.beginTransaction();
            //Clear table
            db.delete(LockListContract.LockListEntry.TABLE_NAME, null, null);
            //Enter data one by one
            for (ContentValues c:list) {
                db.insert(LockListContract.LockListEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //RIP
        } finally {
            db.endTransaction();
        }
    }
}
