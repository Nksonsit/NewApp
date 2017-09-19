package com.myapp.newapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myapp.newapp.R;
import com.myapp.newapp.api.model.Publisher;

import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class CategorySpinnerAdapter  extends ArrayAdapter<Publisher> {

    private List<Publisher> models;

    private Context context;
    private int textViewResourceId;
    private LayoutInflater inflater;

    public CategorySpinnerAdapter(Context context, int textViewResourceId, List<Publisher> models) {
        super(context, textViewResourceId);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.models = models;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Publisher getItem(int position) {
        return models.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = inflater.inflate(textViewResourceId, parent, false);

        TextView txtItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        txtItem.setText(models.get(position).getName());

        return convertView;
    }
}

