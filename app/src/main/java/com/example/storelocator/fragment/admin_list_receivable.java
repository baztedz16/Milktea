package com.example.storelocator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.R;
import com.example.storelocator.adapter_receivables;
import com.example.storelocator.adapter_userlist;
import com.example.storelocator.helper_payables;
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
import java.util.List;


public class admin_list_receivable extends Fragment {
    ArrayList<helper_payables> list;
    adapter_receivables myAdapter;
    RecyclerView recyclerView;
    TextView textView15;
    Spinner optionlist;

    Query query1;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adminreceivables, container, false);
        recyclerView = view.findViewById(R.id.accountview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        list = new ArrayList<>();
        myAdapter = new adapter_receivables(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        optionlist = view.findViewById(R.id.optionlist);
        //defaultview();
        listoption();

        optionlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(optionlist.getSelectedItem().toString().equals("Receivables")){
                    defaultview();
                }else{
                    defaultview2();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void listoption(){
        final List<String> areas = new ArrayList<String>();
        areas.clear();
        areas.add("Receivables");
        areas.add("Payables");
        Spinner areaSpinner =optionlist;
        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areasAdapter);

    }
    public void defaultview(){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        query1=reference.child("payables").orderByChild("status").equalTo("Under review");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                list.clear();
                if (dataSnapshot.exists()) {

                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_payables user = snapshot.getValue(helper_payables.class);
                        list.add(user);
                    }

                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void defaultview2(){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        query1=reference.child("storereceivables").orderByChild("status").equalTo("Under review");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                list.clear();
                if (dataSnapshot.exists()) {

                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_payables user = snapshot.getValue(helper_payables.class);
                        list.add(user);
                    }

                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}