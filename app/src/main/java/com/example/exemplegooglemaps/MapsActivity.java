package com.example.exemplegooglemaps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; //en metre
    private long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; //en millisecond (2 mn)
    protected LocationManager locationManager;
    Context context;

    private Location location;
    private double latitude;
    private double longitude;
    private boolean checkGPS;
    private boolean checkNetwork;
    private String typePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Je n'ai pas l'autorisation", Toast.LENGTH_SHORT).show();
            LatLng paris = new LatLng(48.8566, 2.3522);
            initMap(paris);
            return;
        }

        // enable geolocalisation
        mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {

            // get GPS status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }

        // Mode GPS
        if (checkGPS) {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    (LocationListener) context
            );

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } else if (checkNetwork) {

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    (LocationListener) context
            );

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            LatLng myPosition = new LatLng(latitude, longitude);

            initMap(myPosition);
        }

    }

    private void initMap(LatLng latLng) {

        // LatLng paris = new LatLng(48.8566, 2.3522);

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Paris"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

        // Zoom level :
        // 1: World
        // 5: Landmass/continent
        // 10: City
        // 15: Streets
        // 20: Buildings

        // effet zoom animé
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(paris, 10));
    }

    @Override
    public void onLocationChanged(Location location) {
        // Efface les markers
        mMap.clear();

        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(myPosition).title("Moi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 20));
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
