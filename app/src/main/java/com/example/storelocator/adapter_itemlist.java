package com.example.storelocator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter_itemlist extends RecyclerView.Adapter<adapter_itemlist.MyViewHolder> {

    Context context;
    FirebaseStorage storage;

    ArrayList<helper_product> list;

    public adapter_itemlist(Context context, ArrayList<helper_product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item2,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_product product = list.get(position);
        holder.itemName.setText(product.getParoductName());
        holder.itemID.setText(product.getItemID());
        holder.addCount.setText("AddToCart Count: "+product.getProductview()+" times");

        //StorageReference gsReference = storage.getReferenceFromUrl("gs://storelocator-c908a.appspot.com/1643612433037.jpg");
        Picasso.get().load(product.getProductImage()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,updateProduct.class);
                intent.putExtra("itemid",product.getItemID());
                intent.putExtra("itemname",product.getParoductName());
                intent.putExtra("productimage",product.getProductImage());
                intent.putExtra("category",product.getCategory());

                intent.putExtra("pricesm",product.getPricesm());
                intent.putExtra("pricemd",product.getPricemd());
                intent.putExtra("pricelg",product.getPricelg());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemName1,itemID,addCount;
        ImageView itemImage;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameList);
            itemName1 = itemView.findViewById(R.id.storeListName);
            itemImage = itemView.findViewById(R.id.imageShow);
            itemID = itemView.findViewById(R.id.itemid);
            addCount = itemView.findViewById(R.id.addCount);
        }
    }
}
