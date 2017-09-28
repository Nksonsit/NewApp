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
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.StackTransformer;
import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;

public class DashboardActivity extends AppCompatActivity {

    private Context context;
    private TextView txtTitle;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private FloatingActionButton fab;
    private List<News> list;
    private ImageView imgEmpty;
    private TextView txtEmpty;
    private VerticalViewPager viewPager;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        PrefUtils.setEntered(context,true);
        init();
        actionListener();

        Log.e("fcm", FirebaseInstanceId.getInstance().getToken());
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
        imgEmpty = (ImageView) findViewById(R.id.imgEmpty);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        imgEmpty.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);
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



        viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        viewPager.setPageTransformer(false, new ZoomOutTransformer());
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
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        imgEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    recyclerView.setVisibility(View.GONE);
                    adapter.setDataList(list);
                    myAdapter=new MyAdapter(DashboardActivity.this, list, new MyAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {

                        }
                    });
                    viewPager.setAdapter(myAdapter);
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
                Intent intent=new Intent(context, ChangeCategoryActivity.class);
                Functions.fireIntent(context,intent,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
