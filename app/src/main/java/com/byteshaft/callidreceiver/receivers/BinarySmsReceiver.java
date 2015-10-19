package com.byteshaft.callidreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.callidreceiver.utils.Helpers;

import java.text.DateFormat;

public class BinarySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = Helpers.decodeIncomingSmsText(intent);
        String[] content = message.split(",");
        String number = content[0];
        String name = Helpers.getContactName(context, number);
        String dateTime = DateFormat.getInstance().format(Long.valueOf(content[1]));
        String data = String.format("Contact ID: %s\nCall time: %s", name, dateTime);
        Helpers.writeMessageContentToFile(number, data);
    }
}
