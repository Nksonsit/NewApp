package com.myapp.newapp.api.call;

import android.content.Context;

import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.CategoryRes;
import com.myapp.newapp.api.model.LoginReq;
import com.myapp.newapp.api.model.RegisterRes;
import com.myapp.newapp.api.model.User;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.ProgressBarHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 19-09-2017.
 */

public class GetCategory {
    private OnSuccess onSuccess;
    private ProgressBarHelper progressBar;
    private Context context;

    public GetCategory(Context context, OnSuccess onSuccess) {
        this.context = context;
        this.onSuccess = onSuccess;
        progressBar = new ProgressBarHelper(context, false);
        callApi();
    }

    private void callApi() {
        progressBar.showProgressDialog();
        ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
        api.getCategory().enqueue(new Callback<CategoryRes>() {
            @Override
            public void onResponse(Call<CategoryRes> call, Response<CategoryRes> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null) {
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
            public void onFailure(Call<CategoryRes> call, Throwable t) {
                progressBar.hideProgressDialog();
                onSuccess.onFail("Something went wrong please try again later.");
            }
        });
    }

    public interface OnSuccess {
        void onSuccess(List<Category> data);

        void onFail(String s);
    }
}
