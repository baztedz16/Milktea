package com.example.storelocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class list_store extends AppCompatActivity {
    EditText search;
    Button vieworders;
    ListView listview;
    String Name, City, Country, locClass;
    ProgressDialog mProgressDialog;
    ArrayList<helper_liststore> list;
    adapter_storelist myAdapter;
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
        setContentView(R.layout.list_store);
        recyclerView = findViewById(R.id.storeList2);
        vieworders = findViewById(R.id.vieworders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_storelist(this,list);
        recyclerView.setAdapter(myAdapter);

        search = findViewById(R.id.search);
        //view product listed to the mainframe

        defaultview();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(search.getText().toString().equals("")){
                    defaultview();
                }else{
                    defaultviewSearch(search.getText().toString());
                    if(myAdapter.list.isEmpty()){
                        Toast.makeText(list_store.this,"Store Not Found",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        vieworders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),user_order_get.class);
                startActivity(intent);
            }
        });

    }
    public void defaultviewSearch(String searchval){
        Query query1=reference.child("users").orderByChild("accountype").equalTo("Store Owner");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_liststore product = snapshot.getValue(helper_liststore.class);
                        if(product.getStorename().contains(searchval)){
                            list.add(product);
                        }

                    }
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void defaultview(){
        Query query1=reference.child("users").orderByChild("accountype").equalTo("Store Owner");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_liststore product = snapshot.getValue(helper_liststore.class);

                        list.add(product);
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
