package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_rider_order extends RecyclerView.Adapter<adapter_rider_order.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_order_rider> list;
    public adapter_rider_order(Context context, ArrayList<helper_order_rider> list) {
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

        if(order.getStatus().equals("5") || order.getStatus().equals("4") || order.getStatus().equals("3") || order.getStatus().equals("1")){
            holder.cancel.setVisibility(View.INVISIBLE);

        }
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String cty = preferences.getString("accountype","");
        if(order.getStatus().equals("0")) {
            if (cty.equals("User")) {
                holder.cancel.setVisibility(View.VISIBLE);
            } else {
                holder.cancel.setVisibility(View.INVISIBLE);
            }
        }
        //holder.itemName1.setText(product.getOwner());

        /*if(product.getQty() != null){
            holder.qty.setText(product.getQty());
        }else{
            holder.qty.setText("1");
        }*/


        //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        /*Picasso.get().load(product.getImg()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            private Object mainframe_viewcart;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,updateProduct.class);
                intent.putExtra("total",holder.qty.getText());

                *//*Intent intent = new Intent(context,updateProduct.class);
                intent.putExtra("itemid",product.getItemID());
                intent.putExtra("itemname",product.getParoductName());
                intent.putExtra("productimage",product.getProductImage());
                context.startActivity(intent);*//*

            }
        });*/
        String orderid = (String) holder.orderid.getText();


        Intent intent = ((rider_frame) context).getIntent();
        String rider = intent.getStringExtra("user");
        String accountype = intent.getStringExtra("accountype");
        holder.getOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("orders").child(orderid);
                //reference.setValue("sample");
                if(accountype.equals("Rider")){
                    reference.child("status").setValue("2");
                    reference.child("rider").setValue(rider);
                    Toast.makeText(context,"Order: "+ holder.orderid.getText().toString()+" Succesfully Process",Toast.LENGTH_SHORT).show();
                }else{
                    reference.child("status").setValue("1");
                    reference.child("rider").setValue("");
                    Toast.makeText(context,"Order: "+ holder.orderid.getText().toString()+" Succesfully Added",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView storeName,orderid,address,store,cancel;
        Button getOrder;
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
