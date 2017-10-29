package com.myapp.newapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myapp.newapp.R;
import com.myapp.newapp.api.ApiInterface;
import com.myapp.newapp.api.model.BaseResponse;
import com.myapp.newapp.api.model.SendMail;
import com.myapp.newapp.custom.TfButton;
import com.myapp.newapp.custom.TfEditText;
import com.myapp.newapp.custom.TfEditTextOld;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.PrefUtils;
import com.myapp.newapp.helper.ProgressBarHelper;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private TfEditTextOld edtEmailId;
    private TfButton btnSubmit;
    private Random random;
    private ProgressBarHelper progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        init();
        actionListener();
    }

    private void actionListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context,view);
                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, "Please check your internet connection");
                    return;
                }
                if (edtEmailId.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter your email id");
                    return;
                }

                if (!Functions.emailValidation(edtEmailId.getText().toString().trim())) {
                    Functions.showToast(context, "Please enter valid email id");
                    return;
                }

                String id = String.format("%04d", random.nextInt(10000));
                PrefUtils.setCode(context, id);
                progressBar.showProgressDialog();
                SendMail sendMail = new SendMail();
                sendMail.setEmail(edtEmailId.getText().toString().trim());
                sendMail.setCode(id);
                ApiInterface api = MyApplication.getRetrofit().create(ApiInterface.class);
                api.sendMail(sendMail).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        progressBar.hideProgressDialog();
                            Intent intent = new Intent(context, ConfirmCodeActivity.class);
                            intent.putExtra("email", edtEmailId.getText().toString().trim());
                            edtEmailId.setText("");
                            startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        progressBar.hideProgressDialog();
                        Functions.showToast(context, "Something went wrong please try again later");
                    }
                });
            }
        });
    }

    private void init() {
        initToolbar();

        edtEmailId = (TfEditTextOld) findViewById(R.id.edtEmailId);
        btnSubmit = (TfButton) findViewById(R.id.btnOk);
        progressBar = new ProgressBarHelper(context, false);
        random = new Random();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
