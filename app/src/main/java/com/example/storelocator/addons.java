package com.example.storelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.R;
import com.example.storelocator.activity_signup;
import com.example.storelocator.adapter_rider_delivery;
import com.example.storelocator.adapter_userlist;
import com.example.storelocator.helper_order_rider;
import com.example.storelocator.helper_user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


public class addons extends DialogFragment {
    TextView storename;
    RatingBar storerating;
    ArrayList<helper_product> list;
    adapter_addons myAdapter;
    int ii = 0;
    ArrayList<String> list2;
    RecyclerView recyclerView;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_ons_frame, container, false);

        recyclerView = view.findViewById(R.id.listaddons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list = new ArrayList<>();
        myAdapter = new adapter_addons(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        defaultview();
        // Inflate the layout for this fragment
        return view;
    }
    public void defaultview(){
        SharedPreferences sh = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);

        String storename = sh.getString("store", "");
        Query query1=reference.child("products");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",storename);
                double totalrating = 0.0;
                int reviewcount = 0;
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product addons = snapshot.getValue(helper_product.class);
                        list.add(addons);


                    }
                    myAdapter.notifyDataSetChanged();
                }else{
                    //Log.i("error at default:","6"+getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}