package com.byteshaft.callidreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.byteshaft.callidreceiver.utils.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

public class BinarySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = Helpers.decodeIncomingSmsText(intent);
        System.out.println(" Test test test " + message);
        try {
            JSONObject object = new JSONObject(message);
            String state = (String) object.get("state");

            if (state.equals("new")) {
                String time = (String) object.get("time");
                String name = Helpers.getContactName(context, (String) object.get("number"));
                String dateTime = DateFormat.getInstance().format(new Date(Long.valueOf(time)));
                String data = String.format("Contact ID: \"%s\"\n Call time: \"%s\"", name, dateTime);
                Helpers.writeMessageContentToFile((String) object.get("time"), data);

            } else if (state.equals("old")) {
                Writer writer = null;
                String rootDir = Environment.getExternalStorageDirectory() + "/CallerID/";
                String fileName = (String) object.get("time");
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(rootDir + fileName), "utf-8"));
                    writer.write("Something ");
                } catch (IOException ex) {
                    // report
                } finally {
                    try {writer.close();} catch (Exception ex) {/*ignore*/}
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
