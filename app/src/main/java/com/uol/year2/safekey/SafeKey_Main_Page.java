package com.uol.year2.safekey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.uol.year2.safekey.SQLiteDB.LockListContract;

public class SafeKey_Main_Page extends AppCompatActivity implements LockAdapter.PlusClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LockAdapter mAdapter;
    private RecyclerView mLockList;

    //Content Provider
    private static final int LOCK_LOADER_ID = 0;
    private static final String TAG = SafeKey_Main_Page.class.getSimpleName();

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


        //Recycler view setup
        mLockList = (RecyclerView) findViewById(R.id.rv_locks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLockList.setLayoutManager(layoutManager);

        //Create adapter based on locks in database
        mAdapter = new LockAdapter(this);
        mLockList.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.itemView.getTag() == null) return;
                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);

                Uri uri = LockListContract.LockListEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                Log.d(TAG, "onSwiped: " + uri.toString());
                getContentResolver().delete(uri, null, null);
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
        //Create intent
        Intent intent = new Intent(ctx, destinationActivity);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mLockData = null;

            @Override
            protected void onStartLoading() {
                if (mLockData != null) deliverResult(mLockData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    String[] Args = {LockListContract.LockListEntry._ID, LockListContract.LockListEntry.COLUMN_LOCK_NAME};

                    Uri uri = LockListContract.LockListEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath("names").build();
                    Log.d(TAG, "loadInBackground: ");
                    return getContentResolver().query(uri,
                            Args,
                            null,
                            null,
                            null);
                } catch (Exception e) {
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
}
