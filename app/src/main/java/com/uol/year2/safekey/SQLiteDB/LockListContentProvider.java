package com.uol.year2.safekey.SQLiteDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Lottie on 25/03/2018.
 */

public class LockListContentProvider extends ContentProvider {

    //Directory and single item constants
    public static final int LOCKS = 100;
    public static final int LOCKS_WITH_NAME = 101;

    private static final UriMatcher sUriMatcher = builUriMatcher();

    //Uri matcher helper function
    public static UriMatcher builUriMatcher() {
        // Create empty matcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS, LOCKS);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS + "/#", LOCKS_WITH_NAME);

        return uriMatcher;
    }


    private LockListDBHelper mLockDBHelper;

    @Override
    public boolean onCreate() {
        //Assign database stuff
        Context ctx = getContext();
        mLockDBHelper = new LockListDBHelper(ctx);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
