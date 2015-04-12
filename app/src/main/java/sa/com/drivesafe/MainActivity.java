package sa.com.drivesafe;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener(){
            double prevLat = 0, prevLong = 0, prevTime = 0, timer = 0;
            public void onLocationChanged(Location loc){
                double param1 = loc.getLatitude() - prevLat;
                double param2 = loc.getLongitude() - prevLong;
                timer = System.currentTimeMillis() - prevTime;
                loc.setSpeed((float)((Math.sqrt((param1*param1) + (param2*param2)) / timer)));
                prevLat = loc.getLatitude();
                prevLong = loc.getLongitude();
                prevTime = System.currentTimeMillis();
                loc.getLatitude();
                System.out.println(loc.getSpeed());
                Toast.makeText(MainActivity.this, "Current speed: " + loc.getSpeed(), Toast.LENGTH_SHORT).show();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }

        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

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

}
