package com.example.findsitter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAct extends AppCompatActivity {

    Button btn_signin;
    EditText user_id;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_signin = findViewById(R.id.btn_signin);
        user_id = findViewById(R.id.user_id);


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id_new = user_id.getText().toString();

                reference = FirebaseDatabase.getInstance().getReference("Users").child("Cleaner").child(user_id_new);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Intent abcd = new Intent(LoginAct.this, MainActivity.class);
                            abcd.putExtra("user_id_now", user_id_new);
                            startActivity(abcd);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Data not found" + user_id_new, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
