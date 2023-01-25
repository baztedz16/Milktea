package com.example.storelocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addons_dialogfragment extends DialogFragment {
    TextView storename;
    RatingBar storerating;
    ArrayList<helper_product> list;
    adapter_addons myAdapter;
    int ii = 0;
    ArrayList<String> list2;
    RecyclerView recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    static addons_dialogfragment newInstance(int alert_dialog_two_buttons_title) {
        return new addons_dialogfragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_ons_frame, container, false);
        recyclerView = v.findViewById(R.id.listaddons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        list = new ArrayList<>();
        myAdapter = new adapter_addons(v.getContext(),list);
        recyclerView.setAdapter(myAdapter);

        defaultview();
        // Inflate the layout for this fragment

        return v;
    }


    public void defaultview() {
        SharedPreferences sh = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);

        String storename = sh.getString("store", "");
        Query query1 = reference.child("products").orderByChild("storeOwner").startAt(storename).endAt(storename+"\uf8ff");;
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R", storename);
                double totalrating = 0.0;
                int reviewcount = 0;
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R", "4");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product addons = snapshot.getValue(helper_product.class);
                        if(addons.getCategory().equals("Add Ons")){
                            list.add(addons);
                        }

                        Log.i("error at default:","HERE: "+storename);


                    }
                    myAdapter.notifyDataSetChanged();
                } else {

                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
