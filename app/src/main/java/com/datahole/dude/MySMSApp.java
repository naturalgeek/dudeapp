package com.datahole.dude;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.Toast;

public class MySMSApp extends BroadcastReceiver {

    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        GPSTracker gps = new GPSTracker(context);

        if (intent.getAction().equals(ACTION)){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage message : messages){

                    String strMessageFrom = message.getDisplayOriginatingAddress();
                    String strMessageBody = message.getDisplayMessageBody();

                    if (strMessageBody.equals("getLocation")) {
                        double longitude = gps.getLongitude();
                        double latitude = gps.getLatitude();
                        sendSMS(strMessageFrom, "Device Position: https://www.google.de/maps/@" + latitude + "," + longitude + ",10z");
                    }
                    if (strMessageBody.equals("lock")) {
                        double longitude = gps.getLongitude();
                        double latitude = gps.getLatitude();

                        SharedPreferences lockSettings = context.getSharedPreferences("Lock", 0);
                        SharedPreferences.Editor editor = lockSettings.edit();

                        editor.putString("lockLong",Double.toString(longitude));
                        editor.putString("lockLat",Double.toString(latitude));
                        editor.putBoolean("locked",true);
                        editor.commit();

                        sendSMS(strMessageFrom, "Position Locked to:" + lockSettings.getString("lockLong","FAILURE") + "," + lockSettings.getString("lockLat","FAILURE"));
                    }
                    if (strMessageBody.equals("unlock")) {

                        SharedPreferences lockSettings = context.getSharedPreferences("Lock", 0);
                        SharedPreferences.Editor editor = lockSettings.edit();

                        editor.putBoolean("locked", false);
                        editor.commit();

                        sendSMS(strMessageFrom, "Position Unlocked!");
                    }

                }
            }
        }
    }



    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}