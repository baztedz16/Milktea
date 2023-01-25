package com.example.storelocator.fragment;

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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
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


public class admin_list_account extends Fragment {
    ArrayList<helper_user> list;
    adapter_userlist myAdapter;
    RecyclerView recyclerView;
    TextView textView15;
    Spinner spinner;
    Query query1;
    EditText usernameSearch;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountlist, container, false);
        textView15 = view.findViewById(R.id.textView15);
        usernameSearch = view.findViewById(R.id.usernameSearch);
        recyclerView = view.findViewById(R.id.accountview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> list2 = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sorting));
        list2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list2);

        list = new ArrayList<>();
        myAdapter = new adapter_userlist(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        defaultview();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String acctype = spinner.getSelectedItem().toString();
                defaultview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        usernameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(usernameSearch.getText().toString().equals("") || usernameSearch.getText().toString().isEmpty()){
                    defaultview();
                }else{
                    defaultview(spinner.getSelectedItem().toString(),usernameSearch.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    //for bargraph
    public void defaultview(){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        query1=reference.child("users");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_user user = snapshot.getValue(helper_user.class);
                        if(!user.getAccountype().equals("Store Owner") && !user.getAccountype().equals("Admin")){
                            list.add(user);
                        }

//                        if(accountype.equals("STAFF") && (user.getStatus().equals("5") )){
//                            if(user.getStore().equals(staffstore)){
//                                if(user.getStore().equals(staffstore)){
//
//                                    Log.i("R","1");
//                                }
//                            }
//                        }else{
//                            if(user.getRider().equals(rider) ){
//
//                                Log.i("2DATA",rider+":"+snapshot.child("rider").getValue().toString());
//                                list.add(user);
//                            }
//                        }
                    }
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
    public void defaultview(String values){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        query1=reference.child("users");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_user user = snapshot.getValue(helper_user.class);
                        if(!user.getAccountype().equals("Store Owner") && !user.getAccountype().equals("Admin")){
                            if(user.getAccountype().equals(values)){
                                list.add(user);
                            }

                        }

//                        if(accountype.equals("STAFF") && (user.getStatus().equals("5") )){
//                            if(user.getStore().equals(staffstore)){
//                                if(user.getStore().equals(staffstore)){
//
//                                    Log.i("R","1");
//                                }
//                            }
//                        }else{
//                            if(user.getRider().equals(rider) ){
//
//                                Log.i("2DATA",rider+":"+snapshot.child("rider").getValue().toString());
//                                list.add(user);
//                            }
//                        }
                    }
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
    public void defaultview(String values, String username){

        SharedPreferences preferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");

        query1=reference.child("users");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_user user = snapshot.getValue(helper_user.class);
                        if(!user.getAccountype().equals("Store Owner") && !user.getAccountype().equals("Admin")){
                            if(user.getAccountype().equals(values) && user.getFullname().contains(username)){
                                list.add(user);
                            }

                        }

//                        if(accountype.equals("STAFF") && (user.getStatus().equals("5") )){
//                            if(user.getStore().equals(staffstore)){
//                                if(user.getStore().equals(staffstore)){
//
//                                    Log.i("R","1");
//                                }
//                            }
//                        }else{
//                            if(user.getRider().equals(rider) ){
//
//                                Log.i("2DATA",rider+":"+snapshot.child("rider").getValue().toString());
//                                list.add(user);
//                            }
//                        }
                    }
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