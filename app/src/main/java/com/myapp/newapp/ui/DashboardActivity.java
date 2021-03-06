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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.myapp.newapp.R;
import com.myapp.newapp.adapter.MyAdapter;
import com.myapp.newapp.adapter.NewsAdapter;
import com.myapp.newapp.api.call.GetNews;
import com.myapp.newapp.api.call.GetPublisher;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.NewsReq;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.custom.TfTextView;
import com.myapp.newapp.helper.AppConstants;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.StackTransformer;
import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;

public class DashboardActivity extends AppCompatActivity {

    private Context context;
    private TfTextView txtTitle;
    private Toolbar toolbar;
    private List<News> list;
    private ImageView imgEmpty;
    private TfTextView txtEmpty;
    private VerticalViewPager viewPager;
    private MyAdapter myAdapter;
    private ImageView imgRefresh;
    private ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        PrefUtils.setEntered(context, true);
        init();
        actionListener();

    }

    private void actionListener() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNewsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNews();
            }
        });
    }

    private void init() {

        callApis();


        initToolbar();
        imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
        imgEmpty = (ImageView) findViewById(R.id.imgEmpty);
        txtEmpty = (TfTextView) findViewById(R.id.txtEmpty);
        imgEmpty.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);

        list = new ArrayList<>();

        viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
//        viewPager.setPageTransformer(false, new ZoomOutTransformer());
        viewPager.setPageTransformer(true, new StackTransformer());

        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);


        getNews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNews();
    }

    private void getNews() {
        NewsReq newsReq = new NewsReq();

        List<Category> selectCat = PrefUtils.getCategories(context);
        String catId = "";
        for (int i = 0; i < selectCat.size(); i++) {
            catId = catId + selectCat.get(i).getId();
            if (i != selectCat.size() - 1) {
                catId = catId + ",";
            }
        }
        newsReq.setCategory(catId);
        new GetNews(context, newsReq, new GetNews.OnSuccess() {
            @Override
            public void onSuccess(List<News> data) {
                if (data != null && data.size() > 0) {
                    list = data;
                    if (list.size() > 0) {
                        imgEmpty.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        myAdapter = new MyAdapter(DashboardActivity.this, list, new MyAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {

                            }
                        });


                        viewPager.setAdapter(myAdapter);
                        if (getIntent() != null) {
                            String titleMsg = getIntent().getStringExtra(AppConstants.INTENT_NOTIFICATION_MSG);
                            if (titleMsg != null && !titleMsg.isEmpty()) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getTitle().toLowerCase().equals(titleMsg.toLowerCase())) {
                                        viewPager.setCurrentItem(i);
                                        break;
                                    }
                                }
                            }
                        }

                    } else {
                        imgEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.GONE);
                    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                Functions.logout(context);
                break;

            case R.id.menuCategory:
                Intent intent = new Intent(context, ChangeCategoryActivity.class);
                Functions.fireIntent(context, intent, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
