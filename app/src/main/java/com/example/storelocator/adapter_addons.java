package com.example.storelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_addons extends RecyclerView.Adapter<adapter_addons.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_product> list;

    public adapter_addons(Context context, ArrayList<helper_product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.add_ons_adapter, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_product addons = list.get(position);

        SharedPreferences sh = context.getSharedPreferences("currentOrder", context.MODE_PRIVATE);

        String orderid = sh.getString("orderid", "");
        String value = sh.getString("value", "");
        holder.switchoption.setText(addons.getParoductName());


        holder.switchoption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.switchoption.isChecked()){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("cart").child(orderid);
                    //reference.setValue("sample");

                    int pricenew = Integer.parseInt(value)+Integer.parseInt(addons.getPrice());
                    reference.child("price").setValue(String.valueOf(pricenew));
                }else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("cart").child(orderid);
                    //reference.setValue("sample");

                    int pricenew = Integer.parseInt(value)-Integer.parseInt(addons.getPrice())+Integer.parseInt(addons.getPrice());
                    reference.child("price").setValue(String.valueOf(pricenew));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Switch switchoption;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            switchoption = itemView.findViewById(R.id.switch1);
        }
    }
}
