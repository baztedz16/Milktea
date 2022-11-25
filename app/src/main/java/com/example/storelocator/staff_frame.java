package com.example.storelocator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class staff_frame extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 vp;
    viewpager_staff viewpager_staff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_frame);

        tabLayout = findViewById(R.id.riderLayout);
        vp = findViewById(R.id.viewpager);
        viewpager_staff = new viewpager_staff(this);
        vp.setAdapter(viewpager_staff);

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
}