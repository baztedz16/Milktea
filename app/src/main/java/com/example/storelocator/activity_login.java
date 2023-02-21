package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.storelocator.email.GMailSender;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        checkLocationPermission();

//        btnsetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),api_keys.class);
//                startActivity(intent);
//            }
//        });
        btnsetting.setVisibility(View.GONE);
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_signup.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginfunction();

            }
        });

    }
    private void otp(Intent intent,String email,DatabaseReference reference,int verifCode,String username){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_login.this);
        builder.setTitle("Enter your Verification Code.");
        builder.setMessage("The Code has been sent on " + email);

//        int verifCode = codeSendFunction();
//
//        String username = "storelocator2023@gmail.com";
//        String password = "ceqcpxxmxadyvzod";
//
//        sendEmail(username,
//                password,
//                email,
//                "Email Verification From Milkea App",
//                "The Code is "+verifCode+".");


        final EditText input = new EditText(activity_login.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);
        builder.setCancelable(false);

//        builder.setPositiveButton("Submit",null);
//        builder.setNegativeButton("Cancel",null);
//        Button dialog_pos = builder.show().getButton(AlertDialog.BUTTON_POSITIVE);
//        Button dialog_neg = builder.show().getButton(AlertDialog.BUTTON_NEGATIVE);
//
//        dialog_pos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(input.getText().toString().isEmpty()){
//                    Toast.makeText(activity_login.this,"Please Enter your Code",Toast.LENGTH_LONG).show();
//                } else if (Integer.parseInt(input.getText().toString().trim()) != verifCode ) {
//                    Toast.makeText(activity_login.this,"Wrong Code. Try Again.",Toast.LENGTH_LONG).show();
//                } else {
//                    startActivity(intent);
//                    reference.setValue("1");
//                }
//            }
//        });

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("Username For OTP",username);
                if(input.getText().toString().isEmpty()){
                    Toast.makeText(activity_login.this,"Please Enter your Code",Toast.LENGTH_LONG).show();
                    otp(intent,email,reference,verifCode,username);
                } else if (Integer.parseInt(input.getText().toString().trim()) != verifCode ) {
                    otp(intent,email,reference,verifCode,username);
                    Toast.makeText(activity_login.this,"Wrong Code. Try Again.",Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                    reference.setValue("1");
                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://storelocator-c908a-default-rtdb.firebaseio.com/").child("forOTP").child(username).removeValue();
                    dialog.cancel();
                    finishAffinity();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }
    private void loginfunction(){
        final String enterUsername =  textInputEditTextUsername.getText().toString().trim();
        final String enterPassword =  textInputEditTextPassword.getText().toString().trim();

        if (enterPassword.isEmpty() || enterUsername.isEmpty()) {
            Toast.makeText(activity_login.this,"Please Fill the Form.",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = reference.child("users").child(enterUsername).child("activation");

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
                    String email = dataSnapshot.child(enterUsername).child("email").getValue(String.class);
                    String storeName = dataSnapshot.child(enterUsername).child("storename").getValue(String.class);
                    String phone = dataSnapshot.child(enterUsername).child("phone").getValue(String.class);
                    String fullname = dataSnapshot.child(enterUsername).child("fullname").getValue(String.class);
                    Log.i("result",passwordDB);
                    if(passwordDB.equals(enterPassword)){
                        Log.i("yehey","Pasok");
                        switch (activation){
                            case "0":
                                Intent intent = new Intent(activity_login.this, activity_activation_frame.class);
                                Intent intent_user = new Intent(activity_login.this,list_store.class);
                                if (userType.equals("User")) {
                                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("forOTP");
//                                    int verifCode = codeSendFunction();
//                                    SharedPreferences preferences;
//                                    SharedPreferences.Editor editor;
//                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
//                                    editor = preferences.edit();
//                                    editor.putString("username",enterUsername);
//                                    editor.putString("accountype",userType);
//                                    editor.putString("password",passwordDB);
//                                    editor.putString("address",address);
//                                    editor.putString("longti",longti);
//                                    editor.putString("lati",lati);
//                                    editor.putString("phone",phone);
//                                    editor.putString("fullname",fullname);
//                                    editor.putString("email",email);
//                                    editor.commit();
//                                    String username = "storelocator2023@gmail.com";
//                                    String password = "ceqcpxxmxadyvzod";
//
//                                    sendEmail(username,
//                                            password,
//                                            email,
//                                            "Email Verification From Your Friendly Milktea App",
//                                            "Hello " + fullname + ", the Code is "+verifCode+".");
//                                    otp(intent_user,email,ref,verifCode);
                                    dataRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                           if (snapshot.child(enterUsername).exists()) {
                                               int verifCode = codeSendFunction();
                                               SharedPreferences preferences;
                                               SharedPreferences.Editor editor;
                                               preferences = getSharedPreferences("user",MODE_PRIVATE);
                                               editor = preferences.edit();
                                               editor.putString("username",enterUsername);
                                               editor.putString("accountype",userType);
                                               editor.putString("password",passwordDB);
                                               editor.putString("address",address);
                                               editor.putString("longti",longti);
                                               editor.putString("lati",lati);
                                               editor.putString("phone",phone);
                                               editor.putString("fullname",fullname);
                                               editor.putString("email",email);
                                               editor.commit();
                                               String username = "storelocator2023@gmail.com";
                                               String password = "ceqcpxxmxadyvzod";

                                               sendEmail(username,
                                                       password,
                                                       email,
                                                       "Email Verification From Your Friendly Milktea App",
                                                       "Hello " + fullname + ", the Code is "+verifCode+".");
                                               otp(intent_user,email,ref,verifCode,enterUsername);
                                           } else {
                                               startActivity(intent);
                                           }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(activity_login.this,"Please Wait.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    startActivity(intent);
                                }
                                break;
                            case  "1":
                                if(userType.equals("User")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("password",passwordDB);
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.putString("phone",phone);
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.commit();
                                    Intent intent1 = new Intent(activity_login.this,list_store.class);
                                    startActivity(intent1);
//                                    otp(intent1);
                                }else if(userType.equals("Store Owner")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;
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
                                    editor.putString("password",passwordDB);
                                    editor.putString("Store",storeName);
                                    editor.putString("phone",phone);
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.commit();
                                    startActivity(intent2);
                                    finishAffinity();
//                                    otp(intent2);
                                }else if(userType.equals("Rider")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("password",passwordDB);
                                    editor.putString("Store","");
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.putString("phone",phone);
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.commit();

                                    Intent intent2 = new Intent(activity_login.this,rider_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);

                                    startActivity(intent2);
                                    finishAffinity();
//                                    otp(intent2);
                                }else if(userType.equals("STAFF")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();

                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("password",passwordDB);
                                    editor.putString("Store",storeName);
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.putString("phone",phone);
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.commit();


                                    Intent intent2 = new Intent(activity_login.this,rider_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);
                                    intent2.putExtra("Store",storeName);

                                    startActivity(intent2);
                                    finishAffinity();
//                                    otp(intent2);
                                }else if(userType.equals("Admin")){
                                    SharedPreferences preferences;
                                    SharedPreferences.Editor editor;

                                    preferences = getSharedPreferences("user",MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("username",enterUsername);
                                    editor.putString("accountype",userType);
                                    editor.putString("password",passwordDB);
                                    editor.putString("Store",storeName);
                                    editor.putString("address",address);
                                    editor.putString("longti",longti);
                                    editor.putString("lati",lati);
                                    editor.putString("phone",phone);
                                    editor.putString("fullname",fullname);
                                    editor.putString("email",email);
                                    editor.commit();


                                    Intent intent2 = new Intent(activity_login.this,admin_frame.class);
                                    intent2.putExtra("user",enterUsername);
                                    intent2.putExtra("accountype",userType);
                                    intent2.putExtra("Store",storeName);
                                    startActivity(intent2);
                                    finishAffinity();
//                                    otp(intent2);
                                }


                        }



                    }else {
                        Toast.makeText(activity_login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                        Log.i("error","Password1");
                    }


                } else {
                    Toast.makeText(activity_login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    Log.i("error","Password2");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error",String.valueOf(databaseError));
            }
        });
    }


    private void sendEmail(final String Sender,final String Password,final String Receiver,final String Title,final String Message)
    {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(Sender,Password);
                    sender.sendMail(Title, "<b>"+Message+"</b>", Sender, Receiver);
                    makeAlert(Receiver);

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }
    private void makeAlert(String email){
        this.runOnUiThread(new Runnable() {
            public void run() {
                System.out.println("Mail Sent to "+email+".");
            }
        });
    }

    private  boolean checkLocationPermission(){
        int location = ContextCompat.checkSelfPermission(activity_login.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(activity_login.this,Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermission = new ArrayList<>();
        if(location != PackageManager.PERMISSION_GRANTED){

            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions((Activity) activity_login.this,listPermission.toArray(new String[listPermission.size()]),1);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int codeSendFunction() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}
