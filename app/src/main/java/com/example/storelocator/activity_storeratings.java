package com.example.storelocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class activity_storeratings extends AppCompatActivity {
    TextView storename;
    RatingBar storerating;
    ArrayList<helper_review> list;
    adapter_ratinglist myAdapter;
    int ii = 0;
    ArrayList<String> list2;
    RecyclerView ratinglist;

    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storerating);
        storename = findViewById(R.id.storename);
        storerating = findViewById(R.id.storerating);
        ratinglist = findViewById(R.id.ratinglist);

        storerating.setEnabled(false);


        ratinglist.setHasFixedSize(true);
        ratinglist.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_ratinglist(this,list);
        ratinglist.setAdapter(myAdapter);




        storename.setText(getIntent().getStringExtra("storeSelect"));
        //view product listed to the mainframe

        defaultview();


    }



    public void defaultview(){
        Query query1=reference.child("reviews").orderByChild("store").startAt(storename.getText().toString()).endAt(storename.getText().toString()+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",storename.getText().toString());
                double totalrating = 0.0;
                int reviewcount = 0;
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R","4");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_review review = snapshot.getValue(helper_review.class);
                        if(review.getRatingtype().equals("Store")){
                            list.add(review);
                            totalrating = totalrating+ Double.parseDouble(review.getRating_count());
                            reviewcount=reviewcount+1;
                        }

                    }
                    storerating.setRating((float) totalrating/5);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("error at default:","6"+getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}