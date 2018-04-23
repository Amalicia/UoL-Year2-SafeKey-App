package com.uol.year2.safekey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.uol.year2.safekey.SQLiteDB.LockListContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SafeKey_Main_Page extends AppCompatActivity implements LockAdapter.PlusClickListener, LockAdapter.SwitchCheckListener, LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LockAdapter mAdapter;
    private RecyclerView mLockList;

    //Content Provider
    private static final int LOCK_LOADER_ID = 0;
    private static final String TAG = SafeKey_Main_Page.class.getSimpleName();

    private Toast mToast;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_key__main__page);

        //Menu button setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Assign Context
        ctx = this;

        //Recycler view setup
        mLockList = (RecyclerView) findViewById(R.id.rv_locks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLockList.setLayoutManager(layoutManager);

        //Create adapter based on locks in database
        mAdapter = new LockAdapter(this, this);
        mLockList.setAdapter(mAdapter);


        //Swipe to delete lock
        //Create touch helper
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //If Recycler view item is moved
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //When swiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Verify lock ID
                Log.d(TAG, "Plus tag is: " + viewHolder.itemView.getTag());
                //If the tag is empty ( "+" symbol)
                if (viewHolder.itemView.getTag() == null) {
                    //Restart loader and refresh to show position
                    getSupportLoaderManager().restartLoader(LOCK_LOADER_ID, null, SafeKey_Main_Page.this);
                    return;
                }

                //Log.d(TAG, "This should not be seen when plus is swiped");

                //Get tag from lock which tells the ID in SQLite DB
                int id = (int) viewHolder.itemView.getTag();
                //Parse to String
                String stringId = Integer.toString(id);

                //Create URI for content provider to use
                Uri uri = LockListContract.LockListEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                //Debug - verify created URI
                Log.d(TAG, "onSwiped: " + uri.toString());
                //Use content provider to remove lock from database
                getContentResolver().delete(uri, null, null);
                //Refresh loader and view update lock list
                getSupportLoaderManager().restartLoader(LOCK_LOADER_ID, null, SafeKey_Main_Page.this);
            }
        }).attachToRecyclerView(mLockList);

        getSupportLoaderManager().initLoader(LOCK_LOADER_ID, null, this);
    }
  
    //Menu button action on click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Restarts loader to allow new data to be added
    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(LOCK_LOADER_ID, null, this);
    }

    @Override
    public void onPlusClicked(int clickedItemIndex) {

        //Assign context
        Context ctx = SafeKey_Main_Page.this;
        //Assign destination class
        Class destinationActivity = Add_New_Lock_Activity.class;
        //Create intent for adding new lock
        Intent intent = new Intent(ctx, destinationActivity);
        //Start activity
        startActivity(intent);
    }

    //When a lock switch is changed
    @Override
    public void onSwitchChanged(int clickedItemIndex, boolean b, Object tag) {
        //Debug stuff
        Log.d("Switch", "Not broken " + clickedItemIndex);
        String pos = tag.toString();
        Log.d("Switch", "Tag: " + pos);

        //Create base URI
        Uri uri = LockListContract.LockListEntry.CONTENT_URI;
        //Append path so content provider can query for IP address using lock ID
        uri = uri.buildUpon().appendPath("ip").appendPath(pos).build();

        //Debug - confirm URI
        Log.d(TAG, "Uri is: " + uri.toString());

        //Cursor to store query results
        Cursor mCusror = getContentResolver().query(uri, null, null, null, null);
        String data = "";

        //If query has data
        if (mCusror.moveToFirst()) {
            //Debug - show queried IP
            Log.d(TAG, "onSwitchChanged: Cursor opened");
            data = mCusror.getString(mCusror.getColumnIndex(LockListContract.LockListEntry.COLUMN_IP_ADDRESS));
            Log.d(TAG, "IP: " + data);
        }

        //If switch checked
        if (b) {
            //Lock door
            new Lock_Connect().execute(data + "/?lock");
        } else {
            new Lock_Connect().execute(data + "/?unlock");
        }
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mLockData = null;

            @Override
            protected void onStartLoading() {
                if (mLockData != null) deliverResult(mLockData);
                else forceLoad();
            }

            //Load in data via content provider
            @Override
            public Cursor loadInBackground() {
                try {
                    //Arguments for SQL query
                    String[] Args = {LockListContract.LockListEntry._ID, LockListContract.LockListEntry.COLUMN_LOCK_NAME};

                    //Create URI for content provider
                    Uri uri = LockListContract.LockListEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath("names").build();
                    //Verify URI
                    Log.d(TAG, "loadInBackground: ");
                    //Content provider SQL Select query
                    return getContentResolver().query(uri,
                            Args,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    //If any error occurs
                    Log.e(TAG, "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mLockData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class Lock_Connect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                //Create URL to connect to Arduino server
                URL url = new URL("http://" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Log.d(TAG, "doInBackground: " + url.toString());

                //Buffering input stream from HTTP connection
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine).append("\n");

                in.close();
                connection.disconnect();

                //Connected toast
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            mToast = Toast.makeText(ctx, "Nope, no connection :(", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
