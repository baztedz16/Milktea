package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class adapter_storelist_category extends RecyclerView.Adapter<adapter_storelist_category.MyViewHolder>{
    Context context;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    SharedPreferences preferences;
    int selected_position = 0;
    ArrayList<String> list;



    public adapter_storelist_category(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public adapter_storelist_category.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_category,parent,false);
        return  new adapter_storelist_category.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_storelist_category.MyViewHolder holder, int position) {


        String product = list.get(position);
        holder.category.setText(product);
        holder.itemView.setBackgroundColor(selected_position == position ? Color.GREEN : Color.TRANSPARENT);

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == RecyclerView.NO_POSITION) return;


                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("selectionCat",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("cat",product);
                editor.commit();
//                SharedPreferences preferences = mainframe.this.getSharedPreferences("selectionCat", Context.MODE_PRIVATE);
//                String cat = preferences.getString("accountype","cat");
                // Updating old as well as new positions
                notifyItemChanged(selected_position);
                selected_position = position;
                notifyItemChanged(selected_position);



            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            category = itemView.findViewById(R.id.category);
        }

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
