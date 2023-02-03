package com.example.storelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
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

import java.util.ArrayList;

public class adapter_addons extends RecyclerView.Adapter<adapter_addons.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    String result = "";
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

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("addons");
        Query query = reference;
        //reference.setValue("sample");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.i("Addons",ds.child("addonsref").getValue().toString()+"  "+orderid+addons.getParoductName());
                    if(ds.child("addonsref").getValue().toString().equals(orderid+addons.getParoductName())){
                        result = "1";
                        holder.switchoption.setChecked(true);
                    }else{
                        holder.switchoption.setChecked(false);
                        result = "0";
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        holder.switchoption.setText(addons.getParoductName()+" (+"+addons.getPrice()+")");

        holder.switchoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.switchoption.isChecked()){
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("cart").child(orderid);
                    //reference.setValue("sample");

                    int pricenew = Integer.parseInt(sh.getString("value", ""))+Integer.parseInt(addons.getPrice());
                    reference.child("price").setValue(String.valueOf(pricenew));


                    //adding addons in firebase
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("addons").child(orderid+addons.getParoductName());
                    reference.child("addonsref").setValue(orderid+addons.getParoductName());
                    reference.child("addonsitem").setValue(addons.getParoductName());
                    reference.child("price").setValue(addons.getPrice());

                    SharedPreferences preferences;
                    SharedPreferences.Editor editor;

                    preferences = context.getSharedPreferences("currentOrder",context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("value",String.valueOf(pricenew));
                    editor.commit();
                }else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("cart").child(orderid);
                    //reference.setValue("sample");
                    int pricenew = Integer.parseInt(sh.getString("value", ""))-Integer.parseInt(addons.getPrice());
                    reference.child("price").setValue(String.valueOf(pricenew));

                    SharedPreferences preferences;
                    SharedPreferences.Editor editor;

                    preferences = context.getSharedPreferences("currentOrder",context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("value",String.valueOf(pricenew));
                    editor.commit();
                    //remove addons
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("addons");
                    Query query = reference.orderByChild("addonsref").equalTo(orderid+addons.getParoductName());
                    //reference.setValue("sample");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                                Toast.makeText(context,"Add ons remove Removed",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
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
