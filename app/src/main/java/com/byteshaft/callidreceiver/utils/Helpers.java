package com.byteshaft.callidreceiver.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Helpers {

    private static final String FILE_HEADER = "id,name,call_start,call_end";

    public static String decodeIncomingSmsText(Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages;
        String messageText = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                byte[] data = messages[i].getUserData();
                for (byte aData : data) {
                    messageText += Character.toString((char) aData);
                }
            }
        }

        return messageText;
    }

    public static String getContactName(Context context, String number) {

        String name = null;

        // define the columns I want the query to return
        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = context.getContentResolver().query(
                contactUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            } else {
                // Contact not saved.
                name = "N/A";
            }
            cursor.close();
        }
        return name;
    }

    public static void writeMessageContentToFile(String fileName, String body, boolean append) {
        File outputFile = getOutputFile(fileName);
        if (append) {
            System.out.println("Appending " + body);
        } else {
            System.out.println("Writing " + body);
        }
        writeToFile(outputFile.getPath(), body, append);
    }

    private static void writeToFile(String filePath, String body, boolean append) {
        try {
            FileWriter writer = new FileWriter(filePath, append);
            writer.append(body);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getOutputFile(String fileName) {
        File outputDirectory = getDefaultDirectory();
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        return new File(outputDirectory, fileName);
    }

    public static File getDefaultDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "CallerID");
    }

    public static String getTimeStamp(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(new Date(time));
    }

    public static File ensureOutputFileExists() throws IOException {
        File file = getOutputFile("logs.csv");
        if (!file.exists()) {
            FileWriter writer = new FileWriter(file.getPath());
            writer.append(FILE_HEADER);
            writer.append("\n");
            writer.flush();
            writer.close();
        }
        return file;
    }

    public static void writeToCSV(String filePathToRead) {
        try {
            File file = ensureOutputFileExists();
            FileWriter writer = new FileWriter(file.getPath(), true);
            List<String> lines = Files.readLines(new File(filePathToRead), Charsets.UTF_8);
            writer.append(lines.get(0).trim() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
