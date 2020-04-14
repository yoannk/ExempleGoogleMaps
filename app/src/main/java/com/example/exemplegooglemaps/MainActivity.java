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

        if (Build.VERSION.SDK_INT >= 23) //telephone supérieur à android 5.1
        {
            permissions = new ArrayList<>();
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(Manifest.permission.SEND_SMS);
            //appelle la méthode permission
            callPermissions();
        } else {
            Intent intent = new Intent(context, MapsActivity.class);
            startActivity(intent);
        }
    }

    private void callPermissions() {
        permissionsRequest = new ArrayList<>();

        for (int i = 0; i < permissions.size(); i++) {
            String permissionDemande = permissions.get(i).toString();

            if (ContextCompat.checkSelfPermission(context, permissionDemande)
                    != PackageManager.PERMISSION_GRANTED) {

                permissionsRequest.add(permissionDemande);
            }
        }

        if (permissionsRequest.isEmpty()) //toutes les permissions ont été approuvées
        {
            Intent intent = new Intent(context, MapsActivity.class);
            startActivity(intent);
        } else //demande l'acceptation des permissions
        {
            String[] request = new String[permissionsRequest.size()];
            request = permissionsRequest.toArray(request);
            ActivityCompat.requestPermissions(this, request, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        /*switch (requestCode) {
            case 100: {
                //utilisation spécifique en fonction de l'application
            }
            break;
        }*/

        Intent intent = new Intent(context, MapsActivity.class);
        startActivity(intent);

    }
}
