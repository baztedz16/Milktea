package com.example.storelocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class user_order_get extends AppCompatActivity {

    ArrayList<helper_order_rider> list;
    adapter_user_order myAdapter;
    RecyclerView recyclerView;
    TextView textView15;

    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");


    /*public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    private boolean gps_enable = false;
    private boolean network_enable = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listorders);
        //textView15.setText("Geo");
        recyclerView = findViewById(R.id.orderVIew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_user_order(this,list);
        recyclerView.setAdapter(myAdapter);


        //view product listed to the mainframe

        defaultview();

    }

    public void defaultview(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);

        final String username = sh.getString("username","");
        Query query1=reference.child("orders").orderByChild("order_user").equalTo(username);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        helper_order_rider orders = snapshot.getValue(helper_order_rider.class);
                        /*SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
                        String username =sh.getString("username", "");
                        if(orders.getOrder_user().equals(username)){
                            list.add(orders);
                        }else{

                        }*/
                        list.add(orders);

                    }
                    Collections.reverse(list);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","ErrorListing");
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
