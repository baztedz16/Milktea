package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class adapter_user_order extends RecyclerView.Adapter<adapter_user_order.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_order_rider> list;
    public adapter_user_order(Context context, ArrayList<helper_order_rider> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_order_rider_adapter, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_order_rider order = list.get(position);
        //holder.getOrder.setVisibility(View.INVISIBLE);
        holder.storeName.setText(order.getOrder_total());
        holder.orderid.setText(order.getOrder_id());
        holder.address.setText(order.getAddress());
        holder.store.setText(order.getStore());
        holder.getOrder.setText("View Order");
        String orderid = (String) holder.orderid.getText();

        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");

        if(order.getStatus().equals("10")){
            holder.getOrder.setText("Cancel");
            holder.getOrder.setEnabled(false);
        }

        if(order.getStatus().equals("0")){
            if(accountype.equals("User")){
                holder.cancel.setVisibility(View.VISIBLE);
            }else{
                holder.cancel.setVisibility(View.INVISIBLE);
            }

        }else{
            holder.cancel.setVisibility(View.INVISIBLE);
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("orders").child(order.getOrder_id());
                //reference.setValue("sample");
                reference.child("status").setValue("10");
            }
        });

        holder.getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context,order_details.class);
                intent2.putExtra("orderid",orderid);
                context.startActivity(intent2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView storeName,orderid,address,store;
        Button getOrder,cancel;
        ImageView itemImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            storeName = itemView.findViewById(R.id.riderStore);
            orderid = itemView.findViewById(R.id.orderID);
            itemImage = itemView.findViewById(R.id.imageShow);
            getOrder = itemView.findViewById(R.id.getOrder);
            address = itemView.findViewById(R.id.Addressds);
            store = itemView.findViewById(R.id.Storeds);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}
