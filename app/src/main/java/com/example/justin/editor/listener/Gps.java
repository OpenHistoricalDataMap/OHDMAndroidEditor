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

    public Gps(Map map){
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {

            Toast.makeText(map, "Lon:   " + location.getLongitude() + " Lat:    " + location.getLatitude(), Toast.LENGTH_LONG).show();
            map.setGPS(location.getLongitude(), location.getLatitude());

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
