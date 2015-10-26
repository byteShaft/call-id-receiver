package com.byteshaft.callidreceiver.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.byteshaft.callidreceiver.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
