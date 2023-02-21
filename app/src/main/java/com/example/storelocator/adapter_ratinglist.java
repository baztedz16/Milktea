package com.example.storelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adapter_ratinglist extends RecyclerView.Adapter<adapter_ratinglist.MyViewHolder> {

    Context context;
    FirebaseStorage storage;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");

    ArrayList<helper_review> list;

    public adapter_ratinglist(Context context, ArrayList<helper_review> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_storeratings, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        helper_review reviews = list.get(position);
//        holder.username.setText(reviews.getUser());
        holder.username.setText(reviews.getOrder_date());
//        holder.date.setText(reviews.getOrder_date());
        holder.date.setText(reviews.getComment());
//        holder.comment.setText(reviews.getComment() +"  \n"+ reviews.getRatingtype()+" Rating" );
        holder.comment.setText(reviews.getRatingtype()+" Rating" );
        holder.storerating.setRating((float) Double.parseDouble(reviews.getRating_count()));
        holder.storerating.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,date,comment;
        RatingBar storerating;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.date);
            comment = itemView.findViewById(R.id.comment);
            storerating = itemView.findViewById(R.id.storerating);
        }
    }
}
