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
import android.widget.ImageView;
import android.widget.TextView;

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

public class adapter_storelist extends  RecyclerView.Adapter<adapter_storelist.storeHolder>{
    Context context;
    FirebaseStorage storage;
    ArrayList<helper_liststore> list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    RecyclerView recyclerView;



    public adapter_storelist(Context context, ArrayList<helper_liststore> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public storeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_storehelper,parent,false);

        return  new storeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_storelist.storeHolder holder, int position) {
        helper_liststore store = list.get(position);
        holder.itemName.setText(store.getStorename());
        holder.storeAddress.setText(store.getAddress());




        holder.Distance.setText(store.getCurrLocation()+" "+store.getMetric());
                //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        Picasso.get().load(store.getImage()).into(holder.itemImage);


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("store",store.getStorename());
                editor.commit();
                Intent intent = new Intent(context,mainframe.class);
                intent.putExtra("storeName",store.getStorename());
                intent.putExtra("address",store.getAddress());
                intent.putExtra("lati",store.getDestlat());
                intent.putExtra("long",store.getDestlong());
                context.startActivity(intent);
            }
        });
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("store",store.getStorename());
                editor.commit();
                Intent intent = new Intent(context,mainframe.class);
                intent.putExtra("storeName",store.getStorename());
                intent.putExtra("address",store.getAddress());
                intent.putExtra("lati",store.getDestlat());
                intent.putExtra("long",store.getDestlong());
                context.startActivity(intent);
            }
        });
        Query query1=reference.child("reviews").orderByChild("store").startAt(store.getStorename()).endAt(store.getStorename()+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",store.getStorename());
                double totalrating = 0.0;
                int reviewcount = 0;
                if (dataSnapshot.exists()) {
                    Log.i("R","4");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_review review = snapshot.getValue(helper_review.class);
                        if(review.getRatingtype().equals("Store") && !review.getRating_count().equals("0")){
                            totalrating = totalrating+ Double.parseDouble(review.getRating_count());
                            reviewcount=reviewcount+1;
                        }

                    }
                    holder.Rating.setText("Ratings: " +String.valueOf((float) totalrating/reviewcount));
                }else{
                    //Log.i("error at default:","6"+getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public int getItemCount() {

        return list.size();
    }


    public static class storeHolder extends RecyclerView.ViewHolder{
        TextView itemName,storeAddress,Rating,Distance;
        ImageView itemImage;
        public storeHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.storeListName);
            storeAddress = itemView.findViewById(R.id.storeListAdd);
            itemImage = itemView.findViewById(R.id.imageView3);
            Rating = itemView.findViewById(R.id.Rating);
            Distance = itemView.findViewById(R.id.Distance);
        }
    }

}
