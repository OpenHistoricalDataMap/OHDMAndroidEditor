package com.example.justin.editor.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.justin.editor.maps.Map;

/**
 * Created by Justin on 15.11.2017.
 */

public class Gps extends AppCompatActivity implements LocationListener {
    private Map map;
    private boolean TESTMODE = true;
    private double lastLong=0,lastLat=0,dif=0.00001;

    public Gps(Map map){
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
            if(!TESTMODE) {
                Toast.makeText(map, "Lon:   " + location.getLongitude() + " Lat:    " + location.getLatitude(), Toast.LENGTH_LONG).show();
                map.setGPS(location.getLongitude(), location.getLatitude());
            }
            else{

                if(lastLong-location.getLongitude() > dif || lastLat - location.getLatitude() > dif || location.getLongitude()-lastLong > dif || location.getLatitude() - lastLat > dif) {
                    map.setGPS(location.getLongitude(), location.getLatitude());
                    lastLat = location.getLatitude();
                    lastLong = location.getLongitude();
                }
            }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
