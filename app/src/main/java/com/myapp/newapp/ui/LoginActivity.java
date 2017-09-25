package com.myapp.newapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myapp.newapp.R;
import com.myapp.newapp.api.call.AddGcm;
import com.myapp.newapp.api.call.GetCheckPassword;
import com.myapp.newapp.api.call.GetLogin;
import com.myapp.newapp.api.model.EncPasswordReq;
import com.myapp.newapp.api.model.GcmReq;
import com.myapp.newapp.api.model.LoginReq;
import com.myapp.newapp.api.model.User;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.PrefUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailId;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private CardView loginView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        init();
        actionListener();
    }

    private void actionListener() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context, view);
/*
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
*/

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
                if (edtPassword.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter your password");
                    return;
                }

                LoginReq loginReq = new LoginReq();
                loginReq.setEmail(edtEmailId.getText().toString().trim());
                loginReq.setPassword(edtPassword.getText().toString().trim());
                new GetLogin(context, loginReq, new GetLogin.OnSuccess() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            if (user.getIsApproved().trim().equals("1")) {
                                proceedLogin(user);
                            } else {
                                Functions.showAlertDialogWithTwoOpt(context, "Admin still not approved you. Try after some times", new Functions.DialogOptionsSelectedListener() {
                                    @Override
                                    public void onSelect(boolean isYes) {
                                    }
                                }, "OK");
                            }
                        }
                    }

                    @Override
                    public void onFail(String s) {
                        Functions.showToast(context, s);
                    }
                });
            }
        });
    }

    private void proceedLogin(final User user) {
        EncPasswordReq encPasswordReq = new EncPasswordReq();
        encPasswordReq.setPassword2(user.getPassword());
        encPasswordReq.setPassword1(edtPassword.getText().toString().trim());
        new GetCheckPassword(context, encPasswordReq, new GetCheckPassword.OnSuccess() {
            @Override
            public void onSuccess(String res) {
                if (res != null && res.trim().toLowerCase().contains("success")) {
                    PrefUtils.setLoggedIn(context, true);
                    PrefUtils.setUserFullProfileDetails(context, user);
                    GcmReq gcmReq = new GcmReq();
                    gcmReq.setDeviceToken(Functions.getDeviceId(context));
                    gcmReq.setGcm(PrefUtils.getFCMToken(context));
                    new AddGcm(context, gcmReq, new AddGcm.OnSuccess() {
                        @Override
                        public void onSuccess(String data) {

                        }

                        @Override
                        public void onFail(String s) {

                        }
                    });

                    Intent intent = new Intent(context, CategoryActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Functions.showToast(context, "Wrong credential");
                }
            }

            @Override
            public void onFail(String s) {

            }
        });
    }

    private void init() {
        loginView = (CardView) findViewById(R.id.loginView);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
    }
}
