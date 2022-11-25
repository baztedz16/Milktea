package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_rider_payables extends RecyclerView.Adapter<adapter_rider_payables.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_order_rider> list;
    public adapter_rider_payables(Context context, ArrayList<helper_order_rider> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listpayables, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_order_rider order = list.get(position);
        holder.orderid.setText(order.getOrder_id());
        holder.amount.setText(order.getOrder_total());
        holder.date.setText(order.getDate_order());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView orderid,amount,date;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            orderid = itemView.findViewById(R.id.orderid);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);


        }
    }
}
