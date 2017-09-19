package com.myapp.newapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myapp.newapp.R;
import com.myapp.newapp.api.call.GetRegister;
import com.myapp.newapp.api.model.RegisterReq;
import com.myapp.newapp.api.model.User;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmailId;
    private EditText edtPassword;
    private EditText edtCPassword;
    private Button btnRegister;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        init();
        actionListener();

    }

    private void actionListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setId(1);
                user.setEmail("test@gmail.com");
                user.setName("Ishan");
                user.setPassword("123456");

                PrefUtils.setLoggedIn(context, true);
                PrefUtils.setUserFullProfileDetails(context, user);

                Intent intent = new Intent(context, CategoryActivity.class);
                startActivity(intent);
                finish();

                /*if (!Functions.isConnected(context)) {
                    Functions.showToast(context, "Please check your internet connection");
                    return;
                }

                if (edtName.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter your name");
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
                if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter your password");
                    return;
                }

                if (edtCPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter confirm password");
                    return;
                }

                if (edtPassword.getText().toString().trim().length() < 6 || edtPassword.getText().toString().trim().length() >= 15) {
                    Functions.showToast(context, "Password must be between 6 to 15 character");
                    return;
                }

                if (edtCPassword.getText().toString().trim().length() < 6 || edtCPassword.getText().toString().trim().length() >= 15) {
                    Functions.showToast(context, "Confirm password must be between 6 to 15 character");

                    return;
                }

                if (edtPassword.getText().toString().trim().equals(edtCPassword.getText().toString().trim())) {
                    Functions.showToast(context, "Password and Confirm password must be same");
                    return;
                }

                RegisterReq registerReq = new RegisterReq();
                registerReq.setName(edtName.getText().toString().trim());
                registerReq.setEmail(edtEmailId.getText().toString().trim());
                registerReq.setPassword(edtPassword.getText().toString().trim());
                registerReq.setDeviceToken(Functions.getDeviceId(context));
                registerReq.setGcm("abcd");

                new GetRegister(context, registerReq, new GetRegister.OnSuccess() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            PrefUtils.setLoggedIn(context, true);
                            PrefUtils.setUserFullProfileDetails(context, user);

                            Intent intent = new Intent(context, CategoryActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(String s) {
                        Functions.showToast(context, s);
                    }
                });*/
            }
        });
    }

    private void init() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtName = (EditText) findViewById(R.id.edtName);
    }
}