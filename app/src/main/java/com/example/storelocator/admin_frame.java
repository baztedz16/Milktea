package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class admin_frame extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 vp;
    viewpager_adminframe viewpager_adminframe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_frame);
        getSupportActionBar().setTitle("YFMA Control Panel");
        tabLayout = findViewById(R.id.adminLayout);
        vp = findViewById(R.id.viewpager);
        viewpager_adminframe = new viewpager_adminframe(this);
        vp.setAdapter(viewpager_adminframe);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item2,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        SharedPreferences preferences = admin_frame.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.mangeStore){
            Intent intent = new Intent(admin_frame.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
            finishAffinity();

        }else if(item_id == R.id.Account){

            SharedPreferences preferences1 = this.getSharedPreferences("user", Context.MODE_PRIVATE);
            Intent intent = new Intent(admin_frame.this,signupstaff.class);
            intent.putExtra("storeSelect","N/A");
            intent.putExtra("fullname",preferences.getString("fullname",""));
            intent.putExtra("username",preferences.getString("username",""));
            intent.putExtra("email",preferences.getString("email",""));
            intent.putExtra("password",preferences.getString("password",""));
            intent.putExtra("phone",preferences.getString("phone",""));
            intent.putExtra("address",preferences.getString("address",""));
            intent.putExtra("long",preferences.getString("longti",""));
            intent.putExtra("lat",preferences.getString("lati",""));
            intent.putExtra("action",preferences.getString("1",""));
            intent.putExtra("hasdata","3");
            startActivity(intent);

        }
        return true;
    }
}