package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class MainActivity extends Activity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    Boolean got_location;
    protected boolean gps_enabled, network_enabled;
    String str_longitude, str_latitude;
    String what_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLat = findViewById(R.id.textview);
        got_location = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
//        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        str_latitude = String.valueOf(location.getLatitude());
        str_longitude = String.valueOf(location.getLongitude());
        got_location = true;
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void gutter_click(View view) {

        what_clicked = "Gutter";
        push_data();
    }

    private void push_data() {
        if (got_location)
        {
            String ts = String.valueOf(Calendar.getInstance().getTime());
            Log.d("TAG", "gutter_click: Time: " + ts);
            Log.d("TAG", "gutter_click: Longitude: " + str_longitude);
            Log.d("TAG", "gutter_click: Latitude: " + str_latitude);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(what_clicked);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Time", ts);
            hashMap.put("Longitude", str_longitude);
            hashMap.put("Latitude", str_latitude);

            myRef.push().setValue(hashMap);
            Log.d("TAG", "Data Sent");
        }
        else
        {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();
        }
    }

    public void light_click(View view) {
        what_clicked = "Street-Light";
        push_data();
    }

    public void bus_click(View view) {
        what_clicked = "Bus-Stop";
        push_data();
    }
}