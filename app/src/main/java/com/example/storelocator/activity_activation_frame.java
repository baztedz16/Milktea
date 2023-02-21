package com.example.storelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class activity_activation_frame extends AppCompatActivity {

    public static Activity activating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        activating = activity_activation_frame.this;

        Button btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_activation_frame.this,activity_login.class));
                finishAffinity();
            }
        });
        btnBack.setVisibility(View.GONE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item3,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        SharedPreferences preferences = activity_activation_frame.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.Account){

            SharedPreferences preferences1 = this.getSharedPreferences("user", Context.MODE_PRIVATE);
            Intent intent = new Intent(activity_activation_frame.this,signupstaff.class);
            intent.putExtra("storeSelect","N/A");
            intent.putExtra("fullname",preferences.getString("fullname",""));
            intent.putExtra("username",preferences.getString("username",""));
            intent.putExtra("email",preferences.getString("email",""));
            intent.putExtra("password",preferences.getString("password",""));
            intent.putExtra("phone",preferences.getString("phone",""));
            intent.putExtra("address",preferences.getString("address",""));
            intent.putExtra("long",preferences.getString("longti",""));
            intent.putExtra("lat",preferences.getString("lati",""));
            intent.putExtra("hasdata","2");
            startActivity(intent);
        }else if(item_id == R.id.mangeStore){
            Intent intent = new Intent(activity_activation_frame.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
            finishAffinity();
        }

        return true;
    }
}