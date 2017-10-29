package com.myapp.newapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myapp.newapp.R;
import com.squareup.picasso.Picasso;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ImageView img1=(ImageView)findViewById(R.id.img1);
        ImageView img2=(ImageView)findViewById(R.id.img2);

        Picasso.with(this).load("https://crpost.in/uploads/post/1509164018340.jpeg").into(img1);
        Picasso.with(this).load("https://crpost.in/uploads/post/38624858f8e60cc13720b81b9b686a10b7b109c2.jpg").into(img2);
    }
}
