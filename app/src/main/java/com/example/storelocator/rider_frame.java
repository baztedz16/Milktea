package com.example.storelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class rider_frame extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 vp;
    viewpager_ridersframe viewpager_ridersframe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_frame);

        tabLayout = findViewById(R.id.riderLayout);
        vp = findViewById(R.id.viewpager);
        viewpager_ridersframe = new viewpager_ridersframe(this);
        vp.setAdapter(viewpager_ridersframe);
        SharedPreferences preferences = rider_frame.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = preferences.getString("username","");
        String acc = preferences.getString("accountype","");
        String shop = preferences.getString("Store","");
        String name = preferences.getString("fullname","");

        if (acc.equals("Rider")) {
            getSupportActionBar().setTitle(name);
            tabLayout.getTabAt(3).setText("Payables");
        } else if (acc.equals("STAFF")) {
            getSupportActionBar().setTitle(shop+" | "+name);
            tabLayout.removeTab(tabLayout.getTabAt(3));
        } else {
            getSupportActionBar().setTitle(shop+" | "+name);
            tabLayout.getTabAt(3).setText("Reports");
        }


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
        SharedPreferences preferences = rider_frame.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.mangeStore){
            Intent intent = new Intent(rider_frame.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
        }else if(item_id == R.id.Account){

            Intent intent = new Intent(rider_frame.this,signupstaff.class);
            intent.putExtra("storeSelect",staffstore);
            intent.putExtra("fullname",preferences.getString("fullname",""));
            intent.putExtra("username",preferences.getString("username",""));
            intent.putExtra("email",preferences.getString("email",""));
            intent.putExtra("password",preferences.getString("password",""));
            intent.putExtra("phone",preferences.getString("phone",""));
            intent.putExtra("address",preferences.getString("address",""));
            intent.putExtra("long",preferences.getString("longti",""));
            intent.putExtra("lat",preferences.getString("lati",""));
            intent.putExtra("action",preferences.getString("1",""));
            intent.putExtra("hasdata","2");
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }
}