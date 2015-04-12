package sa.com.drivesafe;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends Activity {
    LocationManager locationManager;
    LocationListener locationListener;
    Boolean checkBoxChecked = false;
    BroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("in on receive at top");
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String messageReceived = "";
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        messageReceived += msgs[i].getMessageBody().toString();
                        messageReceived += "\n";
                    }
                    // Get the Sender Phone Number
                    String senderPhoneNumber = msgs[0].getOriginatingAddress();
                    SmsManager smsManager = SmsManager.getDefault();
                    System.out.println("above send text back ");
                    smsManager.sendTextMessage(senderPhoneNumber, null, "I am currently driving..", null, null);
                }
            }
        };
        this.registerReceiver(smsReceiver, intentFilter);
        startLocationTracking();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        if(view.getId() == R.id.checkBox && checked) {
            checkBoxChecked = true;
            startLocationTracking();
        }
        else {
            checkBoxChecked = false;
            stopLocationTracking();
        }
    }

    @Override
    protected void onStop()
    {
        stopLocationTracking();
        super.onStop();
    }

    public void startLocationTracking(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            double prevLat = 0, prevLong = 0, prevTime = 0, timer = 0;
            public void onLocationChanged(Location loc) {
                double param1 = loc.getLatitude() - prevLat;
                double param2 = loc.getLongitude() - prevLong;
                timer = System.currentTimeMillis() - prevTime;
                loc.setSpeed((float) ((Math.sqrt((param1 * param1) + (param2 * param2)) / timer)));
                //loc.setSpeed(15);
                prevLat = loc.getLatitude();
                prevLong = loc.getLongitude();
                prevTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "Speed: " + loc.getSpeed(), Toast.LENGTH_SHORT).show();
                //insert of speed is greater than selected speed from the UI,
                //and an SMS is received, send an SMS back.
                //if(loc.getSpeed() > thresholdSpeed)

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    public void stopLocationTracking(){
        locationManager.removeUpdates(locationListener);
    }
}
