package com.example.android.safekey;

import android.content.Context;
import android.content.Intent;
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

public class SafeKey_Main_Page extends AppCompatActivity implements LockAdapter.PlusClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static final int LOCK_LIST_ITEMS= 15;
    private LockAdapter mAdapter;
    private RecyclerView mLockList;

    private Toast mToast;

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

        mLockList.setHasFixedSize(true);
        mAdapter = new LockAdapter(LOCK_LIST_ITEMS, this);
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
}
