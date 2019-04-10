package com.example.findsitter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView sitter_place;
    DatabaseReference reference, reference2;
    ArrayList<Sitter> list;
    SitterAdapter sitterAdapter;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sitter_place = findViewById(R.id.sitter_place);
        textView = findViewById(R.id.textView);
        sitter_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Sitter>();

        requestPermission();



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Cleaner");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Sitter p = dataSnapshot1.getValue(Sitter.class);
                    list.add(p);
                }
                sitterAdapter = new SitterAdapter(MainActivity.this, list);
                sitter_place.setAdapter(sitterAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
//                textView.setText("a");
            }
        });


    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void saveLocation(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    textView.setText(location.toString());

//                    reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child("vsdlk12");
//                    GeoFire geoFire = new GeoFire(reference2);
//                    geoFire.setLocation("location", new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
//
//                        @Override
//                        public void onComplete(String key, DatabaseError error) {
//                            if(error != null){
//                                Toast.makeText(getApplicationContext(), "Not saved", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
