package com.uol.year2.safekey;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uol.year2.safekey.SQLiteDB.LockListContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Add_New_Lock_Activity extends AppCompatActivity {

    private EditText mIpText;
    private Button mTestConnect;
    private Button mTestFunction;
    private Button mStaticConnect;
    private Button mStaticFunction;
    private Toast mToast;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__lock_);

        mTestConnect = (Button) findViewById(R.id.btn_test_connect);
        mTestFunction = (Button) findViewById(R.id.btn_test_function);
        mStaticConnect = (Button) findViewById(R.id.btn_static_test);
        mStaticConnect = (Button) findViewById(R.id.btn_static_function);

        mIpText = (EditText) findViewById(R.id.et_ip);

        ctx = this;

        //Create click event to test Wifi connectivity
        mTestConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundConnect().doInBackground("");
            }
        });

        mTestFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundConnect().doInBackground("lock");
            }
        });

        mStaticConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StaticConnect().doInBackground("");
            }
        });

        mStaticConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StaticConnect().doInBackground("lock");
            }
        });
    }

    public void onClickAddLock(View view) {
        //Get user input from edit text
        //Lock name
        String name = ((EditText) findViewById(R.id.et_lock_name)).getText().toString();
        if (name.length() == 0) return;
        //Lock password
        String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
        if (password.length() == 0) return;
        //Lock ip
        String ip = ((EditText) findViewById(R.id.et_ip)).getText().toString();
        if (ip.length() == 0) return;

        //Add data to Content Values
        ContentValues cv = new ContentValues();
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_NAME, name);
        cv.put(LockListContract.LockListEntry.COLUMN_LOCK_PW, password);
        cv.put(LockListContract.LockListEntry.COLUMN_IP_ADDRESS, ip);

        //Insert new lock via content resolver
        Uri uri = getContentResolver().insert(LockListContract.LockListEntry.CONTENT_URI, cv);

        finish();
    }

    private class BackgroundConnect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://" + mIpText.getText().toString() + "/?" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    result.append("\n");
                }

                in.close();
                connection.disconnect();

                //Messages to say if connected or not
                String connectMessage = "We are connected!";
                mToast = Toast.makeText(ctx, connectMessage, Toast.LENGTH_LONG);
                mToast.show();

                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String connectFailedMessage = "Nope, no connection :(";
            mToast = Toast.makeText(ctx, connectFailedMessage, Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }
    }

    private class StaticConnect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://192.168.0.29/?" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    result.append("\n");
                }

                in.close();
                connection.disconnect();

                //Messages to say if connected or not
                String connectMessage = "We are connected!";
                mToast = Toast.makeText(ctx, connectMessage, Toast.LENGTH_LONG);
                mToast.show();

                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String connectFailedMessage = "Nope, no connection :(";
            mToast = Toast.makeText(ctx, connectFailedMessage, Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }
    }
}
