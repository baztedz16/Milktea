package com.example.storelocator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
import java.util.Collections;

public class adapter_cart extends RecyclerView.Adapter<adapter_cart.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_cart> list;

    public adapter_cart(Context context, ArrayList<helper_cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_addtocart, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_cart product = list.get(position);
        holder.itemName.setText(product.getItmname());
        holder.size.setText("  ("+product.getSize()+")");
        holder.itemID.setText(product.getCartid());
        holder.itemName1.setText(product.getOwner());
        holder.price.setText(product.getPrice());

        if(product.getQty() != null){
            holder.qty.setText(product.getQty());
        }else{
            holder.qty.setText("1");
        }


        //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        Picasso.get().load(product.getImg()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            private Object mainframe_viewcart;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,updateProduct.class);
                intent.putExtra("total",holder.qty.getText());

                /*Intent intent = new Intent(context,updateProduct.class);
                intent.putExtra("itemid",product.getItemID());
                intent.putExtra("itemname",product.getParoductName());
                intent.putExtra("productimage",product.getProductImage());
                context.startActivity(intent);*/

            }
        });
//        holder.addselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                //commit prefs on change
//                if(isChecked){
//                    System.out.println(" is ding true");
//                }else{
//                    System.out.println(" is ding false");
//                }
//            }
//        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart");
                Query query = reference.orderByChild("cartid").equalTo(holder.itemID.getText().toString());
                //reference.setValue("sample");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(Integer.parseInt(holder.qty.getText().toString()) <= 1){
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                                Toast.makeText(context,"Item Removed",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) -1));
                            String itemID = holder.itemID.getText().toString();
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("cart").child(itemID);
                            //reference.setValue("sample");


                            reference.child("qty").setValue(holder.qty.getText().toString());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) +1));
                String itemID = holder.itemID.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("cart").child(itemID);
                //reference.setValue("sample");


                reference.child("qty").setValue(holder.qty.getText().toString());
            }
        });
        holder.addons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("currentOrder",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("orderid",product.getCartid());
                editor.putString("value",product.getPrice());
                editor.commit();
                DialogFragment newFragment = addons_dialogfragment.newInstance(
                        R.string.alert_dialog_two_buttons_title);

                newFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), "dialog");
            }
        });
    }
//    public void defaultview(){
//        SharedPreferences sh = context.getSharedPreferences("user", context.MODE_PRIVATE);
//
//        String storename = sh.getString("store", "");
//        Query query1=reference.child("products").orderByChild("store").startAt(storename).endAt(storename+"\uf8ff");
//        query1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.i("R",storename);
//                double totalrating = 0.0;
//                int reviewcount = 0;
//                if (dataSnapshot.exists()) {
//                    list2.clear();
//                    Log.i("R","4");
//
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        helper_product addonss = snapshot.getValue(helper_product.class);
//                        list2.add(addonss);
//
//
//                    }
//                    Collections.reverse(list2);
//                    myAdapter.notifyDataSetChanged();
//                }else{
//                    //Log.i("error at default:","6"+getIntent().getStringExtra("storeName"));
//                    //Log.i("R",searchtext);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemName1,itemID,owner,qty,price,size;
        Switch addselect ;
        Button remove,add,addons;
        ImageView itemImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            addons = itemView.findViewById(R.id.addons);
            itemName = itemView.findViewById(R.id.itemNameList);
            itemName1 = itemView.findViewById(R.id.storeListName);
            itemImage = itemView.findViewById(R.id.imageShow);
            itemID = itemView.findViewById(R.id.itemid);
            qty = itemView.findViewById(R.id.qty);
            price  = itemView.findViewById(R.id.price);
            //owner= itemView.findViewById(R.id.ownerId);
            remove= itemView.findViewById(R.id.remove);
            add= itemView.findViewById(R.id.add);
            size = itemView.findViewById(R.id.size);
        }
    }
}
