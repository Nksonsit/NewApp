package com.myapp.newapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.newapp.R;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.helper.AdvancedSpannableString;
import com.myapp.newapp.helper.Functions;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgNews;
    private TextView txtTitle;
    private TextView txtCategory;
    private TextView txtDesc;
    private TextView txtAuthor;
    private TextView txtReadMore;
    private Context context;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        init();
    }

    private void init() {
        initToolbar();
        txtReadMore = (TextView) findViewById(R.id.txtReadMore);
        txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgNews = (ImageView) findViewById(R.id.imgNews);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        news = (News) getIntent().getSerializableExtra("news");
        if (news != null) {
            Glide.with(context).load(news.getImage()).into(imgNews);
            txtTitle.setText(news.getTitle());
            txtCategory.setText(Functions.getFormatedCategory(context, news.getCategory()));
            txtDesc.setText(news.getDescription());
            txtAuthor.setText("By " + news.getType());
            txtReadMore.setText(Functions.getPublisher(context,news.getPublisherId()));
            txtReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(news.getUrl()));
                    startActivity(i);
                }
            });
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
