package com.example.android.safekey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SafeKey_Main_Page extends AppCompatActivity {

    private static final int LOCK_LIST_ITEMS= 15;
    private LockAdapter mAdapter;
    private RecyclerView mLockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_key__main__page);

        mLockList = (RecyclerView) findViewById(R.id.rv_locks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLockList.setLayoutManager(layoutManager);

        mLockList.setHasFixedSize(true);
        mAdapter = new LockAdapter(LOCK_LIST_ITEMS);
        mLockList.setAdapter(mAdapter);
    }


}
