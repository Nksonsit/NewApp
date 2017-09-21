package com.myapp.newapp.api.call;

import android.content.Context;
import android.util.Log;

import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.model.BaseResponse;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.ProgressBarHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 19-09-2017.
 */

public class AddNews {
    private OnSuccess onSuccess;
    private ProgressBarHelper progressBar;
    private Context context;

    public AddNews(Context context, News news, OnSuccess onSuccess) {
        this.context = context;
        this.onSuccess = onSuccess;
        progressBar = new ProgressBarHelper(context, false);
        callApi(news);
    }

    private void callApi(News news) {
        progressBar.showProgressDialog();
        ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
        api.addNews(news).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressBar.hideProgressDialog();
                Log.e("res add news",MyApplication.getGson().toJson(response.body()));
                if (response.body() != null) {
                    if (response.body().getStatus() == 1) {
                        onSuccess.onSuccess(response.body().getMessage());
                    } else {
                        onSuccess.onFail("Something went wrong please try again later.");
                    }
                } else {
                    onSuccess.onFail("Something went wrong please try again later.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                progressBar.hideProgressDialog();
                onSuccess.onFail("Something went wrong please try again later.");
            }
        });
    }

    public interface OnSuccess {
        void onSuccess(String data);

        void onFail(String s);
    }
}
