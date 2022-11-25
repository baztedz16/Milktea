package com.example.storelocator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;


public class signupstaff extends AppCompatActivity {
    private static final int REQUEST_PLACE_PICKER = 1001;
    TextInputEditText regfullname,regusername,regpassword,regemail,regstorename,regphone,longt,lati,address;
    Spinner regacc;
    Button buttonSignup,pickLocationBtn;
    ProgressBar progressBar;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 999;



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

        wifiManager= (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        buttonSignup = findViewById(R.id.signupBtn);

        progressBar = findViewById(R.id.progress);
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


                //view usally use for storeowner for their store rating
                helper_user helper_user = new helper_user(fullname,username,password,email,storename,phone,accountype,Destlongt,Deslati,image,"0","0",Address);
                reference.child(username).setValue(helper_user);

                Toast.makeText(signupstaff.this,"Successfully Register",Toast.LENGTH_SHORT).show();

            }
        });

        //Start ProgressBar first (Set visibility VISIBLE)


    }
    private void showPlacePicker() {
        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(40.748672, -73.985628)
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
