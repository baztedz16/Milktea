package com.example.storelocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class rating_store extends AppCompatActivity {

    Button accept,submit,servicebtn;
    TextView orderid,storename;
    String user;

    SharedPreferences preferences;
    RatingBar ratingBar,ratingBar6;
    EditText commentTv,servicecomment;


    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_store);
        orderid = findViewById(R.id.orderId);
        storename = findViewById(R.id.storename);
        accept = findViewById(R.id.accept);
        submit = findViewById(R.id.submit);
        ratingBar=findViewById(R.id.ratingBar3);
        ratingBar6=findViewById(R.id.ratingBar6);
        servicebtn=findViewById(R.id.servicebtn);
        commentTv=findViewById(R.id.comment);
        servicecomment=findViewById(R.id.servicecomment);

        orderid.setText(getIntent().getStringExtra("orderid"));
        storename.setText(getIntent().getStringExtra("store"));
        user = getIntent().getStringExtra("user");
        getReviewStore();
        getReviewService();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(rating_store.this);
                builder.setMessage("Accept order without Rating?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("reviews");
                                String id = orderid.getText().toString()+"Store";
                                String user = getIntent().getStringExtra("user");
                                String comment = commentTv.getText().toString();
                                String ratingtype = "Store";
                                String rating_count = String.valueOf(ratingBar.getRating());
                                String order_date = getIntent().getStringExtra("orderdate");
                                String store_rated = storename.getText().toString();

                                //helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,address);
                                reference.child(id).child("orderid").setValue(id);
                                reference.child(id).child("user").setValue(user);
                                reference.child(id).child("comment").setValue(comment);
                                reference.child(id).child("ratingtype").setValue(ratingtype);
                                reference.child(id).child("rating_count").setValue(rating_count);
                                reference.child(id).child("order_date").setValue(order_date);
                                reference.child(id).child("store").setValue(store_rated);
                                Toast.makeText(getApplicationContext(),"Order: "+ id+" Rating Submitted",Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(rating_store.this);
                builder.setMessage("Submit rating and Aceept Order?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        servicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("reviews");
                                String id = orderid.getText().toString()+"Service";
                                String user = getIntent().getStringExtra("user");
                                String comment = servicecomment.getText().toString();
                                String ratingtype = "Service";
                                String rating_count = String.valueOf(ratingBar6.getRating());
                                String order_date = getIntent().getStringExtra("orderdate");
                                String store_rated = storename.getText().toString();

                                //helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,address);
                                reference.child(id).child("orderid").setValue(id);
                                reference.child(id).child("user").setValue(user);
                                reference.child(id).child("comment").setValue(comment);
                                reference.child(id).child("ratingtype").setValue(ratingtype);
                                reference.child(id).child("rating_count").setValue(rating_count);
                                reference.child(id).child("order_date").setValue(order_date);
                                reference.child(id).child("store").setValue(store_rated);
                                Toast.makeText(getApplicationContext(),"Order: "+ id+" Service Rating Submitted",Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(rating_store.this);
                builder.setMessage("Submit rating For Service Revice").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    public void getReviewStore(){
        Query query1 = reference.child("reviews").orderByChild("orderid").equalTo(orderid.getText().toString()+"Store");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("R", "OrderedItems");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        try {
                            helper_review review = snapshot.getValue(helper_review.class);
                            ratingBar.setRating((float) Double.parseDouble(review.getRating_count()));
                            commentTv.setText(review.getComment());
                        }catch (Exception e)
                        {
                            Log.i("Error:",e.toString());
                        }

                    }
                } else {
                    Log.i("error at default:", "6" + getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getReviewService(){
        Query query1 = reference.child("reviews").orderByChild("orderid").equalTo(orderid.getText().toString()+"Service");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("R", "OrderedItems");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            helper_review review = snapshot.getValue(helper_review.class);
                            ratingBar6.setRating((float) Double.parseDouble(review.getRating_count()));
                            servicecomment.setText(review.getComment());
                        }catch (Exception e)
                        {
                            Log.i("Error:",e.toString());
                        }

                    }
                } else {
                    Log.i("error at default:", "6" + getIntent().getStringExtra("storeName"));
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

