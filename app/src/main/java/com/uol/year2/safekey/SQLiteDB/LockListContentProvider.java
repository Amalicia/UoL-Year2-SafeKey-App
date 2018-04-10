package com.uol.year2.safekey.SQLiteDB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Lottie on 25/03/2018.
 */

public class LockListContentProvider extends ContentProvider {

    //Directory and single item constants
    public static final int LOCKS = 100;
    public static final int LOCKS_WITH_ID = 101;
    public static final int NAMES = 110;
    public static final int LOCKS_WITH_IP = 120;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //Uri matcher helper function
    public static UriMatcher buildUriMatcher() {
        // Create empty matcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS, LOCKS);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS + "/#", LOCKS_WITH_ID);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS + "/names", NAMES);
        uriMatcher.addURI(LockListContract.AUTHORITY, LockListContract.PATH_LOCKS + "/ip/#", LOCKS_WITH_IP);

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Get writable db
        final SQLiteDatabase db = mLockDBHelper.getReadableDatabase();

        Log.d("query uri", "uri is " + uri.toString());
        //Get match from uri matcher
        int match = sUriMatcher.match(uri);
        Log.d("query Match code", "Match is: " + match);
        Cursor returnCursor;

        switch (match) {
            case NAMES:
                //Query DB
                returnCursor = db.query(
                        LockListContract.LockListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case LOCKS_WITH_IP:
                String id = uri.getPathSegments().get(1);
                String[] mProjection = new String[] {"ip_address"};
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[] {id};

                returnCursor = db.query(
                        LockListContract.LockListEntry.TABLE_NAME,
                        mProjection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        //Update content resolver and return data
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //Get writable database
        final SQLiteDatabase db = mLockDBHelper.getWritableDatabase();

        //Get match from Uri matcher
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case LOCKS:
                long id = db.insert(LockListContract.LockListEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(LockListContract.LockListEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Notify change
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        //Get database
        final SQLiteDatabase db = mLockDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {
            case LOCKS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(LockListContract.LockListEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
