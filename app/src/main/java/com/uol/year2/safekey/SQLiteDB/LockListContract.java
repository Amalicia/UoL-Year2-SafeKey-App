package com.uol.year2.safekey.SQLiteDB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Lottie on 19/03/2018.
 */

public class LockListContract {
    //Content provider constants
    public  static final String AUTHORITY = "com.uol.year2.safekey";
    //Base content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_LOCKS = "locks";

    public static final class LockListEntry implements BaseColumns {
        //Create base Uri
        public  static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCKS).build();

        //Database constants
        public static final String TABLE_NAME = "lock_list";
        public static final String COLUMN_LOCK_NAME = "lock_name";
        public static final String COLUMN_LOCK_PW = "lock_password";
    }
}
