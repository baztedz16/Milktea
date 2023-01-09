package com.example.storelocator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_login extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/");
    TextInputEditText textInputEditTextUsername,textInputEditTextPassword;
    Button buttonLogin,btnsetting;
    TextView textViewSignup;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstancesState) {

        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_login);

        textInputEditTextUsername = findViewById(R.id.LoginUser);
        textInputEditTextPassword = findViewById(R.id.LoginPass);
        buttonLogin = findViewById(R.id.buttonLogin);
        btnsetting = findViewById(R.id.btnsetting);
        textViewSignup = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),api_keys.class);
                startActivity(intent);
            }
        });
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_signup.class);
                startActivity(intent);
                finish();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loginfunction();

            }
        });

    }
    private void otp(Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_login.this);
        builder.setTitle("Enter OTP");


        final EditText input = new EditText(activity_login.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!input.getText().toString().isEmpty()){
                    startActivity(intent);
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
    }
    private void loginfunction(){
        final String enterUsername =  textInputEditTextUsername.getText().toString().trim();
        final String enterPassword =  textInputEditTextPassword.getText().toString().trim();

        Query query = reference.child("users").orderByChild("username").equalTo(enterUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    String passwordDB = dataSnapshot.child(enterUsername).child("password").getValue(String.class);
                    String userType = dataSnapshot.child(enterUsername).child("accountype").getValue(String.class);
                    String activation = dataSnapshot.child(enterUsername).child("activation").getValue(String.class);
                    String lati = dataSnapshot.child(enterUsername).child("destlat").getValue(String.class);
                    String longti = dataSnapshot.child(enterUsername).child("destlong").getValue(String.class);
                    String address = dataSnapshot.child(enterUsername).child("address").getValue(String.class);
                    String fullname = dataSnapshot.child(enterUsername).child("fullname").getValue(String.class);
                    String email = dataSnapshot.child(enterUsername).child("email").getValue(String.class);
                    String phone = dataSnapshot.child(enterUsername).child("phone").getValue(String.class);
                    Log.i("result",passwordDB);
                    if(passwordDB.equals(enterPassword)){
                        Log.i("yehey","Pasok");
                        switch (activation){
                            case "0":
                                Intent intent = new Intent(activity_login.this, activity_activation_frame.class);
                                startActivity(intent);
                                break;
                            case  "1":
                                if(userType.equals("User")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.putString("phone",phone);
                                    editor.putString("username",enterUsername);
                                    editor.putString("password",enterPassword);
                                    editor.putString("accountype",userType);
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.commit();
                                    Intent intent1 = new Intent(activity_login.this,list_store.class);
                                    otp(intent1);
                                }else if(userType.equals("Store Owner")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;



                                    String storeName = dataSnapshot.child(enterUsername).child("storename").getValue(String.class);
                                    String destlong = dataSnapshot.child(enterUsername).child("destlong").getValue(String.class);
                                    String destlat = dataSnapshot.child(enterUsername).child("destlat").getValue(String.class);
                                    Intent intent2 = new Intent(activity_login.this,store_owner.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("store",storeName);
                                    intent2.putExtra("address",destlong+","+destlat);

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("Store",storeName);
                                    editor.commit();
                                    otp(intent2);
                                }else if(userType.equals("Rider")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("Store","");
                                    editor.commit();

                                    String storeName = dataSnapshot.child(enterUsername).child("storename").getValue(String.class);
                                    Intent intent2 = new Intent(activity_login.this,rider_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);

                                    otp(intent2);
                                }else if(userType.equals("STAFF")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    String storeName = dataSnapshot.child(enterUsername).child("storename").getValue(String.class);
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("Store",storeName);
                                    editor.commit();


                                    Intent intent2 = new Intent(activity_login.this,rider_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);
                                    intent2.putExtra("Store",storeName);
                                    otp(intent2);
                                }else if(userType.equals("Admin")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    String storeName = dataSnapshot.child(enterUsername).child("storename").getValue(String.class);
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("Store",storeName);
                                    editor.commit();


                                    Intent intent2 = new Intent(activity_login.this,admin_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);
                                    intent2.putExtra("Store",storeName);
                                    otp(intent2);
                                }


                        }



                    }else {
                        Log.i("error","Password1");
                    }


                } else {
                    Log.i("error","Password2");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    }
