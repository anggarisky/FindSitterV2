package com.example.findsitter;

import android.Manifest;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    RecyclerView sitter_place;
    DatabaseReference reference, reference2, reference3, reference4;

    ArrayList<Sitter> list;
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
        list = new ArrayList<Sitter>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(3000);
//        locationRequest.setFastestInterval(3000);

        requestPermission();

        sitterAdapter = new SitterAdapter(MainActivity.this, list);

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

                        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 100);

                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(final String key, GeoLocation location) {

                                Query locationDataQuery = FirebaseDatabase.getInstance().getReference("Users").child("Cleaner").child(key).orderByKey();

                                locationDataQuery.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            list.clear();

                                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                Sitter p = dataSnapshot1.getValue(Sitter.class);
                                                list.add(p);

                                            }

                                            sitterAdapter.notifyDataSetChanged();
                                            sitter_place.setAdapter(sitterAdapter);

                                            Toast.makeText(getApplicationContext(), "Data Ada", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onKeyExited(String key) {

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

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child("Location").child(user_id);
//        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.getRef().removeValue();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
