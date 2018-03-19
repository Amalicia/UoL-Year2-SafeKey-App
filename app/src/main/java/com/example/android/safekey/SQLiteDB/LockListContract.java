package com.example.android.safekey.SQLiteDB;

import android.provider.BaseColumns;

/**
 * Created by Lottie on 19/03/2018.
 */

public class LockListContract {
    public static final class LockListEntry implements BaseColumns {
        public static final String TABLE_NAME = "lock_list";
        public static final String COLUMN_LOCK_NAME = "lock_name";
        public static final String COLUMN_LOCK_PW = "lock_password";
    }
}
