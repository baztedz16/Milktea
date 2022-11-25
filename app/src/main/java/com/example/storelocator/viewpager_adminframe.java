package com.example.storelocator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.storelocator.fragment.admin_list_account;
import com.example.storelocator.fragment.admin_list_receivable;
import com.example.storelocator.fragment.admin_list_reports;
import com.example.storelocator.fragment.admin_list_store;
import com.example.storelocator.fragment.delivery_history;
import com.example.storelocator.fragment.fordelivery;
import com.example.storelocator.fragment.listorders;

public class viewpager_adminframe extends FragmentStateAdapter {
    public viewpager_adminframe(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new admin_list_account();
            case  1:
                return  new admin_list_store();
            case 2:
                return  new admin_list_reports();
            case 3:
                return  new admin_list_receivable();
            default:
                return new admin_list_account();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
