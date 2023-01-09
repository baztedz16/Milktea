package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class store_owner_staff extends AppCompatActivity {
    Button addstaff;

    ArrayList<helper_user> list;
    adapter_userlist myAdapter;
    RecyclerView recyclerView;
    Query query1;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    protected void onCreate(Bundle savedInstances) {

        super.onCreate(savedInstances);
        setContentView(R.layout.list_staff);

        addstaff = findViewById(R.id.addstaff);

        recyclerView = findViewById(R.id.storeList2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        list = new ArrayList<>();
        myAdapter = new adapter_userlist(this,list);
        recyclerView.setAdapter(myAdapter);
        defaultview();

        addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addstaff();
            }
        });
    }
    private void addstaff(){
        Intent intent = new Intent(store_owner_staff.this,signupstaff.class);
        intent.putExtra("storeSelect",getIntent().getStringExtra("storeSelect"));
        intent.putExtra("hasdata","0");
        startActivity(intent);
    }
    public void defaultview(){

        SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
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
                        if(!user.getAccountype().equals("Store Owner") && user.getStorename().equals(getIntent().getStringExtra("storeSelect"))){
                            list.add(user);
                        }
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
