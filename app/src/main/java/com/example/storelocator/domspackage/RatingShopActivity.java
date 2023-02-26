package com.example.storelocator.domspackage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.storelocator.R;

public class RatingShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_shop);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView shopname = (TextView) findViewById(R.id.shopNameRating);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button submit = findViewById(R.id.submitShopRating);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText review = findViewById(R.id.commentShop);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RatingBar rating = findViewById(R.id.ratingShop);

        String orderid = getIntent().getStringExtra("orderid");


    }
}