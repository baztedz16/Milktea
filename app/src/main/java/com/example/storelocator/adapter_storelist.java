package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_storelist extends  RecyclerView.Adapter<adapter_storelist.storeHolder>{
    Context context;
    FirebaseStorage storage;
    ArrayList<helper_liststore> list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    RecyclerView recyclerView;


    public adapter_storelist(Context context, ArrayList<helper_liststore> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public storeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_storehelper,parent,false);
        return  new storeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_storelist.storeHolder holder, int position) {
        helper_liststore store = list.get(position);
        holder.itemName.setText(store.getStorename());
        holder.storeAddress.setText(store.getAddress());

        //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        Picasso.get().load(store.getImage()).into(holder.itemImage);


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("store",store.getStorename());
                editor.commit();
                Intent intent = new Intent(context,mainframe.class);
                intent.putExtra("storeName",store.getStorename());
                context.startActivity(intent);
            }
        });
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = context.getSharedPreferences("user",context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("store",store.getStorename());
                editor.commit();
                Intent intent = new Intent(context,mainframe.class);
                intent.putExtra("storeName",store.getStorename());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class storeHolder extends RecyclerView.ViewHolder{
        TextView itemName,storeAddress;
        ImageView itemImage;
        public storeHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.storeListName);
            storeAddress = itemView.findViewById(R.id.storeListAdd);
            itemImage = itemView.findViewById(R.id.imageView3);
        }
    }
}
