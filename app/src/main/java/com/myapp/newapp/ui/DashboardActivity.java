package com.myapp.newapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.myapp.newapp.R;
import com.myapp.newapp.adapter.NewsAdapter;
import com.myapp.newapp.api.call.GetNews;
import com.myapp.newapp.api.call.GetPublisher;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.NewsReq;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private Context context;
    private TextView txtTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private FloatingActionButton fab;
    private List<News> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        init();
        actionListener();
    }

    private void actionListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {

        callApis();


        initToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        list = new ArrayList<>();
/*        for (int i = 0; i < 10; i++) {
            News news = new News();
            news.setId(i);
            news.setTitle("China reacts to India-Japan cooperation, says no room for 'third party #");
            news.setImage("https://crpost.in/uploads/post/829ff71851e662fe9b25d601e73a83994fdd038e.jpg");
            news.setDescription("China on Friday warned third parties against meddling in its boundary dispute with India, specifically mentioning the Arunachal Pradesh sector in an apparent response to Indo-Japanese plans to invest in infrastructure projects in the northeastern states.");
            news.setLink("china-reacts-to-india-japan-cooperation-says-no-room-for-third-party-53845");
            news.setUrl("http://www.hindustantimes.com/india-news/japan-should-not-get-involved-in-china-india-border-dispute-beijing/story-IE9M3uxuTGTT6j4HHgVCcM.html");
            news.setTitleTag("China reacts to India-Japan cooperation, says no room for \u0091third party");
            news.setMetaDes("China on Friday warned third parties against meddling in its boundary dispute with India, specifically mentioning the Arunachal Pradesh sector in an apparent response to Indo-Japanese plans to invest in infrastructure projects in the northeastern states.");
            news.setAuthorId("1");
            news.setType("admin");
            news.setPublisherId("1");
            news.setShareTitle("china-reacts-to-india-japan-cooperation-says-no-room-for-third-party");
            news.setCategory("2,3");
            news.setCreatedAt("2017-09-15 22:38:00");
            news.setUpdatedAt("2017-09-15 22:43:21");
            list.add(news);
        }*/

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 10, 10, 10);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new NewsAdapter(context, list, new NewsAdapter.OnClickItem() {
            @Override
            public void onClickItem(int position) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("news", list.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        getNews();
    }

    private void getNews() {
        NewsReq newsReq = new NewsReq();
        new GetNews(context, newsReq, new GetNews.OnSuccess() {
            @Override
            public void onSuccess(List<News> data) {
                if (data != null && data.size() > 0) {
                    list = data;
                    adapter.setDataList(list);
                }
            }

            @Override
            public void onFail(String s) {
                Functions.showToast(context, s);
            }
        });
    }

    private void callApis() {
/*        Publisher publisher = new Publisher();
        publisher.setId("1");
        publisher.setName("Hindustan Times");
        publisher.setImage("https://crpost.in/uploads/4c0a16a2d7235ad0a27f2bf6f3ab4a9b9b42551e.png");
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(publisher);
        PrefUtils.setPublisher(context, publishers);*/
        new GetPublisher(context, new GetPublisher.OnSuccess() {
            @Override
            public void onSuccess(List<Publisher> data) {
                if (data != null && data.size() > 0) {
                    PrefUtils.setPublisher(context, data);
                }
            }

            @Override
            public void onFail(String s) {
                Functions.showToast(context, s);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
