package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        holder.addselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //commit prefs on change
                if(isChecked){
                    System.out.println(" is ding true");
                }else{
                    System.out.println(" is ding false");
                }
            }
        });
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemName1,itemID,owner,qty,price;
        Switch addselect ;
        Button remove,add;
        ImageView itemImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameList);
            itemName1 = itemView.findViewById(R.id.storeListName);
            itemImage = itemView.findViewById(R.id.imageShow);
            itemID = itemView.findViewById(R.id.itemid);
            qty = itemView.findViewById(R.id.qty);
            price  = itemView.findViewById(R.id.price);
            addselect = itemView.findViewById(R.id.switch1);
            //owner= itemView.findViewById(R.id.ownerId);
            remove= itemView.findViewById(R.id.remove);
            add= itemView.findViewById(R.id.add);
        }
    }
}
