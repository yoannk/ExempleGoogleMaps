package com.example.exemplegooglemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> permissions;
    ArrayList<String> permissionsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        if (Build.VERSION.SDK_INT >= 23) {
            permissions = new ArrayList<>();
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

            callPermissions();
        } else {
            callIntent();
        }
    }

    private void callPermissions() {
        permissionsRequest = new ArrayList<>();

        for (String permissionDemande : permissions) {
            if (ContextCompat.checkSelfPermission(context, permissionDemande) != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permissionDemande);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            callIntent();
        } else {
            String[] request = new String[permissionsRequest.size()];
            request = permissionsRequest.toArray(request);
            ActivityCompat.requestPermissions(this, request, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callIntent();
    }

    private void callIntent() {
        Intent intent = new Intent(context, MapsActivity.class);
        startActivity(intent);
    }
}
