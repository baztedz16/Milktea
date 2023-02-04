package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.Model;
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
import java.util.Comparator;
import java.util.List;

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


    public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    double startlat,startlon,endlat,endlon;
    private boolean gps_enable = false;
    private boolean network_enable = false;
    /*public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    private boolean gps_enable = false;
    private boolean network_enable = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_store);
        SharedPreferences preferences = list_store.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String fullname = preferences.getString("fullname","");
        getSupportActionBar().setTitle(fullname);
        recyclerView = findViewById(R.id.storeList2);
        vieworders = findViewById(R.id.vieworders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_storelist(this,list);
        recyclerView.setAdapter(myAdapter);

        search = findViewById(R.id.search);
        //view product listed to the mainframe

        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();
        get_Myloc();
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
                    int i =0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_liststore product = snapshot.getValue(helper_liststore.class);

                        float[] result = new float[1];
                        Location.distanceBetween(startlat,startlon,Double.parseDouble(product.getDestlat()),Double.parseDouble(product.getDestlong()),result);
                        float distance = result[0];
                        double kl = (int)(distance/1000);
                        double display = 0;
                        String metric = "";
                        if(kl < 1){
                            display = (double) (kl/1000) ;
                            metric= "meters";
                        }else{
                            display = kl;
                            metric = "Km";
                        }
                        product.setCurrLocation(String.valueOf(display));
                        product.setMetric(metric);
                        list.add(product);

                    }

                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
                Collections.sort(list, new Comparator<helper_liststore>() {
                    @Override
                    public int compare(helper_liststore o1, helper_liststore o2) {
//                        int i =0;
//                        if(Double.parseDouble(o1.getCurrLocation()) < Double.parseDouble(o2.getCurrLocation())){
//                            i = 0;
//                        }else {
//                            i = 1;
//                        }
                        return Double.parseDouble(o1.getCurrLocation()) < Double.parseDouble(o2.getCurrLocation()) ? -1 : Double.parseDouble(o1.getCurrLocation()) < Double.parseDouble(o2.getCurrLocation()) ? 1 : 0;
                    }
                });
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                lat = "" + location.getLatitude();
                lon = "" + location.getLongitude();
                startlat= location.getLatitude();
                startlon= location.getLongitude();
                endlat=14.708621680206571;
                endlon=120.99395285888656;

                float[] result = new float[1];
                Location.distanceBetween(startlat,startlon,endlat,endlon,result);
                float distance = result[0];
                int kl = (int)(distance/1000);
                Log.i("response:", String.valueOf(kl));
                Log.i("etokunin mo:", lat+"-"+ lon);


                //Log.i("response:", location.getLatitude() + "," + location.getLongitude());

            }
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {

        }

        @Override
        public void onFlushComplete(int requestCode) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

    }
    public void get_Myloc() {
        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        if (!gps_enable && !network_enable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("Attention");
            builder.setMessage("Enable,you GPS services...");
            builder.create().show();
        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }
        if(network_enable){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            startlat= location.getLatitude();
            startlon= location.getLongitude();

        }

    }
    private  boolean checkLocationPermission(){
        int location = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermission = new ArrayList<>();
        if(location != PackageManager.PERMISSION_GRANTED){

            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),listPermission.toArray(new String[listPermission.size()]),1);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item3,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        SharedPreferences preferences = list_store.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.Account){

            SharedPreferences preferences1 = this.getSharedPreferences("user", Context.MODE_PRIVATE);
            Intent intent = new Intent(list_store.this,signupstaff.class);
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
            Intent intent = new Intent(list_store.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
            finishAffinity();
        }

        return true;
    }
}
