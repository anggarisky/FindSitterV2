package com.example.findsitter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileAct extends AppCompatActivity {

    DatabaseReference reference;

    TextView textProvider, textId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        final String cleaner_id_ku = bundle.getString("cleaner_id");

        textProvider = findViewById(R.id.textProvider);
        textId = findViewById(R.id.textId);



        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cleaner").child(cleaner_id_ku).child("info");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    textProvider.setText("You chose: " + dataSnapshot.child("cleaner_name").getValue().toString());
                    textId.setText("You chose: " + dataSnapshot.child("cleaner_id").getValue().toString());

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
