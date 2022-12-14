package com.example.storelocator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class updateProduct extends AppCompatActivity {
    EditText productid,productname;
    Button updateitem,deleteitem;
    String sstore;
    ListView listview;
    ImageView productImg;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    RecyclerView recyclerView;
    Spinner categoryspin;
    RatingBar itemrating;


    public Uri imageUri;
    public static final String KEY_STORE = "sstore";
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product);
        productid = findViewById(R.id.itemID);
        productname = findViewById(R.id.UpdateitemName);
        productImg = findViewById(R.id.updateImage);
        categoryspin = findViewById(R.id.categoryspin);
        itemrating= findViewById(R.id.itemrating);

        updateitem = findViewById(R.id.updatebtn);
        deleteitem = findViewById(R.id.deletebtn);
        defaultview();



        //string here is the value when you load the page
        productid.setText(getIntent().getStringExtra("itemid"));
        productname.setText(getIntent().getStringExtra("itemname"));

        Picasso.get().load(getIntent().getStringExtra("productimage")).into(productImg);
        //address.setText(getIntent().getStringExtra("address"));


        updateitem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String itemID = productid.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("products").child(itemID);
                //reference.setValue("sample");
                reference.child("paroductName").setValue(productname.getText().toString());
                reference.child("category").setValue(categoryspin.getSelectedItem().toString());
                Toast.makeText(getApplicationContext(),"Item: "+ productname.getText().toString()+" Updated",Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences preferences = updateProduct.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String ridername = preferences.getString("username","");
        String storename = preferences.getString("Store","");
        reference.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String cat = areaSnapshot.child("store").getValue(String.class);
                    if(cat.equals(storename)){
                        areas.add(areaSnapshot.child("categoryname").getValue(String.class));
                    }

                }


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(updateProduct.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryspin.setAdapter(areasAdapter);
                categoryspin.setSelection(((ArrayAdapter<String>)categoryspin.getAdapter()).getPosition(getIntent().getStringExtra("category")));



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemID = productid.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("products");
                Query query = reference.orderByChild("itemID").equalTo(itemID);
                //reference.setValue("sample");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            Toast.makeText(getApplicationContext(),"Item: "+ productname.getText().toString()+" Removed",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });


                //reference.removeValue(query);
            }
        });




    }
    public void defaultview(){
        Query query1=reference.child("cart").orderByChild("product").startAt(productid.getText().toString()).endAt(productid.getText().toString()+"\uf8ff");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("R",productid.getText().toString());
                double totalrating = 0.0;
                int reviewcount = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_cart cart = snapshot.getValue(helper_cart.class);
                        if(!cart.getItemrating().equals("0")){
                            totalrating = totalrating+ Double.parseDouble(cart.getItemrating());
                            reviewcount=reviewcount+1;
                            Log.i("Count",""+Double.parseDouble(cart.getItemrating()));
                        }


                    }
                    Log.i("Total",""+totalrating);
                    itemrating.setRating((float) totalrating/reviewcount);
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