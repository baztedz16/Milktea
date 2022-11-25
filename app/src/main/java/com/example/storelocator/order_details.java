package com.example.storelocator;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storelocator.databinding.ActivityOrderDetailsBinding;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class order_details extends AppCompatActivity {
    ImageView getRider,pickupRider,onDelivery,ImageView;
    Button accept,confirm,button4,button5;
    TextView textorderid,userText,address,status;
    ProgressBar simpleProgressBar;

    String latStore,LongStore,latUser,longuser,Storename;

    FirebaseStorage storage;
    StorageReference ref;

    //image path string camera
    Uri stringUri;
    private static final int REQUEST_CAMERA_IMAGE = 1;
    ArrayList<helper_cart> list;
    adapter_cart_items myAdapter;
    RecyclerView recyclerView;
    String accountype;



    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
     protected void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_order_details);

         storage = FirebaseStorage.getInstance();
         ref = storage.getReference();

         recyclerView = findViewById(R.id.ordeItems);
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));

         list = new ArrayList<>();
         myAdapter = new adapter_cart_items(this,list);
         recyclerView.setAdapter(myAdapter);

         getRider = findViewById(R.id.getRider);
         pickupRider = findViewById(R.id.pickupRider);
         onDelivery = findViewById(R.id.riderDeliver);
         textorderid = findViewById(R.id.orderidNo);
         userText = findViewById(R.id.user);
         address = findViewById(R.id.address);
         status = findViewById(R.id.status);
         ImageView = findViewById(R.id.ImageProf);

         textorderid.setText(getIntent().getStringExtra("orderid").toString());

         //buttons
         accept = findViewById(R.id.accept);
         confirm = findViewById(R.id.confirm);
         button4 = findViewById(R.id.button4);//for locate store address
         button5 = findViewById(R.id.button5);//locate customer address

         accept.setVisibility(View.INVISIBLE);
         confirm.setVisibility(View.INVISIBLE);

         Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
         Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);


         simpleProgressBar=(ProgressBar) findViewById(R.id.progressBar2); // initiate the progress bar
         simpleProgressBar.setMax(100);

         SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
         accountype = preferences.getString("accountype","");
         getOrderItems();
         getOrderDetails();

         accept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent2 = new Intent(order_details.this,rating_store.class);
                 intent2.putExtra("user",preferences.getString("username",""));
                 intent2.putExtra("store",Storename);
                 intent2.putExtra("orderid",textorderid.getText().toString());
                 startActivity(intent2);
             }
         });
         confirm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(stringUri != null){
                     //Snackbar.make(findViewById(android.R.id.content),"Please add product image!!",Snackbar.LENGTH_SHORT).show();
                     ConfirmDelivery();
                 }else{
                     chooseImage();
                 }
             }
         });
         pickupRider.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(accountype.equals("STAFF") && simpleProgressBar.getProgress() == 50){
                     pickupOrder();
                 }

             }
         });
         onDelivery.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onDeliveryRider();
             }
             });
         button4.setOnClickListener(new View.OnClickListener() { //this for the lcoation of store
             @Override
             public void onClick(View view) {
                 String strUri = "http://maps.google.com/maps?q=loc:" +latStore+ "," +LongStore+ " (" + "Store Lcoation" + ")";
                 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                 intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                 startActivity(intent);
             }
         });
         button5.setOnClickListener(new View.OnClickListener() {//this for the lcoation of user
             @Override
             public void onClick(View view) {
                 String strUri = "http://maps.google.com/maps?q=loc:" +longuser+ "," +latUser+ " (" + "Client Location" + ")";
                 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                 intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                 startActivity(intent);
             }
         });

//         getRider.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 simpleProgressBar.setProgress(50);
//             }
//         });
//
//         onDelivery.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//
//             }
//         });simpleProgressBar.setProgress(100);
//
//                 if(usertype == "rider"){
//                     confirm.setVisibility(View.VISIBLE);
//                 }else{
//                     accept.setVisibility(View.VISIBLE);
//                 }
     }


    public void getOrderItems() {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        Query query1 = reference.child("cart").orderByChild("order_id").equalTo(textorderid.getText().toString());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    Log.i("R", "OrderedItems");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_cart product = snapshot.getValue(helper_cart.class);

                        list.add(product);
                    }
                    myAdapter.notifyDataSetChanged();
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
    public void getOrderDetails() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        Query query12 = reference.child("orders").orderByChild("order_id").equalTo(textorderid.getText().toString());
        query12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("R", "OrderDetails");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_order_rider order = snapshot.getValue(helper_order_rider.class);
                        latUser = order.getLati(); //getting the address to pass the location of the customer
                        longuser = order.getLongti();
                        getStoreDetails(order.getStore()); // passing the value to initiate the details of store
                        Storename = order.getStore();
                        simpleProgressBar.setProgress(100);
                        userText.setText(order.getOrder_user());
                        address.setText(order.getAddress());

                        switch(order.getStatus()){
                            case "1":
                                simpleProgressBar.setProgress(25);
                                status.setText("Store Preparing your order");
                                break;
                            case "2":
                                simpleProgressBar.setProgress(50);
                                status.setText("You Found a Rider");
                                break;
                            case "3":
                                simpleProgressBar.setProgress(75);
                                status.setText("Rider Pickup your Order");
                                break;
                            case "4":
                                simpleProgressBar.setProgress(100);
                                status.setText("Rider on the way...");
                                confirm.setVisibility(View.VISIBLE);
                                break;
                            case "5":
                                simpleProgressBar.setProgress(100);
                                status.setText("Delivery Completed");

                                if(preferences.getString("accountype","").equals("User")){
                                    accept.setVisibility(View.VISIBLE);
                                }

                                confirm.setVisibility(View.INVISIBLE);
                                break;
                            default:
                                simpleProgressBar.setProgress(0);

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
    public void getStoreDetails(String storename){
        Query query12 = reference.child("users").orderByChild("storename").equalTo(storename);
        query12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_liststore store = snapshot.getValue(helper_liststore.class);
                        if(store.getAccountype().equals("Store Owner")){
                            latStore = store.getDestlat(); //getting the address to pass the location of the customer
                            LongStore = store.getDestlong();
                            Log.i("R", latStore +"--::"+LongStore);
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
    public void pickupOrder(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("orders").child(textorderid.getText().toString());
                        reference.child("status").setValue("3");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Rider Pickup the Order?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    public void onDeliveryRider(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("orders").child(textorderid.getText().toString());
                        reference.child("status").setValue("4");

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your going to deliver this Item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData()!=null){
            stringUri=data.getData();
            ImageView.setImageURI(stringUri);


        }
    }
    private String getFileExt(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void ConfirmDelivery(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Create a reference to "mountains.jpg"
                        final Long randomkey= System.currentTimeMillis();
                        StorageReference mountainsRef = ref.child(String.valueOf(randomkey)+"."+getFileExt(stringUri));

                        // Create a reference to 'images/mountains.jpg'
                        StorageReference mountainImagesRef = ref.child("images/"+randomkey+"."+getFileExt(stringUri));

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("products");
                        //reference.setValue("sample");



                        Toast.makeText(order_details.this,"Successfully Register",Toast.LENGTH_SHORT).show();

                        // While the file names are the same, the references point to different files
                        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                        mountainsRef.getPath().equals(mountainImagesRef.getPath());
                        mountainsRef.putFile(stringUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Snackbar.make(findViewById(android.R.id.content),"Image Uplaoded",Snackbar.LENGTH_SHORT).show();

                                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        rootNode = FirebaseDatabase.getInstance();
                                        reference = rootNode.getReference("orders").child(textorderid.getText().toString());
                                        reference.child("status").setValue("5");
                                        reference.child("prof_image").setValue(uri.toString());

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your going to deliver this Item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }



}