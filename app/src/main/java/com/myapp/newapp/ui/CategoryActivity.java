package com.myapp.newapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.newapp.R;
import com.myapp.newapp.adapter.CategoryAdapter;
import com.myapp.newapp.api.call.GetCategory;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Context context;
    private TextView txtTitle;
    private Button btnNext;
    private CategoryAdapter adapter;
    private List<Category> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        context = this;
        init();
        actionListerner();
    }

    private void actionListerner() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Category> dataList = adapter.getList();
                List<Category> selectedList = new ArrayList<Category>();

                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).isSelected()) {
                        selectedList.add(dataList.get(i));
                    }
                }

                if (selectedList.size() > 0) {
                    PrefUtils.setCategory(context,selectedList);
                    PrefUtils.setCategorySelected(context,true);
                    Intent intent = new Intent(context, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Please select Category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        initToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 20, 20, 20);
            }
        });
        btnNext = (Button) findViewById(R.id.btnNext);

        list = new ArrayList<>();
/*        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setId(i);
            category.setCategoryImage("https://crpost.in/uploads/e17f5f38b6f383549cec3a2c1f74ff760f01c86c.png");
            category.setName("Automobile");
            category.setSelected(false);
            list.add(category);
        }*/
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new CategoryAdapter(context, list);
        recyclerView.setAdapter(adapter);

        callApi();
    }

    private void callApi() {
        new GetCategory(context, new GetCategory.OnSuccess() {
            @Override
            public void onSuccess(List<Category> data) {
                if (data != null && data.size() > 0) {
                    list = data;
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(false);
                    }
                    adapter.setDataList(list);
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
