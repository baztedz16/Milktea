package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class activity_signup extends AppCompatActivity {
    private static final int REQUEST_PLACE_PICKER = 1001;
    TextInputEditText regfullname,regusername,regpassword,regemail,regstorename,regphone,longt,lati,address;
    Spinner regacc;
    Button buttonSignup,pickLocationBtn;
    TextView textViewLogin;
    ProgressBar progressBar;
    static Double lh,lt;

    private static int shopfound;

    DatabaseReference verifydb = FirebaseDatabase.getInstance().getReference();

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 999;

    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private static final String NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    private static final String CONTACT_PATTERN = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    // private static final String ADDRESS_PATTERN = "^[#.0-9a-zA-Z\\s,-]+$";



    private static final Pattern pattern_email = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern pattern_name = Pattern.compile(NAME_PATTERN);
    private static final Pattern pattern_contact = Pattern.compile(CONTACT_PATTERN);
    // private static final Pattern pattern_address = Pattern.compile(ADDRESS_PATTERN);

    public static boolean isValidEmail(final String email) {
        Matcher matcher = pattern_email.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidContact(final String contact) {
        Matcher matcher = pattern_contact.matcher(contact);
        return matcher.matches();
    }
    public static boolean isValidName(final String name) {
        Matcher matcher = pattern_name.matcher(name);
        return matcher.matches();
    }
    public static boolean isValidAddress(final String address) {
        Matcher matcher = pattern_name.matcher(address);
        return matcher.matches();
    }


    String accountype;


    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstancesState) {


        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_signup);
        regfullname = findViewById(R.id.fullname);
        regusername = findViewById(R.id.username);
        regpassword = findViewById(R.id.password);
        regemail = findViewById(R.id.email);
//        regstorename = findViewById(R.id.storename);
        regphone = findViewById(R.id.phone);
        textViewLogin = findViewById(R.id.loginText);
        address = findViewById(R.id.address);
        regacc = findViewById(R.id.accType);
        Spinner spinner= (Spinner) findViewById(R.id.accType);
        pickLocationBtn = findViewById(R.id.pickLocation);
        longt =  findViewById(R.id.longt);
        lati =  findViewById(R.id.lat);
        address =  findViewById(R.id.address);

        longt.setVisibility(View.GONE);
        lati.setVisibility(View.GONE);

        wifiManager= (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        buttonSignup = findViewById(R.id.signupBtn);

//        progressBar = findViewById(R.id.progress);

        ArrayAdapter<String> list = new ArrayAdapter<String>(activity_signup.this
                ,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.account_type));
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(list);




        pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = regfullname.getText().toString();
                String username = regusername.getText().toString();
                String password = regpassword.getText().toString();
                String email = regemail.getText().toString();
//                String storename = regstorename.getText().toString();
                String storename = "";
                String phone = regphone.getText().toString();
//                String accountype = regacc.getSelectedItem().toString();
//                if (regacc.getSelectedItem().toString() == "Customer") {
//                    accountype = "User";
//                } else if (regacc.getSelectedItem().toString() == "Shop Owner") {
//                    accountype = "Store Owner";
//                } else if (regacc.getSelectedItem().toString() == "Delivery Guy") {
//                    accountype = "Rider";
//                }
                String Destlongt = longt.getText().toString();
                String Deslati = lati.getText().toString();
                String Address = address.getText().toString();
                String image = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";
                if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                        || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                    Toast.makeText(activity_signup.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                } else if (!isValidName(fullname)) {
                    regfullname.setError("Name should be valid.");
                } else if (!isValidEmail(email)) {
                    regemail.setError("Please use a Valid Email.");
                } else if (password.length() < 6) {
                    regpassword.setError("The password is too short.");
                } else if (phone.length() < 10) {
                    regphone.setError("Please Input Right Phone Number.");
                }
                else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");
                    //reference.setValue("sample");

                    Query query = reference.orderByChild(username);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String usernameDB = snapshot.child(username).child("username").getValue(String.class);
                                if (usernameDB != null) {
                                    Log.i("Error:", "User:" + username + " Already Exist");
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_signup.this);
//                                    builder.setMessage("User Exist, Kindly enter different Username")
//                                            .setCancelable(false)
//                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            });
//                                    AlertDialog alert = builder.create();
//                                    alert.show();
                                    regusername.setError("This username has already taken.");
                                    return;
                                } else {
//                                    helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"0","0",Address);
//                                    reference.child(username).setValue(helper_user);
//                                    Toast.makeText(activity_signup.this,"Successfully Register",Toast.LENGTH_SHORT).show();


                                    Intent intent_login = new Intent(activity_signup.this, activity_login.class);

//                                    if (regacc.getSelectedItem().toString() == "Shop Owner") {
//                                        accountype = "Store Owner";
//                                        storeNameCreate(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address, intent_login, reference);
//                                    } else if (regacc.getSelectedItem().toString() == "Delivery Guy") {
//                                        accountype = "Rider";
//                                        helper_user helper_user = new helper_user(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address);
//                                        reference.child(username).setValue(helper_user);
//                                        Toast.makeText(activity_signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(intent_login);
//                                        finish();
//                                    } else {
//                                        verifydb.child("forOTP").child(username).setValue(1);
//                                        accountype = "User";
//                                        helper_user helper_user = new helper_user(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address);
//                                        reference.child(username).setValue(helper_user);
//                                        Toast.makeText(activity_signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(intent_login);
//                                        finish();
//                                    }

                                    helper_user helper_user;

                                    switch(regacc.getSelectedItemPosition()) {
                                        case 1:
                                            accountype = "Store Owner";
                                            storeNameCreate(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address, intent_login, reference);
                                            break;
                                        case 2:
                                            accountype = "Rider";
                                            helper_user = new helper_user(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address);
                                            reference.child(username).setValue(helper_user);
                                            Toast.makeText(activity_signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(intent_login);
                                            finish();
                                            break;
                                        case 0:
                                            verifydb.child("forOTP").child(username).setValue(1);
                                            accountype = "User";
                                            helper_user = new helper_user(fullname, username, password, email, storename, phone, accountype, Destlongt, Deslati, image, "0", "0", Address);
                                            reference.child(username).setValue(helper_user);
                                            Toast.makeText(activity_signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(intent_login);
                                            finish();
                                            break;
                                        default:
                                            break;
                                    }



                                }


                            }else{
                                Log.i("Error:", "User:"+username+" DidntCheck");



                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"0","0",Address);
//                    reference.child(username).setValue(helper_user);
//
//                    Toast.makeText(signup.this,"Successfully Register",Toast.LENGTH_SHORT).show();


                    //view usally use for storeowner for their store rating

                }


            }
        });

        //Start ProgressBar first (Set visibility VISIBLE)

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Toast.makeText(getBaseContext(), "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(activity_signup.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity_signup.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(activity_signup.this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(activity_signup.this, Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lh = addressList.get(0).getLongitude();
                            lt = addressList.get(0).getLatitude();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {

                    }
                }
            });
        }
    }
    private void showPlacePicker() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(lt, lh)
                .showLatLong(true)
                .setMapRawResourceStyle(R.raw.map_style)
                .setMapType(MapType.NORMAL)
                .build(activity_signup.this);

        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
    }

    private void storeNameCreate(String fullname, String username, String password, String email, String storename, String phone,String accountype,String destlong,String destlat,String image,String view,String activation,String Address,Intent intent,DatabaseReference reference) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_signup.this);
        builder.setTitle("Set Your Milktea Shop Name.");

        final EditText input = new EditText(activity_signup.this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);
        builder.setCancelable(true);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().isEmpty()){
                    Toast.makeText(activity_signup.this,"Please Input Shop Name.",Toast.LENGTH_SHORT).show();
                    storeNameCreate(fullname,username,password,email,storename,phone,accountype,destlong,destlat,image,"0","0",Address,intent,reference);
                } else if (shopFound(input.getText().toString().trim(),username) == 0) {
                    Toast.makeText(activity_signup.this,"Shop name existed. Input another.",Toast.LENGTH_SHORT).show();
                    storeNameCreate(fullname,username,password,email,storename,phone,accountype,destlong,destlat,image,"0","0",Address,intent,reference);
                } else {
                    String storeName = input.getText().toString().trim();
                    helper_user helper_user = new helper_user(fullname,username,password,email,storeName,phone,accountype,destlong,destlat,image,"0","0",Address);
                    reference.child(username).setValue(helper_user);
                    Toast.makeText(activity_signup.this,"Successfully Register",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
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



    private int shopFound(String shopname,String username) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(username).child("storename").child(shopname).exists()) {
                    shopfound = 0;
                } else {
                    shopfound = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return shopfound;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                longt.setText(String.valueOf(addressData.getLongitude()));
                lati.setText(String.valueOf(addressData.getLatitude()));
                address.setText(String.valueOf(addressData.getAddressList().get(0).getAddressLine(0)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
