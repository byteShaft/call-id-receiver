package com.byteshaft.callidreceiver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.byteshaft.callidreceiver.R;

public class OverlayDialog extends Activity implements View.OnClickListener {

    EditText message;
    Button saveButton;
    Button cancelButton;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovelay_dialog);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        saveButton = (Button) findViewById(R.id.save_button);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        message = (EditText) findViewById(R.id.message);
        text = getIntent().getExtras().getString("message", null);
        message.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                finish();
                break;
            case R.id.save_button:
                Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                addContactIntent.putExtra(Contacts.Intents.Insert.PHONE, text); // an example, there is other data available
                startActivity(addContactIntent);
        }
    }
}