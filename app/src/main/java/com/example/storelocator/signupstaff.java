package com.example.storelocator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ValueEventListener;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signupstaff extends AppCompatActivity {
    private static final int REQUEST_PLACE_PICKER = 1001;
    TextInputEditText regfullname,regusername,regpassword,regemail,regstorename,regphone,longt,lati,address;
    Spinner regacc;
    Button buttonSignup,pickLocationBtn;
    ProgressBar progressBar;
    static Double lh,lt;

    private static int userfound;

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

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstancesState) {


        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_signup_staff);
        regfullname = findViewById(R.id.fullname);
        regusername = findViewById(R.id.username);
        regpassword = findViewById(R.id.password);
        regemail = findViewById(R.id.email);
        regstorename = findViewById(R.id.storename);
        regphone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        pickLocationBtn = findViewById(R.id.pickLocation);
        longt =  findViewById(R.id.longt);
        lati =  findViewById(R.id.lat);
        address =  findViewById(R.id.address);


        longt.setVisibility(View.GONE);
        lati.setVisibility(View.GONE);

        buttonSignup = findViewById(R.id.signupBtn);

        if(getIntent().getStringExtra("hasdata").equals("0")){
            pickLocationBtn.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            buttonSignup.setText("Add Staff");
//            if (signupstaff.this.getSharedPreferences("user", Context.MODE_PRIVATE).getString("accountype","").equals("Shop Owner")) {
//
//            }
        } else if (getIntent().getStringExtra("hasdata").equals("1") &&
                !signupstaff.this.getSharedPreferences("user", Context.MODE_PRIVATE).getString("accountype","").equals("Admin")) {
            regstorename.getLayoutParams().height = 1;
            regfullname.setText(getIntent().getStringExtra("fullname"));
            regemail.setText(getIntent().getStringExtra("email"));
            regstorename.setText(getIntent().getStringExtra("storeSelect"));
            regusername.setText(getIntent().getStringExtra("username"));
            regpassword.setText(getIntent().getStringExtra("password"));
            regphone.setText(getIntent().getStringExtra("phone"));
            longt.setText(getIntent().getStringExtra("long"));
            lati.setText(getIntent().getStringExtra("lat"));
            address.setText(getIntent().getStringExtra("address"));
            buttonSignup.setText("Update Staff");
            getSupportActionBar().setTitle("Change Staff Info");
            pickLocationBtn.setVisibility(View.GONE);
            address.setVisibility(View.GONE);

        } else {
            regfullname.setText(getIntent().getStringExtra("fullname"));
            regemail.setText(getIntent().getStringExtra("email"));
            regstorename.setText(getIntent().getStringExtra("storeSelect"));
            regusername.setText(getIntent().getStringExtra("username"));
            regpassword.setText(getIntent().getStringExtra("password"));
            regphone.setText(getIntent().getStringExtra("phone"));
            longt.setText(getIntent().getStringExtra("long"));
            lati.setText(getIntent().getStringExtra("lat"));
            address.setText(getIntent().getStringExtra("address"));
            buttonSignup.setText("Update Your Account");
            getSupportActionBar().setTitle("Change User Account");
        }

        wifiManager= (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        progressBar = findViewById(R.id.progress);
        regstorename.setVisibility(View.INVISIBLE);
        regstorename.setText(getIntent().getStringExtra("storeSelect"));






        pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //reference.setValue("sample");
                if(getIntent().getStringExtra("hasdata").equals("1")){
                    String fullname = regfullname.getText().toString();
                    String username = regusername.getText().toString();
                    String password = regpassword.getText().toString();
                    String email = regemail.getText().toString();
                    String storename = regstorename.getText().toString();
                    String phone = regphone.getText().toString();
                    String accountype = "STAFF";
                    String Destlongt = longt.getText().toString();
                    String Deslati = lati.getText().toString();
                    String Address = address.getText().toString();
                    String image = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";

                    if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                            || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                        Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!isValidName(fullname) && fullname.length() < 2) {
                        regfullname.setError("Name should be valid.");
                        return;
                    } else if (!isValidEmail(email)) {
                        regemail.setError("Please use a Valid Email.");
                        return;
                    } else if (password.length() < 6) {
                        regpassword.setError("The password is too short.");
                        return;
                    } else if (phone.length() < 10) {
                        regphone.setError("Please Input Right Phone Number.");
                        return;
                    } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                        regusername.setError("This username exists.");
                        return;
                    } else {
                        reference.child(getIntent().getStringExtra("username")).removeValue();

                        //view usally use for storeowner for their store rating
                        helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"0","1",Address);
                        reference.child(username).setValue(helper_user);

                        Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signupstaff.this,rider_frame.class);
                        startActivity(intent);
                    }

                }else if(getIntent().getStringExtra("hasdata").equals("2")){
                    String fullname = regfullname.getText().toString();
                    String username = regusername.getText().toString();
                    String password = regpassword.getText().toString();
                    String email = regemail.getText().toString();
                    String storename = regstorename.getText().toString();
                    String phone = regphone.getText().toString();

                    String Destlongt = longt.getText().toString();
                    String Deslati = lati.getText().toString();
                    String Address = address.getText().toString();
                    String image = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";

                    if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                            || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                        Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!isValidName(fullname) && fullname.length() < 2) {
                        regfullname.setError("Name should be valid.");
                        return;
                    } else if (!isValidEmail(email)) {
                        regemail.setError("Please use a Valid Email.");
                        return;
                    } else if (password.length() < 6) {
                        regpassword.setError("The password is too short.");
                        return;
                    } else if (phone.length() < 10) {
                        regphone.setError("Please Input Right Phone Number.");
                        return;
                    }



                    //view usally use for storeowner for their store rating

                    SharedPreferences preferences = signupstaff.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                    String actype = preferences.getString("accountype","");

                    // signupstaff.this.getSharedPreferences("user", Context.MODE_PRIVATE).getString("accountype","");
                    if(actype.equals("User")){
                        if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                                || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                            Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!isValidName(fullname) && fullname.length() < 2) {
                            regfullname.setError("Name should be valid.");
                            return;
                        } else if (!isValidEmail(email)) {
                            regemail.setError("Please use a Valid Email.");
                            return;
                        } else if (password.length() < 6) {
                            regpassword.setError("The password is too short.");
                            return;
                        } else if (phone.length() < 10) {
                            regphone.setError("Please Input Right Phone Number.");
                            return;
                        } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                            regusername.setError("This username exists.");
                            return;
                        } else {
                            reference.child(getIntent().getStringExtra("username")).removeValue();
                            String accountype = "User";
                            helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"1","1",Address);
                            reference.child(username).setValue(helper_user);


                            SharedPreferences preferences1;
                            SharedPreferences.Editor editor;
                            preferences1 = getSharedPreferences("user",MODE_PRIVATE);
                            editor = preferences1.edit();
                            editor.putString("username",username);
                            editor.putString("accountype",accountype);
                            editor.putString("password",password);
                            editor.putString("address",Address);
                            editor.putString("longti",Destlongt);
                            editor.putString("lati",Deslati);
                            editor.putString("phone",phone);
                            editor.putString("fullname",fullname);
                            editor.putString("email",email);
                            editor.commit();


                            Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(signupstaff.this,list_store.class);
                            startActivity(intent);
                        }


                    }else if(actype.equals("Store Owner")){
                        if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                                || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                            Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!isValidName(fullname) && fullname.length() < 2) {
                            regfullname.setError("Name should be valid.");
                            return;
                        } else if (!isValidEmail(email)) {
                            regemail.setError("Please use a Valid Email.");
                            return;
                        } else if (password.length() < 6) {
                            regpassword.setError("The password is too short.");
                            return;
                        } else if (phone.length() < 10) {
                            regphone.setError("Please Input Right Phone Number.");
                            return;
                        } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                            regusername.setError("This username exists.");
                            return;
                        } else {

                            reference.child(getIntent().getStringExtra("username")).removeValue();
                            String accountype = "Store Owner";
                            helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"1","1",Address);
                            reference.child(username).setValue(helper_user);

                            SharedPreferences preferences1;
                            SharedPreferences.Editor editor;
                            preferences1 = getSharedPreferences("user",MODE_PRIVATE);
                            editor = preferences1.edit();
                            editor.putString("username",username);
                            editor.putString("accountype",accountype);
                            editor.putString("password",password);
                            editor.putString("address",Address);
                            editor.putString("longti",Destlongt);
                            editor.putString("lati",Deslati);
                            editor.putString("phone",phone);
                            editor.putString("fullname",fullname);
                            editor.putString("email",email);
                            editor.commit();

                            Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }else if(actype.equals("STAFF")){
                        if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                                || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                            Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!isValidName(fullname) && fullname.length() < 2) {
                            regfullname.setError("Name should be valid.");
                            return;
                        } else if (!isValidEmail(email)) {
                            regemail.setError("Please use a Valid Email.");
                            return;
                        } else if (password.length() < 6) {
                            regpassword.setError("The password is too short.");
                            return;
                        } else if (phone.length() < 10) {
                            regphone.setError("Please Input Right Phone Number.");
                            return;
                        } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                            regusername.setError("This username exists.");
                            return;
                        } else {
                            reference.child(getIntent().getStringExtra("username")).removeValue();
                            String accountype = "STAFF";
                            helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"1","1",Address);
                            reference.child(username).setValue(helper_user);


                            Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }else{
                        if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                                || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                            Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!isValidName(fullname) && fullname.length() < 2) {
                            regfullname.setError("Name should be valid.");
                            return;
                        } else if (!isValidEmail(email)) {
                            regemail.setError("Please use a Valid Email.");
                            return;
                        } else if (password.length() < 6) {
                            regpassword.setError("The password is too short.");
                            return;
                        } else if (phone.length() < 10) {
                            regphone.setError("Please Input Right Phone Number.");
                            return;
                        } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                            regusername.setError("This username exists.");
                            return;
                        } else {
                            reference.child(getIntent().getStringExtra("username")).removeValue();
                            String accountype = "Rider";
                            helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"1","1",Address);
                            reference.child(username).setValue(helper_user);

                            SharedPreferences preferences1;
                            SharedPreferences.Editor editor;
                            preferences1 = getSharedPreferences("user",MODE_PRIVATE);
                            editor = preferences1.edit();
                            editor.putString("username",username);
                            editor.putString("accountype",accountype);
                            editor.putString("password",password);
                            editor.putString("address",Address);
                            editor.putString("longti",Destlongt);
                            editor.putString("lati",Deslati);
                            editor.putString("phone",phone);
                            editor.putString("fullname",fullname);
                            editor.putString("email",email);
                            editor.commit();

                            Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(signupstaff.this,rider_frame.class);
                            startActivity(intent);
                        }

                    }

                }else if(getIntent().getStringExtra("hasdata").equals("3")){
                    String fullname = regfullname.getText().toString();
                    String username = regusername.getText().toString();
                    String password = regpassword.getText().toString();
                    String email = regemail.getText().toString();
                    String storename = regstorename.getText().toString();
                    String phone = regphone.getText().toString();
                    String accountype = "Admin";
                    String Destlongt = longt.getText().toString();
                    String Deslati = lati.getText().toString();
                    String Address = address.getText().toString();
                    String image = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";

                    if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                            || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){
                        Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!isValidName(fullname) && fullname.length() < 2) {
                        regfullname.setError("Name should be valid.");
                        return;
                    } else if (!isValidEmail(email)) {
                        regemail.setError("Please use a Valid Email.");
                        return;
                    } else if (password.length() < 6) {
                        regpassword.setError("The password is too short.");
                        return;
                    } else if (phone.length() < 10) {
                        regphone.setError("Please Input Right Phone Number.");
                        return;
                    } else if (userFound(username) == 0 && !username.equals(getIntent().getStringExtra("username"))) {
                        regusername.setError("This username exists.");
                        return;
                    } else {
                        reference.child(getIntent().getStringExtra("username")).removeValue();


                        //view usally use for storeowner for their store rating
                        helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"1","1",Address);
                        reference.child(username).setValue(helper_user);

                        SharedPreferences preferences1;
                        SharedPreferences.Editor editor;
                        preferences1 = getSharedPreferences("user",MODE_PRIVATE);
                        editor = preferences1.edit();
                        editor.putString("username",username);
                        editor.putString("accountype",accountype);
                        editor.putString("password",password);
                        editor.putString("address",Address);
                        editor.putString("longti",Destlongt);
                        editor.putString("lati",Deslati);
                        editor.putString("phone",phone);
                        editor.putString("fullname",fullname);
                        editor.putString("email",email);
                        editor.commit();

                        Toast.makeText(signupstaff.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signupstaff.this,admin_frame.class);
                        startActivity(intent);
                    }

                }else{
                    String fullname = regfullname.getText().toString();
                    String username = regusername.getText().toString();
                    String password = regpassword.getText().toString();
                    String email = regemail.getText().toString();
                    String storename = regstorename.getText().toString();
                    String phone = regphone.getText().toString();
                    String accountype = "STAFF";
                    String Destlongt = "";
                    String Deslati = "";
                    String Address = "";
                    String image = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";

                    if(fullname.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                            || Destlongt.isEmpty() || Deslati.isEmpty() || Address.isEmpty()){ // one or more fields are empty
                        Toast.makeText(signupstaff.this,"Kindly fillup all fields",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!isValidName(fullname) && fullname.length() < 2) {
                        regfullname.setError("Name should be valid.");
                        return;
                    } else if (!isValidEmail(email)) {
                        regemail.setError("Please use a Valid Email.");
                        return;
                    } else if (password.length() < 6) {
                        regpassword.setError("The password is too short.");
                        return;
                    } else if (phone.length() < 10) {
                        regphone.setError("Please Input Right Phone Number.");
                        return;
                    } else if (userFound(username) == 0 ) {
                        regusername.setError("This username exists.");
                        return;
                    } else {
                        helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"0","1",Address);
                        reference.child(username).setValue(helper_user);

                        Toast.makeText(signupstaff.this,"Staff Registered",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(signupstaff.this,store_owner.class);
                        startActivity(intent);
                    }


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
            if (ActivityCompat.checkSelfPermission(signupstaff.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(signupstaff.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(signupstaff.this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(signupstaff.this, Locale.getDefault());
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

    private int userFound(String username) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(username).exists()) {
                    userfound = 0;
                } else {
                    userfound = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return userfound;
    }

    private void showPlacePicker() {
        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(lt, lh)
                .showLatLong(true)
                .setMapRawResourceStyle(R.raw.map_style)
                .setMapType(MapType.NORMAL)
                .build(signupstaff.this);

        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
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
