package com.byteshaft.callidreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.callidreceiver.utils.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;

public class BinarySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = Helpers.decodeIncomingSmsText(intent);
        try {
            JSONObject object = new JSONObject(message);
            String state = (String) object.get("state");
            if (state.equals("new")) {
                String name = Helpers.getContactName(context, (String) object.get("number"));
                String dateTime = DateFormat.getInstance().format(object.get("time"));
                String data = String.format("Contact ID: %s\nCall time: %s", name, dateTime);
                Helpers.writeMessageContentToFile((String) object.get("time"), data);
            } else if (state.equals("old")) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
