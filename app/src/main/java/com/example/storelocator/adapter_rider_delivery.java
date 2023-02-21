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

public class adapter_rider_delivery extends RecyclerView.Adapter<adapter_rider_delivery.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_order_rider> list;
    public adapter_rider_delivery(Context context, ArrayList<helper_order_rider> list) {
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
        holder.storeName.setText(order.getOrder_total());
        holder.orderid.setText(order.getOrder_id());
        holder.address.setText(order.getAddress());
        holder.store.setText(order.getStore());
        holder.getOrder.setText("Order Details");
        holder.dateTxt.setText(order.getDate_order());


        String orderid = (String) holder.orderid.getText();
        Intent intent = ((rider_frame) context).getIntent();
        String rider = intent.getStringExtra("user");

        if(order.getStatus().equals("5") || order.getStatus().equals("4") || order.getStatus().equals("3") || order.getStatus().equals("2") || order.getStatus().equals("1")){
            holder.cancel.setVisibility(View.INVISIBLE);

        }
        holder.getOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context,order_details.class);
                intent2.putExtra("orderid",order.getOrder_id());
                intent2.putExtra("orderid",order.getOrder_id());
                intent2.putExtra("total",order.getOrder_total());
                context.startActivity(intent2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView storeName,orderid,address,store,dateTxt;
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
            dateTxt = itemView.findViewById(R.id.dateTxt);


        }
    }
}
