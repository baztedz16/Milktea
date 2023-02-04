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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;

public class activity_storeratings extends AppCompatActivity {
    TextView storename,storerate;
    RatingBar storerating;
    ArrayList<helper_review> list;
    adapter_ratinglist myAdapter;
    int ii = 0;
    ArrayList<String> list2;
    RecyclerView ratinglist;
    Spinner sortingspinner;

    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storerating);
        storename = findViewById(R.id.storename);
        storerating = findViewById(R.id.storerating);
        ratinglist = findViewById(R.id.ratinglist);
        sortingspinner = findViewById(R.id.SortSpin);
        storerating.setEnabled(false);
        storerate = findViewById(R.id.ScoreRate);


        ratinglist.setHasFixedSize(true);
        ratinglist.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_ratinglist(this,list);
        ratinglist.setAdapter(myAdapter);
        Spinner spinner= (Spinner) findViewById(R.id.SortSpin);
        ArrayAdapter<String> list = new ArrayAdapter<String>(activity_storeratings.this
                ,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sorting_rating));
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list);


        storename.setText(getIntent().getStringExtra("storeSelect"));

        //view product listed to the mainframe

        defaultview();

        sortingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sorting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                            list.add(review);
                            totalrating = totalrating+ Double.parseDouble(review.getRating_count());
                            reviewcount=reviewcount+1;

                    }
                    storerating.setRating((float) totalrating/reviewcount);
                    Collections.reverse(list);
                    myAdapter.notifyDataSetChanged();
                    storerate.setText("("+String.valueOf(totalrating/reviewcount)+")");
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
    private void sorting(){
       if(sortingspinner.getSelectedItemId() == 1){
           Collections.sort(list, new Comparator<helper_review>() {
               @Override
               public int compare(helper_review o1, helper_review o2) {
                   return Double.parseDouble(o1.getRating_count()) < Double.parseDouble(o2.getRating_count()) ? -1 : Double.parseDouble(o1.getRating_count()) < Double.parseDouble(o2.getRating_count()) ? 1 : 0;
               }
           });
       }else {
           Collections.sort(list, new Comparator<helper_review>() {
               @Override
               public int compare(helper_review o1, helper_review o2) {
                   return Double.parseDouble(o1.getRating_count()) > Double.parseDouble(o2.getRating_count()) ? -1 : Double.parseDouble(o1.getRating_count()) > Double.parseDouble(o2.getRating_count()) ? 1 : 0;
               }
           });
       }
        myAdapter.notifyDataSetChanged();
    }


}