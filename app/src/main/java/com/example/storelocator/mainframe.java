package com.example.storelocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class mainframe extends AppCompatActivity {
    EditText editTextname;
    Button buttonfetch,buttonStoreList,viewCart,viewOrder;
    ListView listview;
    String Name, City, Country, locClass;
    ProgressDialog mProgressDialog;
    ArrayList<helper_product> list;
    adapter_storelist_items myAdapter;
    RecyclerView recyclerView;

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
        setContentView(R.layout.main_frame);
        editTextname = findViewById(R.id.etname);
        buttonfetch = findViewById(R.id.btnfetch);
        recyclerView = findViewById(R.id.storeList);
        buttonStoreList = findViewById(R.id.liststoreBtn);
        viewCart = findViewById(R.id.viewCart);
        viewOrder = findViewById(R.id.viewOrders);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_storelist_items(this,list);
        recyclerView.setAdapter(myAdapter);


        //view product listed to the mainframe

        defaultview();

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),user_order_get.class);
                startActivity(intent);
            }
        });
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),mainframe_viewcart.class);
                startActivity(intent);
            }
        });
        buttonStoreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),list_store.class);
                startActivity(intent);
                //finish();
            }
        });
        buttonfetch.setOnClickListener(new View.OnClickListener() {
            String searchtext = editTextname.getText().toString();
            @Override
            public void onClick(View view) {
                if(editTextname.getText().toString().equals(null) || editTextname.getText().toString().equals("")){
                    Query query1=reference.child("products").orderByChild("storeOwner").startAt(getIntent().getStringExtra("storeName")).endAt(getIntent().getStringExtra("storeName")+"\uf8ff");
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                list.clear();
                                Log.i("R","1");
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    helper_product product = snapshot.getValue(helper_product.class);
                                    Log.i("R","2");
                                    list.add(product);
                                }
                                myAdapter.notifyDataSetChanged();
                            }else{
                                Log.i("R","3");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    Query query1=reference.child("products").orderByChild("storeOwner").startAt(editTextname.getText().toString().trim()).endAt(editTextname.getText().toString().trim()+"\uf8ff");
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("R",editTextname.getText().toString());
                            if (dataSnapshot.exists()) {
                                list.clear();
                                Log.i("R","4");
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    helper_product product = snapshot.getValue(helper_product.class);

                                    list.add(product);
                                }
                                myAdapter.notifyDataSetChanged();
                            }else{
                                Log.i("R","6");
                                list.clear();
                                myAdapter.notifyDataSetChanged();
                                //Log.i("R",searchtext);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    public void defaultview(){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("currentStore",getIntent().getStringExtra("storeName"));
        Query query1=reference.child("products").orderByChild("storeOwner").startAt(getIntent().getStringExtra("storeName")).endAt(getIntent().getStringExtra("storeName")+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product product = snapshot.getValue(helper_product.class);

                        list.add(product);
                    }
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