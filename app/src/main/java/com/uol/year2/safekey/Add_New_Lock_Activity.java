package com.uol.year2.safekey;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Add_New_Lock_Activity extends AppCompatActivity {

    private EditText mIpText;
    private Button mTestConnect;
    private Toast mToast;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__lock_);

        mTestConnect = (Button) findViewById(R.id.btn_test_connect);
        mIpText = (EditText) findViewById(R.id.et_ip);

        ctx = this;

        //Create click event to test Wifi connectivity
        mTestConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundConnect().doInBackground("");
            }
        });
    }

    private class BackgroundConnect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://" + mIpText.getText().toString());
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
