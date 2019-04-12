package com.example.findsitter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    RecyclerView sitter_place;
    DatabaseReference reference, reference2, reference3, reference4;

    HashMap<String, Sitter> list;
    SitterAdapter sitterAdapter;


    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sitter_place = findViewById(R.id.sitter_place);
        textView = findViewById(R.id.textView);

        sitter_place.setLayoutManager(new LinearLayoutManager(this));
        sitter_place.setHasFixedSize(true);
        list = new HashMap<String, Sitter>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        requestPermission();

        sitterAdapter = new SitterAdapter(MainActivity.this, list, list.values().toArray());
        sitter_place.setAdapter(sitterAdapter);
    }

    public void requestLoc(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED
        ){
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if(locationResult != null){

                        double latitude = locationResult.getLastLocation().getLatitude();
                        double longitude = locationResult.getLastLocation().getLongitude();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child("Location");
                        GeoFire geoFire = new GeoFire(reference);

                        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 0.1);

                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(final String key, GeoLocation location) {

                                Query locationDataQuery = FirebaseDatabase.getInstance().getReference("Users").child("Cleaner").child(key);

                                locationDataQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                Sitter p = dataSnapshot1.getValue(Sitter.class);

                                                list.put(p.cleaner_id, p);
                                            }

                                            sitterAdapter.update(list, list.values().toArray());

                                            Integer rand = new Random().nextInt(sitterAdapter.getItemCount());

                                            Sitter dataSelected = (Sitter) list.values().toArray()[rand];

                                            Intent axs = new Intent(MainActivity.this, ProfileAct.class);
                                            axs.putExtra("cleaner_id", dataSelected.cleaner_id);
                                            startActivity(axs);


                                            Toast.makeText(getApplicationContext(), "Data Ada, index ke " + dataSelected.cleaner_id, Toast.LENGTH_SHORT).show();

                                        } else {

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onKeyExited(String key) {

                                list.remove(key);

                                sitterAdapter.update(list, list.values().toArray());

                                Toast.makeText(getApplicationContext(), key + " left", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(getIntent());

                            }

                            @Override
                            public void onKeyMoved(String key, GeoLocation location) {

                            }

                            @Override
                            public void onGeoQueryReady() {

                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {

                            }
                        });

                    }
                }
            }, getMainLooper());

        }
        else {
            requestPermission();
        }

    }


    public void requestPermission(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                requestLoc();
            }
        });
    }
}