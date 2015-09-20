package com.datahole.dude;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
   // EditText txtPhoneNumber = (EditText)findViewById(R.id.txtPhoneNumber);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences lockSettings = getSharedPreferences("Lock", 0);


        EditText ePhoneNUmberInput=(EditText)findViewById(R.id.phoneNumberInput);
        ePhoneNUmberInput.setText(settings.getString("NotifyNr",""));

        TextView eTxtLockLat=(TextView)findViewById(R.id.txtLockLat);
        TextView eTxtLockLong=(TextView)findViewById(R.id.txtLockLong);
        TextView eTxtLockStatus=(TextView)findViewById(R.id.txtLockStatus);

        eTxtLockLat.setText(lockSettings.getString("lockLat","FAIL"));
        eTxtLockLong.setText(lockSettings.getString("lockLong", "FAIL"));
        if (lockSettings.getBoolean("locked",false)){
            eTxtLockStatus.setText("LOCKED!");
        }else{
            eTxtLockStatus.setText("UNLOCKED!");
        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void setNotificationNumber(View v) {


       // GPSTracker gps = new GPSTracker(this);

        //double latitude = gps.getLatitude();

        //Toast toast = Toast.makeText(this, Double.toString(latitude), 1000);
        //toast.show();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();

        EditText ePhoneNUmberInput=(EditText)findViewById(R.id.phoneNumberInput);

     //   Toast.makeText(this,settings.getString("NotifyNr","") ,1000).show();

        editor.putString("NotifyNr", ePhoneNUmberInput.getText().toString());
        editor.commit();

        Toast.makeText(this,"Changed notification number to:\n"+settings.getString("NotifyNr","") ,3000).show();


    }

    public void sendTestLock (View v) {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        sendSMS(settings.getString("NotifyNr",""),"lock");
    }
    public void sendTestGetLocation (View v) {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        sendSMS(settings.getString("NotifyNr",""),"getLocation");
    }




        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}