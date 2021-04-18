package com.example.food_waste_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.*;

public class NGOMapActivity extends AppCompatActivity{
    private FusedLocationProviderClient client;
    GoogleMap map;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private  SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;

    double dLat, dLon;

    private Switch mWorkingSwitch;

    private Button mLogout, mSettings, mCollect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_map);
        LatLng cObject = new CustomerMapActivity().clatLon();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);

        client = LocationServices.getFusedLocationProviderClient(NGOMapActivity.this);
        mCollect = (Button) findViewById(R.id.collect);
        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cObject!=null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            MarkerOptions markerOptions = new MarkerOptions().position(cObject).title("Food is available");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cObject, 14));
                            googleMap.addMarker(markerOptions).showInfoWindow();
                        }
                    });
                }
                else{
                    makeText(NGOMapActivity.this, "No nearby Donor found", LENGTH_SHORT).show();
                }
            }
        });


        mLogout = (Button) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(NGOMapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mSettings = (Button) findViewById(R.id.button3);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NGOMapActivity.this, DriverProfile.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mWorkingSwitch = (Switch) findViewById(R.id.workingswitch);
        mWorkingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    makeText(NGOMapActivity.this, "Location is live now", LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(NGOMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        getCurrentLocation();
                    }
                    else{
                        ActivityCompat.requestPermissions(NGOMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    }
//                    connectDriver();
                }else{
                    makeText(NGOMapActivity.this, "Location is not live now", LENGTH_SHORT).show();
//                    disconnectDriver();
                }
            }
        });

//        Bundle mapViewBundle = null;
//        if (savedInstanceState != null) {
//            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
//        }
//        mMapView = (MapView) findViewById(R.id.mapView2);
//        mMapView.onCreate(mapViewBundle);
//
//        mMapView.getMapAsync(this);
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
//        if (mapViewBundle == null) {
//            mapViewBundle = new Bundle();
//            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
//        }
//
//        mMapView.onSaveInstanceState(mapViewBundle);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mMapView.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mMapView.onStop();
//    }
//    @Override
//    protected void onPause() {
//        mMapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mMapView.onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
private void getCurrentLocation() {
    Task<Location> task = client.getLastLocation();
    task.addOnSuccessListener(new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if(location!=null){
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng latLng = new LatLng(dLat=location.getLatitude(), dLon=location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You Are Here");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        googleMap.addMarker(markerOptions).showInfoWindow();
                    }
                });
            }
        }
    });

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
        else{
            makeText(this, "Permission denied", LENGTH_SHORT).show();
        }
    }

    public LatLng dlatLon() {
        LatLng nlatLng = new LatLng(dLat, dLon);
        return nlatLng;
    }
}