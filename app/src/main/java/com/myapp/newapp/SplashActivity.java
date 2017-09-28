package com.myapp.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.myapp.newapp.helper.PrefUtils;
import com.myapp.newapp.ui.CategoryActivity;
import com.myapp.newapp.ui.DashboardActivity;
import com.myapp.newapp.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (PrefUtils.isUserEntered(this) && PrefUtils.isUserLoggedIn(SplashActivity.this)) {
            if (PrefUtils.isUserLoggedIn(SplashActivity.this)) {
                if (PrefUtils.isCategorySelected(SplashActivity.this)) {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {

            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (PrefUtils.isUserLoggedIn(SplashActivity.this)) {
                        if (PrefUtils.isCategorySelected(SplashActivity.this)) {
                            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, CategoryActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
        }

    }
}
