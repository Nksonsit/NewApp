package com.myapp.newapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myapp.newapp.R;
import com.myapp.newapp.api.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 18-09-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Category> list;
    private Context context;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setValues(list.get(position));

        if (list.get(position).isSelected()) {
            myViewHolder.llCategory.setBackgroundResource(R.drawable.red_border_category);
        } else {
            myViewHolder.llCategory.setBackgroundResource(R.drawable.border_category);
        }
        myViewHolder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);
                } else {
                    list.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            }
        });

        myViewHolder.txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);
                } else {
                    list.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Category> getList() {
        return list;
    }

    public void setDataList(List<Category> list) {
        this.list=new ArrayList<>();
        this.list=list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView txtCategory;
        private LinearLayout llCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            llCategory = (LinearLayout) itemView.findViewById(R.id.llCategory);
        }

        public void setValues(Category category) {
            Glide.with(context).load(category.getCategoryImage()).apply(RequestOptions.circleCropTransform()).into(imgCategory);
            txtCategory.setText(category.getName());
        }
    }
}
