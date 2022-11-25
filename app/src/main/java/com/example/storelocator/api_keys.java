package com.example.storelocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class api_keys  extends AppCompatActivity {
    Button apibtn;
    EditText editApi,viewApi;
    protected void onCreate(Bundle savedInstancesState) {

        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_api);

        apibtn = findViewById(R.id.button2);
        editApi = findViewById(R.id.apikey);
        viewApi = findViewById(R.id.textView19);
        SharedPreferences sh = getSharedPreferences("api", MODE_PRIVATE);
        String s1 = sh.getString("key", "");
        if(!s1.equals("") || !s1.equals(null)){
            viewApi.setText(s1);
        }

        apibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences;
                SharedPreferences.Editor editor;

                preferences = getSharedPreferences("api",MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("key",editApi.getText().toString());
                editor.commit();
            }
        });
    }
}
