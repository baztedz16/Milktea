package com.example.storelocator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class storeSelected extends AppCompatActivity {
    TextView storeText,addressText,storeReach;
    Button locateAddress;
    ImageView storeImage;
    adapter_itemlist myAdapter;
    ArrayList<helper_product> list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    FirebaseDatabase rootNode;
    RecyclerView recyclerView;
    SharedPreferences preferences;

    public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    double startlat,startlon,endlat,endlon;
    private boolean gps_enable = false;
    private boolean network_enable = false;
    public Uri imageUri;
    public static final String KEY_STORE = "sstore";


    FirebaseStorage storage;
    StorageReference ref;



    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_selected);
        storeText = findViewById(R.id.selectedStore);
        addressText=findViewById(R.id.storeAddress);
        locateAddress=findViewById(R.id.add_btn);
        storeImage = findViewById(R.id.imageView2);
        storeReach = findViewById(R.id.storeReach);

        recyclerView = findViewById(R.id.selectstoreview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        storeText.setText(getIntent().getStringExtra("storeSelect"));
        /*addressText.setText(getIntent().getStringExtra("address"));
        String location = getIntent().getStringExtra("map_coor");
        Log.i("coor",location);*/

        list = new ArrayList<>();
        myAdapter = new adapter_itemlist(this,list);
        recyclerView.setAdapter(myAdapter);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        get_Myloc();
        checkLocationPermission();


        locateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String uri = "http://maps.google.com/maps?saddr=14.343517521140628, 120.84576260033143&daddr=" + "14.353236614753964,120.85965184459297";
                String uri = "http://maps.google.com/maps?saddr=" + getIntent().getStringExtra("destlong") + "&daddr=" +addressText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                Log.i("response:", uri);
                startActivity(intent);
            }
        });



        Query query = reference.child("products").orderByChild("storeOwner").equalTo(getIntent().getStringExtra("storeSelect"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product product = snapshot.getValue(helper_product.class);
                        list.add(product);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query2 = reference.child("users").orderByChild("storename").equalTo(getIntent().getStringExtra("storeSelect"));
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String address = snapshot.child("username").getValue().toString();
                        addressText.setText(snapshot.child("destlong").getValue().toString()+","+snapshot.child("destlat").getValue().toString());
                        storeReach.setText("Store Reache by: "+snapshot.child("view").getValue().toString()+" People");
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(storeImage);
                        System.out.println(address);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();

        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){
                    //Snackbar.make(findViewById(android.R.id.content),"Please add product image!!",Snackbar.LENGTH_SHORT).show();
                    uploadImage();
                }else{
                    chooseImage();
                }
            }
        });

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            storeImage.setImageURI(imageUri);


        }
    }
    private String getFileExt(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadImage() {
        // Create a reference to "mountains.jpg"
        final Long randomkey= System.currentTimeMillis();
        StorageReference mountainsRef = ref.child(String.valueOf(randomkey)+"."+getFileExt(imageUri));

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = ref.child("images/"+randomkey+"."+getFileExt(imageUri));

        rootNode = FirebaseDatabase.getInstance();

        //reference.setValue("sample");



        Toast.makeText(storeSelected.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());
        mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Snackbar.make(findViewById(android.R.id.content),"Image Uplaoded",Snackbar.LENGTH_SHORT).show();

                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        preferences=storeSelected.this.getSharedPreferences("user",Context.MODE_PRIVATE);
                        final String username = preferences.getString("username","");
                        reference = rootNode.getReference("users");
                        reference.child(username).child("image").setValue(uri.toString());
                        Log.i("user",username);
                        Log.i("user",uri.toString());

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(storeSelected.this);
            builder.setTitle("Attention");
            builder.setMessage("Enable,you GPS services...");
            builder.create().show();
        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        }

    }
    private  boolean checkLocationPermission(){
        int location = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermission = new ArrayList<>();
        if(location != PackageManager.PERMISSION_GRANTED){

            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),1);
        }
        return true;
    }
}
