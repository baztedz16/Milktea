package com.example.storelocator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class store_owner_staff extends AppCompatActivity {
    Button addstaff;

    protected void onCreate(Bundle savedInstances) {

        super.onCreate(savedInstances);
        setContentView(R.layout.list_staff);

        addstaff = findViewById(R.id.addstaff);


        addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(store_owner_staff.this,signupstaff.class);
                intent.putExtra("storeSelect",getIntent().getStringExtra("storeSelect"));
                startActivity(intent);
            }
        });
    }
}
