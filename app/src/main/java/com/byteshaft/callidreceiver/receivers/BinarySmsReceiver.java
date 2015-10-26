package com.byteshaft.callidreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.callidreceiver.activities.OverlayDialog;
import com.byteshaft.callidreceiver.utils.Helpers;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BinarySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = Helpers.decodeIncomingSmsText(intent);
        try {
            JSONObject object = new JSONObject(message);
            String state = (String) object.get("state");
            String startTime = (String) object.get("time");
            String tempFilePath = Helpers.getDefaultDirectory().getPath() + File.separator + startTime;
            File tempFile = new File(tempFilePath);

            if (state.equals("new")) {
                String number = (String) object.get("number");
                String name = Helpers.getContactName(context, number);
                String dateTime = Helpers.getTimeStamp(Long.valueOf(startTime));
                String data = String.format("%s,%s,%s,", number, name, dateTime);

                String endTimeInFile = null;
                if (tempFile.exists()) {
                    try {
                        List<String> lines = Files.readLines(tempFile, Charsets.UTF_8);
                        endTimeInFile = lines.get(0).trim();
                        String realData = data+endTimeInFile;
                        Helpers.writeMessageContentToFile((String) object.get("time"), realData, false);
                        Helpers.writeToCSV(tempFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Helpers.writeMessageContentToFile((String) object.get("time"), data, true);
                    if (name.equals("N/A")) {
                        showDialog(context, number);
                    }
                }
            } else if (state.equals("old")) {
                String time = (String) object.get("dcTime");
                String endTime = Helpers.getTimeStamp(Long.valueOf(time));
                Helpers.writeMessageContentToFile(startTime, endTime, true);
                if (tempFile.exists()) {
                    Helpers.writeToCSV(tempFilePath);
                }
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
