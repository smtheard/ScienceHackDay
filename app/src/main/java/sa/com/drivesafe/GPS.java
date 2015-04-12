package sa.com.drivesafe;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by USER on 4/12/2015.
 */
public class GPS {
    GPS() {

    }
    private void test(Activity test) {
        LocationManager locationManager = (LocationManager) test.getSystemService(Context.LOCATION_SERVICE);

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
                //Toast.makeText(test.getthis, "Current speed: " + loc.getSpeed(), Toast.LENGTH_SHORT).show();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }

        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
