package com.myapp.newapp.api.call;

import android.content.Context;
import android.util.Log;

import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.CategoryRes;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.NewsReq;
import com.myapp.newapp.api.model.NewsRes;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.ProgressBarHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 19-09-2017.
 */

public class GetNews {
    private OnSuccess onSuccess;
    private ProgressBarHelper progressBar;
    private Context context;

    public GetNews(Context context , NewsReq newsReq, OnSuccess onSuccess) {
        this.context = context;
        this.onSuccess = onSuccess;
        progressBar = new ProgressBarHelper(context, false);
        callApi(newsReq);
    }

    private void callApi(NewsReq newsReq) {
        Log.e("req get news",MyApplication.getGson().toJson(newsReq));
        progressBar.showProgressDialog();
        ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
        api.getNews(newsReq).enqueue(new Callback<NewsRes>() {
            @Override
            public void onResponse(Call<NewsRes> call, Response<NewsRes> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null) {
                    Log.e("res get news",MyApplication.getGson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            onSuccess.onSuccess(response.body().getData());
                        } else {
                            onSuccess.onFail("Something went wrong please try again later.");
                        }
                    } else {
                        onSuccess.onFail("Something went wrong please try again later.");
                    }
                } else {
                    onSuccess.onFail("Something went wrong please try again later.");
                }
            }

            @Override
            public void onFailure(Call<NewsRes> call, Throwable t) {
                progressBar.hideProgressDialog();
                onSuccess.onFail("Something went wrong please try again later.");
            }
        });
    }

    public interface OnSuccess {
        void onSuccess(List<News> data);

        void onFail(String s);
    }
}
