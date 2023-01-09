package com.example.storelocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class mainframe_viewcart extends AppCompatActivity {
    TextView total;
    EditText editTextname;
    Button buttonfetch,buttonStoreList;
    ListView listview;
    String Name, City, Country, locClass;
    String id = null;
    ProgressDialog mProgressDialog;
    ArrayList<helper_cart> list;
    adapter_cart myAdapter;
    RecyclerView recyclerView;
    SharedPreferences preferences;
    int cartValue =0,totalitems=0 ;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseDatabase rootNode;
    DatabaseReference reference =FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");




    /*public LocationManager locationManager;
    public LocationListener locationListener = new myLocationListener();
    String lat, lon;
    private boolean gps_enable = false;
    private boolean network_enable = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainframe_viewcart);
        editTextname = findViewById(R.id.etname);
        buttonfetch = findViewById(R.id.btnfetch);
        recyclerView = findViewById(R.id.storeList);
        buttonStoreList = findViewById(R.id.placeorder);
        total = findViewById(R.id.textView3);
        preferences=this.getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new adapter_cart(this,list);
        recyclerView.setAdapter(myAdapter);




        //view product listed to the mainframe

        defaultview();
        buttonStoreList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertCharges();
               }


                //Intent intent = new Intent(getApplicationContext(),list_store.class);
                // startActivity(intent);
                //finish();

        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total.setText((getIntent().getStringExtra("total")));
            }
        });

    }
    private  void alertCharges(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainframe_viewcart.this);
        builder.setTitle("Order Details");
        builder.setMessage("Your Orders Amount is: PHP"+ cartValue +"\n and Delivery Charge of: PHP60\n"+"TOTAL:"+(cartValue+60));

//        final EditText input = new EditText(mainframe_viewcart.this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
//        input.setGravity(Gravity.CENTER);
//        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkout();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void checkout(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);

        String address = sh.getString("address", "");
        String lati = sh.getString("lati", "");
        String longti = sh.getString("longti", "");
        String store = sh.getString("store", "");
        final String username = preferences.getString("username","");
        Query query =reference.child("cart").orderByChild("username").equalTo(username);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("orders");
        id = reference.push().getKey();
        reference.child(id).child("order_id").setValue(id);
        reference.child(id).child("status").setValue("0");
        reference.child(id).child("order_user").setValue(username);
        reference.child(id).child("order_total").setValue(String.valueOf(cartValue+60));
        reference.child(id).child("address").setValue(address);
        reference.child(id).child("longti").setValue(lati);
        reference.child(id).child("lati").setValue(longti);
        reference.child(id).child("store").setValue(store);
        reference.child(id).child("rider").setValue(store);
        reference.child(id).child("itemcount").setValue(totalitems);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        reference.child(id).child("date_order").setValue(dtf.format(now));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if (snapshot1.exists()) {

                    try {
                        for (DataSnapshot snapshot : snapshot1.getChildren()) {
                            if (snapshot.child("orderstatus").getValue().equals("0") && snapshot.child("owner").getValue().equals(store)) {
                                Log.i("R", snapshot.child("cartid").getValue().toString());
                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("cart").child(snapshot.child("cartid").getValue().toString());
                                //reference.setValue("sample");
                                reference.child("orderstatus").setValue("1");
                                reference.child("order_id").setValue(id);
                                //

                            }
                        }
                    }catch (Exception e){

                    }

                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent1 = new Intent(mainframe_viewcart.this,mainframe.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
    }

    public void defaultview(){
        SharedPreferences sh = getSharedPreferences("user", MODE_PRIVATE);
        String store = sh.getString("store", "");
        final String username = preferences.getString("username","");
        Query query1=reference.child("cart").orderByChild("username").equalTo(username);
        Log.i("R",store);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("R",editTextname.getText().toString());
                cartValue=0;
                totalitems=0;
                if (dataSnapshot.exists()) {
                    list.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        try{
                            if(!snapshot.child("orderstatus").getValue().toString().equals("1") && snapshot.child("owner").getValue().equals(store)){
                                helper_cart product = snapshot.getValue(helper_cart.class);
                                Log.i("OS:",snapshot.child("orderstatus").getValue().toString());
                                Log.i("Values",snapshot.child("price").getValue().toString() +"x"+ snapshot.child("qty").getValue().toString());
                                cartValue = cartValue + (Integer.parseInt(snapshot.child("price").getValue().toString())*Integer.parseInt(snapshot.child("qty").getValue().toString()));
                                totalitems = totalitems+1;
                                Log.i("OrderTotal:", String.valueOf(cartValue));
                                list.add(product);
                            }
                        }catch(Exception e){
                            Log.i("Error",e.toString());
                        }

                    }
                    myAdapter.notifyDataSetChanged();
                }else{
                    Log.i("R","6");
                    //Log.i("R",searchtext);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}