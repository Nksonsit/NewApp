package com.myapp.newapp.api.call;

import android.content.Context;
import android.util.Log;

import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.model.LoginReq;
import com.myapp.newapp.api.model.RegisterRes;
import com.myapp.newapp.api.model.User;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.ProgressBarHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ishan on 19-09-2017.
 */

public class GetLogin {
    private OnSuccess onSuccess;
    private ProgressBarHelper progressBar;
    private Context context;

    public GetLogin(Context context, LoginReq loginReq, OnSuccess onSuccess) {
        this.context = context;
        this.onSuccess = onSuccess;
        progressBar = new ProgressBarHelper(context, false);
        callApi(loginReq);
    }

    private void callApi(LoginReq loginReq) {
        Log.e("req login",MyApplication.getGson().toJson(loginReq));
        progressBar.showProgressDialog();
        ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
        api.getLogin(loginReq).enqueue(new Callback<RegisterRes>() {
            @Override
            public void onResponse(Call<RegisterRes> call, Response<RegisterRes> response) {
                progressBar.hideProgressDialog();
                if (response.body() != null) {
                    Log.e("res login",MyApplication.getGson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            onSuccess.onSuccess(response.body().getData().get(0));
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
            public void onFailure(Call<RegisterRes> call, Throwable t) {
                progressBar.hideProgressDialog();
                onSuccess.onFail("Something went wrong please try again later.");
            }
        });
    }

    public interface OnSuccess {
        void onSuccess(User user);

        void onFail(String s);
    }
}
