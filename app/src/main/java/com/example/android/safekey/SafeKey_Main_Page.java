package com.example.android.safekey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SafeKey_Main_Page extends AppCompatActivity {

    private Button doorButton;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

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
        
        doorButton = (Button) findViewById(R.id.doorButton);
        doorButton.setText(R.string.unlock);
    }
    
    //Menu button action on click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBtn(View view) {
        //Toast.makeText(this, "Button pressed", Toast.LENGTH_LONG).show();

        String btnText = doorButton.getText().toString();

        if (btnText.equals(getString(R.string.unlock))) {
            doorButton.setText(R.string.lock);
        }
        else if (btnText.equals(getString(R.string.lock))){
            doorButton.setText(R.string.unlock);
        }
        else {
            doorButton.setText("Error");
        }
    }
}
