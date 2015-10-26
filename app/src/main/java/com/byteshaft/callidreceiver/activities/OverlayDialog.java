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
        setTitle("Save contact ?");
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

                //*
                // Add listener so your activity gets called back upon completion of action,
// in this case with ability to get handle to newly added contact
//                myActivity.addActivityListener(someActivityListener);
//
//                Intent intent = new Intent(Intent.ACTION_INSERT);
//                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

// Just two examples of information you can send to pre-fill out data for the
// user.  See android.provider.ContactsContract.Intents.Insert for the complete
//// list.
//                intent.putExtra(ContactsContract.Intents.Insert.NAME, "some Contact Name");
//                intent.putExtra(ContactsContract.Intents.Insert.PHONE, "some Phone Number");

// Send with it a unique request code, so when you get called back, you can
// check to make sure it is from the intent you launched (ideally should be
// some public static final so receiver can check against it)
//                int PICK_CONTACT = 100;
//                myActivity.startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}