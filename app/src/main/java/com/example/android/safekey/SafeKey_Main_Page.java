package com.example.android.safekey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SafeKey_Main_Page extends AppCompatActivity {

    private Button doorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_key__main__page);

        doorButton = (Button) findViewById(R.id.doorButton);
        doorButton.setText(R.string.unlock);
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
