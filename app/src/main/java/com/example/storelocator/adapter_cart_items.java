package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_cart_items extends RecyclerView.Adapter<adapter_cart_items.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_cart> list;

    public adapter_cart_items(Context context, ArrayList<helper_cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_itemordered, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_cart product = list.get(position);
        holder.itemName.setText(product.getItmname()+" ("+product.getSize()+")");
        holder.itemID.setText(product.getCartid());
        holder.storeListName.setText(product.getOwner());
        holder.qty.setText(product.getQty() + "  PC/S");
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String rider = preferences.getString("username","");
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");


        if(accountype.equals("User")){
            holder.itemrating.setVisibility(View.VISIBLE);
                if(product.getOrderstatus().equals("5")){
                    holder.itemrating.setVisibility(View.VISIBLE);
                }else{
                    holder.itemrating.setVisibility(View.INVISIBLE);
                }
        }else{
            holder.itemrating.setVisibility(View.INVISIBLE);
        }
        holder.itemrating.setRating((float) Double.parseDouble(product.getItemrating()));

        double priceTotal = Integer.parseInt(product.getQty()) * Integer.parseInt(product.getPrice());
        holder.price.setText(String.valueOf(priceTotal)  + "   PHP");

        holder.itemrating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart").child(product.cartid);
                reference.child("itemrating").setValue(String.valueOf(holder.itemrating.getRating()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView storeListName,itemName,itemName1,itemID,qty,price;
        RatingBar itemrating;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            storeListName = itemView.findViewById(R.id.storeListName);
            itemName = itemView.findViewById(R.id.itemNameList);
            //itemName1 = itemView.findViewById(R.id.storeListName);
            itemID = itemView.findViewById(R.id.itemid);
            qty = itemView.findViewById(R.id.qty);
            price  = itemView.findViewById(R.id.price);
            itemrating = itemView.findViewById(R.id.itemrating);
        }
    }
}
