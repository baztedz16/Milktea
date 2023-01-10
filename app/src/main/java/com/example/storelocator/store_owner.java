package com.example.storelocator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

public class store_owner extends AppCompatActivity {
    TextView userid;
    EditText store,address,itemname,price1,price2,price3;
    Button saveitem,deleteitem;
    String sstore;
    ListView listview;
    ImageView productImg;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    RecyclerView recyclerView;
    Spinner categoryspin;

    adapter_itemlist myAdapter;
    ArrayList<helper_product> list;
    LinearLayout linearLayout6;

    public Uri imageUri;
    public static final String KEY_STORE = "sstore";
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_owner);

        userid = findViewById(R.id.userid);
        store = findViewById(R.id.store);
        address = findViewById(R.id.address);
        itemname = findViewById(R.id.itemname);
        saveitem = findViewById(R.id.saveitem);
        price1 = findViewById(R.id.pricesm);
        price2 = findViewById(R.id.pricemd);
        price3 = findViewById(R.id.pricelg);
        categoryspin = findViewById(R.id.categoryspin);
        linearLayout6 = findViewById(R.id.linearLayout6);

        linearLayout6.getLayoutParams().height = 1; // LayoutParams: android.view.ViewGroup.LayoutParams
        // wv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        linearLayout6.requestLayout();//It is necesary to refresh the screen


        recyclerView = findViewById(R.id.viewItems);

        //final String storeOwner = ;


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        productImg = findViewById(R.id.product_image);
        //string here is the value when you load the page
        userid.setText(getIntent().getStringExtra("user"));
        store.setText(getIntent().getStringExtra("store"));
        address.setText(getIntent().getStringExtra("address"));

        list = new ArrayList<>();
        myAdapter = new adapter_itemlist(this,list);
        recyclerView.setAdapter(myAdapter);

        Query query = reference.child("products").orderByChild("storeOwner").equalTo(getIntent().getStringExtra("store"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        helper_product product = snapshot.getValue(helper_product.class);
                        list.add(product);
                        Log.i("R",product.itemID.toString());
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String cat = areaSnapshot.child("store").getValue(String.class);
                    if(cat.equals(store.getText().toString())){
                        areas.add(areaSnapshot.child("categoryname").getValue(String.class));
                    }

                }


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(store_owner.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryspin.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.i("result:",dataSnapshot.getValue(helper_product.class).toString());
                   helper_product product = dataSnapshot.getValue(helper_product.class);
                    list.add(product);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();

        saveitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveitem.getText().toString().equals("Add Item")){
                    saveitem.setText("SAVE");
                    linearLayout6.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT; // LayoutParams: android.view.ViewGroup.LayoutParams
                    // wv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    linearLayout6.requestLayout();//It is necesary to refresh the screen
                    //deleteitem = findViewById(R.id.deleteitem);


                }else if(saveitem.getText().toString().equals("SAVE")){
                    if(imageUri != null){
                        //Snackbar.make(findViewById(android.R.id.content),"Please add product image!!",Snackbar.LENGTH_SHORT).show();
                        uploadImage();
                        linearLayout6.getLayoutParams().height = 1; // LayoutParams: android.view.ViewGroup.LayoutParams
                        // wv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                        linearLayout6.requestLayout();//It is necesary to refresh the screen
                        //deleteitem = findViewById(R.id.deleteitem);
                        saveitem.setText("Add Item");

                    }else if(itemname.getText().toString().equals("") || itemname.getText().toString().equals(null)){
                        Snackbar.make(findViewById(android.R.id.content),"Please add product name!!",Snackbar.LENGTH_SHORT).show();

                    }else{

                        chooseImage();
                    }

                }


            }
        });
        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

//        deleteitem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("Delete","Succesfully");
//            }
//        });



    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            productImg.setImageURI(imageUri);


        }
    }
    private String getFileExt(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadImage() {
        // Create a reference to "mountains.jpg"
        final Long randomkey= System.currentTimeMillis();
        StorageReference mountainsRef = ref.child(String.valueOf(randomkey)+"."+getFileExt(imageUri));

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = ref.child("images/"+randomkey+"."+getFileExt(imageUri));

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("products");
        //reference.setValue("sample");



        Toast.makeText(store_owner.this,"Successfully Register",Toast.LENGTH_SHORT).show();

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());
        mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Snackbar.make(findViewById(android.R.id.content),"Image Uplaoded",Snackbar.LENGTH_SHORT).show();

                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String productname = itemname.getText().toString();
                        String storeowner = store.getText().toString();
                        String img = reference.push().getKey();
                        String add = address.getText().toString();
                        String pricesm = price1.getText().toString();
                        String pricemd = price2.getText().toString();
                        String pricelg = price3.getText().toString();
                        String[] strParts = address.getText().toString().split(",");
                        String category = categoryspin.getSelectedItem().toString();


                        String deslong = strParts[0].toString();
                        String deslat =  strParts[1].toString();
                        String link=String.valueOf(randomkey)+"."+getFileExt(imageUri);

                        helper_product helper_product = new helper_product(productname,storeowner,uri.toString(),add,img,deslong,deslat,getIntent().getStringExtra("user"),0,"",pricesm,pricesm,pricemd,pricelg,category);
                        reference.child(img).setValue(helper_product);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        SharedPreferences preferences = store_owner.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String accountype = preferences.getString("accountype","");
        String staffstore = preferences.getString("Store","");
        if(item_id == R.id.openLocator){
            Intent intent = new Intent(store_owner.this,mainframe.class);
            startActivity(intent);
        }else if(item_id == R.id.mangeStore){
            Intent intent = new Intent(store_owner.this, activity_login.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
        }else if(item_id == R.id.staff){
            Intent intent = new Intent(store_owner.this,store_owner_staff.class);
            intent.putExtra("storeSelect",getIntent().getStringExtra("store"));
            startActivity(intent);
        }else if(item_id == R.id.store){
            Intent intent2 = new Intent(store_owner.this,rider_frame.class);
            startActivity(intent2);
        }else if(item_id == R.id.category){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Category");

            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(!input.getText().toString().isEmpty()){
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("category");
                        String storeowner = store.getText().toString();
                        String ref = reference.push().getKey();
                        String category = input.getText().toString();


                        helper_category helper_category = new helper_category(category,ref,storeowner);
                        reference.child(ref).setValue(helper_category);
                    }

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }else if(item_id == R.id.reviews){
            Intent intent = new Intent(this,activity_storeratings.class);
            intent.putExtra("storeSelect",store.getText().toString());
            startActivity(intent);
        }

        return true;
    }
}