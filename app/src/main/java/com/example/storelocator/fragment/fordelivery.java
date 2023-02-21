package com.example.storelocator.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.storelocator.R;
import com.example.storelocator.adapter_rider_delivery;
import com.example.storelocator.adapter_rider_order;
import com.example.storelocator.helper_order_rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class fordelivery extends Fragment {

    ArrayList<helper_order_rider> list;
    adapter_rider_delivery myAdapter;
    RecyclerView recyclerView;
    TextView textView15;
    EditText date;
    DatePickerDialog.OnDateSetListener startdate;

    Query query1;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fordelivery, container, false);
        textView15 = view.findViewById(R.id.textView15);

        date = view.findViewById(R.id.date);
        recyclerView = view.findViewById(R.id.deliveryView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        date.setVisibility(View.INVISIBLE);

        list = new ArrayList<>();
        myAdapter = new adapter_rider_delivery(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        defaultview();


        // Inflate the layout for this fragment
        return view;
    }
    public void defaultview(){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        if(accountype.equals("Rider")){
            query1=reference.child("orders");
        }else{
            query1=reference.child("orders");
        }

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_order_rider orders = snapshot.getValue(helper_order_rider.class);

                        System.out.println("accounttype:"+accountype);
                        System.out.println("status:"+orders.getStatus());

//                        if((accountype.equals("STAFF") || accountype.equals("Store Owner")) && (orders.getStatus().equals("1") || orders.getStatus().equals("2") || orders.getStatus().equals("3") || orders.getStatus().equals("4"))){
//                            list.add(orders);
//                            Log.i("R","1");
//                        }else{
//                            if(orders.getRider().equals(rider)){
//                                if(orders.getStatus().equals("2") || orders.getStatus().equals("3") || orders.getStatus().equals("4")) {
//
//                                    Log.i("2DATA", rider + ":" + snapshot.child("rider").getValue().toString());
//                                    list.add(orders);
//                                }
//                            }
//                        }
                        if (accountype!=null && orders!=null) {
                            if((accountype.equals("STAFF") || accountype.equals("Store Owner")) && (orders.getStatus().equals("1") || orders.getStatus().equals("2") || orders.getStatus().equals("3") || orders.getStatus().equals("4"))){
                                list.add(orders);
                                Log.i("R","1");
                            }else{
                                if(orders.getRider().equals(rider)){
                                    if(orders.getStatus().equals("2") || orders.getStatus().equals("3") || orders.getStatus().equals("4")) {

                                        Log.i("2DATA", rider + ":" + snapshot.child("rider").getValue().toString());
                                        list.add(orders);
                                    }
                                }
                            }
                        }
                    }
                    Collections.reverse(list);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}