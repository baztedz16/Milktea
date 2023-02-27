package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mainframe_viewcart extends AppCompatActivity {
    TextView total,logtitxt,latitxt,address;
    EditText editTextname;
    Button buttonfetch,buttonStoreList,button3;
    ListView listview;
    String Name, City, Country, locClass;
    String id = null;
    ProgressDialog mProgressDialog;
    ArrayList<helper_cart> list;
    adapter_cart myAdapter;
    RecyclerView recyclerView;
    SharedPreferences preferences;
    int cartValue =0,totalitems=0 ;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    static Double lh,lt;
    List<String> s  = new ArrayList<String>();




    /*public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    private boolean gps_enable = false;
    private boolean network_enable = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainframe_viewcart);
        editTextname = findViewById(R.id.etname);
//        buttonfetch = findViewById(R.id.btnfetch);
        recyclerView = findViewById(R.id.storeList);
        buttonStoreList = findViewById(R.id.placeorder);
        total = findViewById(R.id.total);

        address = findViewById(R.id.address);
        logtitxt = findViewById(R.id.logtitxt);
        latitxt = findViewById(R.id.latitxt);
        button3= findViewById(R.id.button3);


        preferences=this.getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        s.clear();
        list = new ArrayList<>();
        myAdapter = new adapter_cart(this,list);
        recyclerView.setAdapter(myAdapter);

        SharedPreferences sh1 = getSharedPreferences("user", MODE_PRIVATE);

        String addressdata = sh1.getString("address", "");
        String lati = sh1.getString("lati", "");
        String longti = sh1.getString("longti", "");


        defaultview();
        showprevaddress();
        s.add(sh1.getString("address", ""));

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Toast.makeText(getBaseContext(), "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(mainframe_viewcart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainframe_viewcart.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(mainframe_viewcart.this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(mainframe_viewcart.this, Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            address.setText(addressList.get(0).getAddressLine(0));
                            lh = addressList.get(0).getLongitude();
                            lt = addressList.get(0).getLatitude();
                            logtitxt.setText(String.valueOf(addressList.get(0).getLongitude()));
                            latitxt.setText(String.valueOf(addressList.get(0).getLatitude()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {

                    }
                }
            });
        }
        //view product listed to the mainframe


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder builder = new AlertDialog.Builder(mainframe_viewcart.this);
                builder.setTitle("Address");
                final ArrayAdapter<String> adp = new ArrayAdapter<String>(mainframe_viewcart.this,
                        android.R.layout.simple_spinner_item, s);

                final Spinner sp = new Spinner(mainframe_viewcart.this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                sp.setAdapter(adp);
                builder.setView(sp);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        address.setText(sp.getSelectedItem().toString());
                        getPreciseLocation();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        buttonStoreList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertCharges();
            }


            //Intent intent = new Intent(getApplicationContext(),list_store.class);
            // startActivity(intent);
            //finish();

        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total.setText((getIntent().getStringExtra("total")));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });

//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
//            Toast.makeText(getBaseContext(), "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
//        } else {
//            if (ActivityCompat.checkSelfPermission(mainframe_viewcart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainframe_viewcart.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(mainframe_viewcart.this);
//            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    Location location = task.getResult();
//                    if (location != null) {
//                        Geocoder geocoder = new Geocoder(mainframe_viewcart.this, Locale.getDefault());
//                        try {
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    } else {
//
//                    }
//                }
//            });
//        }

    }
    private  void alertCharges(){
        defaultview();
        double surcharge = Double.parseDouble(getIntent().getStringExtra("surcharge"));
        double value = 0.0;

        if(surcharge >0){
            value = (surcharge*2)+60;
        }else{
            value = 60;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mainframe_viewcart.this);
        builder.setTitle("Order Details");
        builder.setMessage("Your Orders Amount is: PHP"+ cartValue +"\n and Delivery Charge of: PHP"+String.valueOf(value)+"\n"+"TOTAL:"+(cartValue+value));

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkout();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void checkout(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);

        String address2 = address.getText().toString();
        String lati = latitxt.getText().toString();
        String longti = logtitxt.getText().toString();
        String store = sh.getString("store", "");
        final String username = preferences.getString("username","");
        Query query =reference.child("cart").orderByChild("username").equalTo(username);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("orders");
        id = reference.push().getKey();
        reference.child(id).child("order_id").setValue(id);
        reference.child(id).child("status").setValue("0");
        reference.child(id).child("order_user").setValue(username);
        reference.child(id).child("order_total").setValue(String.valueOf(cartValue+60));
        reference.child(id).child("address").setValue(address2);
        reference.child(id).child("longti").setValue(lati);
        reference.child(id).child("lati").setValue(longti);
        reference.child(id).child("store").setValue(store);
        reference.child(id).child("rider").setValue(store);
        String currentTime = String.valueOf(System.currentTimeMillis());
        reference.child(id).child("ordertime").setValue(currentTime);
        reference.child(id).child("deliverytime").setValue("00000000000");
        reference.child(id).child("itemcount").setValue(totalitems);



        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        reference.child(id).child("date_order").setValue(dtf.format(now));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if (snapshot1.exists()) {

                    try {
                        for (DataSnapshot snapshot : snapshot1.getChildren()) {
                            if (snapshot.child("orderstatus").getValue().equals("0") && snapshot.child("owner").getValue().equals(store)) {
                                Log.i("R", snapshot.child("cartid").getValue().toString());
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("cart").child(snapshot.child("cartid").getValue().toString());
                                //reference.setValue("sample");
                                reference.child("orderstatus").setValue("1");
                                reference.child("order_id").setValue(id);
                                //

                            }
                        }
                    }catch (Exception e){

                    }

                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent1 = new Intent(mainframe_viewcart.this,list_store.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
    }
    private void showPlacePicker() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(lt, lh)
                .showLatLong(true)
                .setMapRawResourceStyle(R.raw.map_style)
                .setMapType(MapType.HYBRID)
                .build(mainframe_viewcart.this);

        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
    }
    public void showprevaddress(){
        final String username = preferences.getString("username","");
        Query query1=reference.child("orders").orderByChild("order_user").equalTo(username);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try{
                            if(snapshot.child("status").getValue().toString().equals("5")){
                                helper_order_rider order = snapshot.getValue(helper_order_rider.class);
                                Log.i("Address","Try Toget"+username+" "+order.getAddress());
                                if(!s.contains(order.getAddress())){
                                    s.add(order.getAddress());
                                    Log.i("Address",s.toString());
                                }
                            }
                        }catch(Exception e){
                            Log.i("Error",e.toString());
                        }
                    }
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void getPreciseLocation(){
        final String username = preferences.getString("username","");
        Query query1=reference.child("orders").orderByChild("address").equalTo(address.getText().toString());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try{
                            helper_order_rider order = snapshot.getValue(helper_order_rider.class);
                            logtitxt.setText(order.getLongti());
                            logtitxt.setText(order.getLati());
                        }catch(Exception e){
                            Log.i("Error",e.toString());
                        }
                    }
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void defaultview(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        String store = sh.getString("store", "");
        final String username = preferences.getString("username","");
        Query query1=reference.child("cart").orderByChild("username").equalTo(username);
        Log.i("R",store);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());

                if (dataSnapshot.exists()) {
                    list.clear();
                    cartValue=0;
                    totalitems=0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        try{

                            if(!snapshot.child("orderstatus").getValue().toString().equals("1") && snapshot.child("owner").getValue().equals(store)){
                                helper_cart product = snapshot.getValue(helper_cart.class);
                                Log.i("OS:",snapshot.child("orderstatus").getValue().toString());
                                Log.i("Values",snapshot.child("price").getValue().toString() +"x"+ snapshot.child("qty").getValue().toString());
                                cartValue = cartValue + (Integer.parseInt(snapshot.child("price").getValue().toString())*Integer.parseInt(snapshot.child("qty").getValue().toString()));
                                totalitems = totalitems+Integer.parseInt(product.getQty());
                                Log.i("OrderTotal:", String.valueOf(cartValue));
                                list.add(product);
                            }
                        }catch(Exception e){
                            Log.i("Error",e.toString());
                        }

                    }
                    if(list.isEmpty()){
                        buttonStoreList.setVisibility(View.INVISIBLE);
                    }else{
                        buttonStoreList.setVisibility(View.VISIBLE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                logtitxt.setText(String.valueOf(addressData.getLongitude()));
                latitxt.setText(String.valueOf(addressData.getLatitude()));
                address.setText(String.valueOf(addressData.getAddressList().get(0).getAddressLine(0)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}