package com.myapp.newapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.myapp.newapp.R;
import com.myapp.newapp.adapter.CategoryAdapter;
import com.myapp.newapp.api.call.GetCategory;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.helper.Functions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class SelectCategoryDialog extends Dialog {
    private OnOkClick onOkClick;
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private List<Category> list;
    private CategoryAdapter adapter;
    private Button btnOk;

    public SelectCategoryDialog(@NonNull Context context, OnOkClick onOkClick) {
        super(context);
        this.onOkClick = onOkClick;
        this.context = context;

        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_select_category, null);
        setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);

        btnOk = (Button) view.findViewById(R.id.btnOk);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 20, 20, 20);
            }
        });
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setId(i);
            category.setCategoryImage("https://crpost.in/uploads/e17f5f38b6f383549cec3a2c1f74ff760f01c86c.png");
            category.setName("Automobile");
            category.setSelected(false);
            list.add(category);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new CategoryAdapter(context, list);
        recyclerView.setAdapter(adapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
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
                    String output = "";
                    String output2 = "";
                    for (int i = 0; i < selectedList.size(); i++) {
                        output = output + selectedList.get(i).getId();
                        output2 = output2 + selectedList.get(i).getName();
                        if (i != selectedList.size() - 1) {
                            output = output + ",";
                            output2 = output2 + ", ";
                        }
                    }
                    onOkClick.onOkClick(output,output2);
                    dismiss();
                } else {
                    Toast.makeText(context, "Please select Category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //callApi();
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
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(String s) {
                Functions.showToast(context, s);
            }
        });
    }

    public interface OnOkClick {
        void onOkClick(String output, String output2);
    }
}
