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
import com.example.storelocator.adapter_userlist;
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
import java.util.Locale;


public class admin_list_store extends Fragment {
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
        recyclerView = view.findViewById(R.id.accountview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> list2 = new ArrayAdapter<String>(getActivity()
                ,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.store_status));
        list2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list2);
        list = new ArrayList<>();
        myAdapter = new adapter_userlist(view.getContext(),list);
        recyclerView.setAdapter(myAdapter);
        usernameSearch = view.findViewById(R.id.usernameSearch);
        defaultview();
        // Inflate the layout for this fragment

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                defaultview(String.valueOf(i));
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
                SearchStore(usernameSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    public void SearchStore( String val){

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
                        if(user.getAccountype().equals("Store Owner") && user.getStorename().toLowerCase(Locale.ROOT).contains(val.toLowerCase(Locale.ROOT))){
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
                        if(user.getAccountype().equals("Store Owner") && !user.getAccountype().equals("Admin")){
                            user.setFullname(user.getStorename());
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void defaultview(String sorting){

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
                        if(user.getAccountype().equals("Store Owner") && !user.getAccountype().equals("Admin")){
                           if(user.getActivation().equals(sorting)){
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