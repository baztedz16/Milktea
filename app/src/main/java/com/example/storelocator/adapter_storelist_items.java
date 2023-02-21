package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class adapter_storelist_items extends RecyclerView.Adapter<adapter_storelist_items.MyViewHolder>{
    Context context;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    SharedPreferences preferences;

    ArrayList<helper_product> list;

    public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    double startlat,startlon,endlat,endlon;
    private boolean gps_enable = false;
    private boolean network_enable = false;
    Double distance;


    public adapter_storelist_items(Context context, ArrayList<helper_product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public adapter_storelist_items.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //get_Myloc();
        checkLocationPermission();
        //context = parent.getContext();
        return  new adapter_storelist_items.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_storelist_items.MyViewHolder holder, int position) {


        helper_product product = list.get(position);
        holder.itemName.setText(product.getParoductName());
        holder.storename.setText(product.getStoreOwner());
        holder.address.setText(product.getAddress());
        holder.price.setText(product.getDescription());
        //variant price s,m,l
        String ssize = String.valueOf(Integer.parseInt( product.getPricesm())+10);
        String msize = String.valueOf(Integer.parseInt( product.getPricemd())+10);
        String lsize = String.valueOf(Integer.parseInt( product.getPricelg())+10);


        holder.textpricesm.setText(ssize);
        holder.textpricemd.setText(msize);
        holder.textpricelg.setText(lsize);

        if(product.getPricesm().equals("0")){
            holder.small.setVisibility(View.INVISIBLE);
            holder.textpricesm.setVisibility(View.INVISIBLE);
        }else{
            holder.small.setVisibility(View.VISIBLE);
            holder.textpricesm.setVisibility(View.VISIBLE);
        }
        if(product.getPricemd().equals("0")){
            holder.medium.setVisibility(View.INVISIBLE);
            holder.textpricemd.setVisibility(View.INVISIBLE);
        }else{
            holder.medium.setVisibility(View.VISIBLE);
            holder.textpricemd.setVisibility(View.VISIBLE);
        }
        if(product.getPricelg().equals("0")){
            holder.large.setVisibility(View.INVISIBLE);
            holder.textpricelg.setVisibility(View.INVISIBLE);
        }else{
            holder.large.setVisibility(View.VISIBLE);
            holder.textpricelg.setVisibility(View.VISIBLE);
        }

        get_Myloc();

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
        Log.i("etokunin mo:", startlat+"-"+ startlon);

        holder.distance.setText(String.valueOf(display)+metric);


        //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        Picasso.get().load(product.getProductImage()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,storeSelected.class);
                intent.putExtra("storeSelect",product.getStoreOwner());
                intent.putExtra("destlong",startlat+","+startlon);
                Log.i("sample",product.getParoductName());
                context.startActivity(intent);

            }
        });

        holder.small.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Small " + product.getParoductName()+" Added",Toast.LENGTH_SHORT).show();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart");
                String id = reference.push().getKey();
                String img = product.getProductImage();
                String itmname= product.getParoductName();
                String owner = product.getStoreOwner();
                String sotreUser = product.getStoreUser(); //StoreUser
                String price = String.valueOf(Integer.parseInt(product.getPricesm())+10); //StoreUser
                int productview = product.getProductview();
                //reference.setValue("sample");
                preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
                final String username = preferences.getString("username","");

                //helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,address);
                reference.child(id).child("cartid").setValue(id);
                reference.child(id).child("product").setValue(product.itemID);
                reference.child(id).child("username").setValue(username);
                reference.child(id).child("img").setValue(img);
                reference.child(id).child("itmname").setValue(itmname);
                reference.child(id).child("owner").setValue(owner);
                reference.child(id).child("delete").setValue("0");
                reference.child(id).child("price").setValue(price);
                reference.child(id).child("size").setValue("S");
                reference.child(id).child("qty").setValue("1");
                reference.child(id).child("orderstatus").setValue("0");
                reference.child(id).child("itemrating").setValue("0");
                //rootNode.getReference("products").child(product.getItemID()).child("view").setValue(1);
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query1 = reference1.child("products").orderByChild("itemID").equalTo(product.getItemID());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(product.getItemID()).child("productview").getValue().toString();
                            rootNode.getReference("products").child(product.getItemID()).child("productview").setValue(Integer.parseInt(view)+1);

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                final String storename = sotreUser;
                //final String hr = id;
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query = reference2.child("users").orderByChild("storename").equalTo(owner);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(storename).child("view").getValue().toString();
                            rootNode.getReference("users").child(storename).child("view").setValue(String.valueOf(Integer.parseInt(view)+1));

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //rootNode.getReference("users").child(owner).child("view").setValue();
                //Toast.makeText(context,"Item Added",Toast.LENGTH_SHORT).show();
            }
        });
        holder.medium.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Medium " + product.getParoductName()+" Added",Toast.LENGTH_SHORT).show();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart");
                String id = reference.push().getKey();
                String img = product.getProductImage();
                String itmname= product.getParoductName();
                String owner = product.getStoreOwner();
                String sotreUser = product.getStoreUser(); //StoreUser
                String price = String.valueOf(Integer.parseInt(product.getPricemd())+10); //StoreUser
                int productview = product.getProductview();
                //reference.setValue("sample");
                preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
                final String username = preferences.getString("username","");

                //helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,address);
                reference.child(id).child("cartid").setValue(id);
                reference.child(id).child("product").setValue(product.itemID);
                reference.child(id).child("username").setValue(username);
                reference.child(id).child("img").setValue(img);
                reference.child(id).child("itmname").setValue(itmname);
                reference.child(id).child("owner").setValue(owner);
                reference.child(id).child("delete").setValue("0");
                reference.child(id).child("size").setValue("M");
                reference.child(id).child("price").setValue(price);
                reference.child(id).child("qty").setValue("1");
                reference.child(id).child("orderstatus").setValue("0");
                reference.child(id).child("itemrating").setValue("0");
                //rootNode.getReference("products").child(product.getItemID()).child("view").setValue(1);
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query1 = reference1.child("products").orderByChild("itemID").equalTo(product.getItemID());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(product.getItemID()).child("productview").getValue().toString();
                            rootNode.getReference("products").child(product.getItemID()).child("productview").setValue(Integer.parseInt(view)+1);

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                final String storename = sotreUser;
                //final String hr = id;
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query = reference2.child("users").orderByChild("storename").equalTo(owner);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(storename).child("view").getValue().toString();
                            rootNode.getReference("users").child(storename).child("view").setValue(String.valueOf(Integer.parseInt(view)+1));

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //rootNode.getReference("users").child(owner).child("view").setValue();
                //Toast.makeText(context,"Item Added",Toast.LENGTH_SHORT).show();
            }
        });
        holder.large.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Large " + product.getParoductName()+" Added",Toast.LENGTH_SHORT).show();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart");
                String id = reference.push().getKey();
                String img = product.getProductImage();
                String itmname= product.getParoductName();
                String owner = product.getStoreOwner();
                String sotreUser = product.getStoreUser(); //StoreUser
                String price = String.valueOf(Integer.parseInt(product.getPricelg())+10); //StoreUser
                int productview = product.getProductview();
                //reference.setValue("sample");
                preferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
                final String username = preferences.getString("username","");

                //helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,address);
                reference.child(id).child("cartid").setValue(id);
                reference.child(id).child("product").setValue(product.itemID);
                reference.child(id).child("username").setValue(username);
                reference.child(id).child("img").setValue(img);
                reference.child(id).child("itmname").setValue(itmname);
                reference.child(id).child("owner").setValue(owner);
                reference.child(id).child("delete").setValue("0");
                reference.child(id).child("size").setValue("L");
                reference.child(id).child("price").setValue(price);
                reference.child(id).child("qty").setValue("1");
                reference.child(id).child("orderstatus").setValue("0");
                reference.child(id).child("itemrating").setValue("0");
                //rootNode.getReference("products").child(product.getItemID()).child("view").setValue(1);
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query1 = reference1.child("products").orderByChild("itemID").equalTo(product.getItemID());
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(product.getItemID()).child("productview").getValue().toString();
                            rootNode.getReference("products").child(product.getItemID()).child("productview").setValue(Integer.parseInt(view)+1);

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                final String storename = sotreUser;
                //final String hr = id;
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
                Query query = reference2.child("users").orderByChild("storename").equalTo(owner);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String view = dataSnapshot.child(storename).child("view").getValue().toString();
                            rootNode.getReference("users").child(storename).child("view").setValue(String.valueOf(Integer.parseInt(view)+1));

                            Log.i("R","data:"+view);

                        }else{
                            Log.i("R","6");
                            //Log.i("R",searchtext);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //rootNode.getReference("users").child(owner).child("view").setValue();
                //Toast.makeText(context,"Item Added",Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,storename,address,distance,price,textpricesm,textpricemd,textpricelg;
        Button small,medium,large;
        ImageView itemImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.productList);
            storename = itemView.findViewById(R.id.storeListName);
            itemImage = itemView.findViewById(R.id.imageView3);
            address = itemView.findViewById(R.id.storeListAdd);
            distance = itemView.findViewById(R.id.distance);
            price = itemView.findViewById(R.id.price);
            textpricesm = itemView.findViewById(R.id.textpricesm);
            textpricemd = itemView.findViewById(R.id.textpricemd);
            textpricelg = itemView.findViewById(R.id.textpricelg);

            //buttons
            small = itemView.findViewById(R.id.small);
            medium = itemView.findViewById(R.id.medium);
            large = itemView.findViewById(R.id.large);
        }

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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Attention");
            builder.setMessage("Enable,you GPS services...");
            builder.create().show();
        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        int location = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermission = new ArrayList<>();
        if(location != PackageManager.PERMISSION_GRANTED){

            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions((Activity) context,listPermission.toArray(new String[listPermission.size()]),1);
        }
        return true;
    }

    private final double r2d = 180.0D / 3.141592653589793D;
    private final double d2r = 3.141592653589793D / 180.0D;
    private final double d2km = 111189.57696D * r2d;
    public double metersGet(double lt1, double ln1, double lt2, double ln2) {
        double x = lt1 * d2r;
        double y = lt2 * d2r;
        return Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
    }

}
