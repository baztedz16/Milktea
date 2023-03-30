package com.example.storelocator.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storelocator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PromoActivity extends AppCompatActivity {

    RecyclerView rec_promos;

    DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dataref2 = FirebaseDatabase.getInstance().getReference().child("Promos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        rec_promos = findViewById(R.id.rec_promos);
        List<HelperPromo> list = new ArrayList<>();

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.child("Promos").getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error: ",""+error);
            }
        });



    }
}