package com.example.storelocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;

public class addons_frame extends AppCompatActivity {
    TextView storename;
    RatingBar storerating;
    ArrayList<helper_product> list;
    adapter_addons myAdapter;
    int ii = 0;
    ArrayList<String> list2;
    RecyclerView recyclerView;

    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ons_frame);

        recyclerView = findViewById(R.id.listaddons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new adapter_addons(this,list);
        recyclerView.setAdapter(myAdapter);



        defaultview();


    }



    public void defaultview(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);

        String storename = sh.getString("store", "");
        Query query1=reference.child("products").orderByChild("store").startAt(storename).endAt(storename+"\uf8ff");
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
                    storerating.setRating((float) totalrating/reviewcount);
                    Collections.reverse(list);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("error at default:","6"+getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}