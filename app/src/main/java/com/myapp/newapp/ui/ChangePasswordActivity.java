package com.myapp.newapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myapp.newapp.R;
import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.call.GetEncPassword;
import com.myapp.newapp.api.model.BaseResponse;
import com.myapp.newapp.api.model.EncPasswordReq;
import com.myapp.newapp.api.model.SavePassword;
import com.myapp.newapp.custom.TfButton;
import com.myapp.newapp.custom.TfEditTextOld;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private Context context;
    private TfEditTextOld edtPassword;
    private TfEditTextOld edtConfirmPassword;
    private TfButton btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = this;
        init();
    }

    private void init() {
        edtPassword = (TfEditTextOld) findViewById(R.id.edtPassword);
        edtConfirmPassword = (TfEditTextOld) findViewById(R.id.edtConfirmPassword);
        btnOk = (TfButton) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context,view);
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, "Please check your internet connection");
                    return;
                }
                if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter password");
                    return;
                }

                if (edtConfirmPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter confirm password");
                    return;
                }

                if (!edtPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
                    Functions.showToast(context, "Password and confirm password should be same");
                    return;
                }

                EncPasswordReq encPasswordReq = new EncPasswordReq();
                encPasswordReq.setPassword1(edtPassword.getText().toString().trim());
                new GetEncPassword(context, encPasswordReq, new GetEncPassword.OnSuccess() {
                    @Override
                    public void onSuccess(String password) {
                        SavePassword savePassword=new SavePassword();
                        savePassword.setEmail(getIntent().getStringExtra("email"));
                        savePassword.setPassword(password);
                        ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
                        api.savePassword(savePassword).enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                if (response.body() != null && response.body().getStatus() == 1) {
                                    Functions.showToast(context,"Successfully change password");
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                } else {
                                    Functions.showToast(context, "Something went wrong please try again later");
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Functions.showToast(context, "Something went wrong please try again later");
                            }
                        });
                    }

                    @Override
                    public void onFail(String s) {
                        Functions.showToast(context, s);
                    }
                });

            }
        });
    }
}
