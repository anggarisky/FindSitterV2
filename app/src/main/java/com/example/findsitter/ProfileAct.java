package com.example.findsitter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileAct extends AppCompatActivity {

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        final String cleaner_id_ku = bundle.getString("cleaner_id");



        reference = FirebaseDatabase.getInstance().getReference("Users").child("Location").child(cleaner_id_ku);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(), cleaner_id_ku, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cleaner offline", Toast.LENGTH_SHORT).show();
                    Intent axd = new Intent(ProfileAct.this, MainActivity.class );
                    startActivity(axd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent axsd = new Intent(ProfileAct.this, IddleAct.class );
        startActivity(axsd);
        finish();
    }
}
