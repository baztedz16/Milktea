package com.example.storelocator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.storelocator.R;
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
import java.util.Collections;

public class listorders extends Fragment {

    ArrayList<helper_order_rider> list;
    adapter_rider_order myAdapter;
    RecyclerView recyclerView;
    TextView textView15;
    Query query1;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listorders, container, false);
        textView15 = view.findViewById(R.id.textView15);
        recyclerView = view.findViewById(R.id.orderVIew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        list = new ArrayList<>();
        myAdapter = new adapter_rider_order(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        defaultview();



        // Inflate the layout for this fragment
        return view;
    }
    public void defaultview(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        String username = preferences.getString("username","");

        if(accountype.equals("STAFF") || accountype.equals("Store Owner")){
            query1=reference.child("orders").orderByChild("status").equalTo("0");
        }else{
            query1=reference.child("orders").orderByChild("status").equalTo("1");
        }
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_order_rider orders = snapshot.getValue(helper_order_rider.class);



                        System.out.println("storeGet from helper"+orders.getStore());
                        System.out.println("storeGet from database"+staffstore);


                        if(accountype.equals("STAFF")){
//                            if(orders.getStore().equals(staffstore)){
//                                list.add(orders);
//                                Log.i("R","1");
//                            }
                            if (orders.getStore() != null && staffstore!=null) {
                                if (orders.getStore().equals(staffstore)) {
                                    list.add(orders);
                                    Log.i("R", "1");

                                }
                            }
                        } else if (accountype.equals("Rider")){
                            if(orders.getStatus().equals("1") || !orders.getRider().equals(username)){
                                list.add(orders);
                                Log.i("R","2");
                            }
                        }

                    }
                    Collections.reverse(list);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6"+accountype);
                    //Log.i("R",searchtext);
                    list.clear();
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}