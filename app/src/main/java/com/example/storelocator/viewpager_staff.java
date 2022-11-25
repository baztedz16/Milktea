package com.example.storelocator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.storelocator.fragment.delivery_history;
import com.example.storelocator.fragment.fordelivery;
import com.example.storelocator.fragment.listorders;
import com.example.storelocator.fragment.rider_list_reports;

public class viewpager_staff extends FragmentStateAdapter {
    public viewpager_staff(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new listorders();
            case  1:
                return  new fordelivery();
            case 2:
                return  new delivery_history();
            case 3:
                return  new rider_list_reports();
            default:
                return new listorders();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
