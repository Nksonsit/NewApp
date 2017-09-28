package com.myapp.newapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myapp.newapp.R;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.custom.TfTextView;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.ui.WebActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 30-11-2016.
 */
public class MyAdapter extends PagerAdapter {
    private Activity mActivity;
    private List<News> list;
    private LayoutInflater inflater;
    private OnClickItem onClickItem;

    public void setDataList(List<News> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        private ImageView imgNews;
        private TfTextView txtTitle;
        private TfTextView txtDesc;
        private TfTextView txtBy;
        private TfTextView txtTopic;
        private TfTextView txtReadMore;

    }

    public MyAdapter(Activity mActivity, List<News> list, OnClickItem onClickItem) {
        inflater = (LayoutInflater) mActivity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mActivity = mActivity;
        this.onClickItem = onClickItem;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;
        ViewHolder holder;

        view = inflater.inflate(R.layout.item_full_news, container, false);
        holder = new ViewHolder();
        Log.e("pos", position + "");
        holder.txtReadMore = (TfTextView) view.findViewById(R.id.txtReadMore);
        holder.txtTopic = (TfTextView) view.findViewById(R.id.txtTopic);
        holder.txtBy = (TfTextView) view.findViewById(R.id.txtBy);
        holder.txtDesc = (TfTextView) view.findViewById(R.id.txtDesc);
        holder.txtTitle = (TfTextView) view.findViewById(R.id.txtTitle);
        holder.imgNews = (ImageView) view.findViewById(R.id.imgNews);


        Glide.with(mActivity).load(list.get(position).getImage()).into(holder.imgNews);
        holder.txtReadMore.setText("Read more at " + Functions.getPublisher(mActivity, list.get(position).getPublisherId()));
        holder.txtTopic.setText("Topics : " + Functions.getFormatedCategory(mActivity, list.get(position).getCategory()));
        holder.txtBy.setText("By " + list.get(position).getType());
        holder.txtDesc.setText(list.get(position).getDescription());
        holder.txtTitle.setText(list.get(position).getTitle());

        holder.txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebActivity.class);
                i.putExtra("url", list.get(position).getUrl());
                Functions.fireIntent(mActivity, i, true);
            }
        });
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
//        super.destroyItem(container, position, object);
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }
}
