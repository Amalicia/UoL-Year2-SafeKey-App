package com.example.android.safekey;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.safekey.SQLiteDB.LockListContract;
import com.example.android.safekey.SQLiteDB.LockListDBHelper;
import com.example.android.safekey.SQLiteDB.TestData;

public class SafeKey_Main_Page extends AppCompatActivity implements LockAdapter.PlusClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LockAdapter mAdapter;
    private RecyclerView mLockList;

    //SQLite Database
    private SQLiteDatabase mDB;


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

        //Create DB helper
        LockListDBHelper dbHelper = new LockListDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        //Insert fake data from TestData
        TestData.insertFakeData(mDB);
        //Get the data
        Cursor cursor = getLockData();

        //Create adapter based on locks in database
        mAdapter = new LockAdapter(cursor, this);
        mLockList.setAdapter(mAdapter);
    }
  
    //Menu button action on click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TESTING USE
    @Override
    public void onPlusClicked(int clickedItemIndex) {
        //Test code to check listener works as expected

        //String toastMessage = "Plus clicked";
        //mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        //mToast.show();

        //Assign context
        Context ctx = SafeKey_Main_Page.this;
        //Assign destination class
        Class destinationActivity = Add_New_Lock_Activity.class;
        //Create intent
        Intent intent = new Intent(ctx, destinationActivity);
        startActivity(intent);
    }

    private Cursor getLockData() {
        String[] columns = new String[1];
        columns[0] = LockListContract.LockListEntry.COLUMN_LOCK_NAME;
        return mDB.query(
                LockListContract.LockListEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
    }
}
