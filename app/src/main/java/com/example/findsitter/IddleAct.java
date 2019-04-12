package com.example.findsitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IddleAct extends AppCompatActivity {

    TextView btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iddle);


        btn_scan = findViewById(R.id.btn_scan);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent axsd = new Intent(IddleAct.this, MainActivity.class );
                startActivity(axsd);
                finish();
            }
        });



    }
}
