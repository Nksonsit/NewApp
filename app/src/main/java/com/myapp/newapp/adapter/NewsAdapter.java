package com.myapp.newapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myapp.newapp.R;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.helper.Functions;

import java.util.List;

/**
 * Created by ishan on 18-09-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnClickItem onClickItem;
    private List<News> list;
    private Context context;

    public NewsAdapter(Context context, List<News> list,OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem=onClickItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setValues(list.get(position));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClickItem(position);
            }
        });
        myViewHolder.imgNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClickItem(position);
            }
        });
        myViewHolder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgNews;
        private TextView txtTitle;
        private TextView txtCategory;
        private TextView txtTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            imgNews = (ImageView) itemView.findViewById(R.id.imgNews);
        }

        public void setValues(News news) {
            Glide.with(context).load(news.getImage()).into(imgNews);
            txtTitle.setText(news.getTitle());
            txtCategory.setText(Functions.getFormatedCategory(context,news.getCategory()));
            txtTime.setText(Functions.getDateFormated(news.getCreatedAt()));
        }
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }
}
