package com.example.storelocator;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    int ii = 0;
    ArrayList<String> list2;
    adapter_storelist_category myAdapter2;
    RecyclerView recyclerView,recyclerViewcatlist;
    String catNow;

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
        recyclerViewcatlist = findViewById(R.id.catlist);
        buttonStoreList = findViewById(R.id.liststoreBtn);
        viewCart = findViewById(R.id.viewCart);
        viewOrder = findViewById(R.id.viewOrders);
        SharedPreferences preferences = mainframe.this.getSharedPreferences("selectionCat", Context.MODE_PRIVATE);
        catNow = preferences.getString("cat","");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_storelist_items(this,list);
        recyclerView.setAdapter(myAdapter);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewcatlist.setLayoutManager(mLayoutManager);
        recyclerViewcatlist.setHasFixedSize(true);

        list2 = new ArrayList<String>();
        myAdapter2 = new adapter_storelist_category(this,list2);
        recyclerViewcatlist.setAdapter(myAdapter2);
        recyclerViewcatlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                SharedPreferences preferences = mainframe.this.getSharedPreferences("selectionCat", Context.MODE_PRIVATE);
                String cat = preferences.getString("cat","");
                defaultviewSearch(cat);
            }
        });




        //view product listed to the mainframe

        defaultview();
        defaultview2();




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

    public void defaultview2(){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("currentStore",getIntent().getStringExtra("storeName"));
        Query query1=reference.child("category").orderByChild("store").startAt(getIntent().getStringExtra("storeName")).endAt(getIntent().getStringExtra("storeName")+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",editTextname.getText().toString());
                if (dataSnapshot.exists()) {
                    list2.clear();
                    list2.add("All");
                    Log.i("R","4");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_category product = snapshot.getValue(helper_category.class);

                        list2.add(product.categoryname);
                    }
                    myAdapter2.notifyDataSetChanged();
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
    public void defaultviewSearch(String cat){
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
                    Log.i("R",cat);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product product = snapshot.getValue(helper_product.class);
                        if(cat.equals("All")){
                            list.add(product);
                        }else{
                            if(cat.equals(product.getCategory())){
                                list.add(product);
                            }
                        }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item3,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        SharedPreferences preferences = mainframe.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.Account){

            SharedPreferences preferences1 = this.getSharedPreferences("user", Context.MODE_PRIVATE);
            Intent intent = new Intent(mainframe.this,signupstaff.class);
            intent.putExtra("storeSelect","N/A");
            intent.putExtra("fullname",preferences.getString("fullname",""));
            intent.putExtra("username",preferences.getString("username",""));
            intent.putExtra("email",preferences.getString("email",""));
            intent.putExtra("password",preferences.getString("password",""));
            intent.putExtra("phone",preferences.getString("phone",""));
            intent.putExtra("address",preferences.getString("address",""));
            intent.putExtra("long",preferences.getString("longti",""));
            intent.putExtra("lat",preferences.getString("lati",""));
            intent.putExtra("hasdata","2");
            startActivity(intent);
        }else if(item_id == R.id.mangeStore){
            Intent intent = new Intent(mainframe.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
        }

        return true;
    }

}