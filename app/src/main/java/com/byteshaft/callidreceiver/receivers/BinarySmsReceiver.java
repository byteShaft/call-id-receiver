package com.byteshaft.callidreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.callidreceiver.activities.OverlayDialog;
import com.byteshaft.callidreceiver.utils.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class BinarySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = Helpers.decodeIncomingSmsText(intent);
        try {
            JSONObject object = new JSONObject(message);
            String state = (String) object.get("state");

            if (state.equals("new")) {
                System.out.println("new " + message);
                String time = (String) object.get("time");
                String name = Helpers.getContactName(context, (String) object.get("number"));
                String dateTime = Helpers.getTimeStamp(Long.valueOf(time));
                String data = String.format("Contact ID: \"%s\"\nCall time: \"%s\"", name, dateTime);
                Helpers.writeMessageContentToFile((String) object.get("time"), data, false);
                showDialog(context, name);

            } else if (state.equals("old")) {
                System.out.println("old " + message);
                String time = (String) object.get("dcTime");
                String dateTime = Helpers.getTimeStamp(Long.valueOf(time));
                String data = String.format("\nEnd time: %s", dateTime);
                Helpers.writeMessageContentToFile((String) object.get("time"), data, true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(Context context, String message) {
        Intent intent = new Intent(context, OverlayDialog.class);
        intent.putExtra("message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
