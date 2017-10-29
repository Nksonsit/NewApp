package com.myapp.newapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.myapp.newapp.R;
import com.myapp.newapp.adapter.CategoryAdapter;
import com.myapp.newapp.api.call.GetCategory;
import com.myapp.newapp.api.call.GetLogin;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.LoginReq;
import com.myapp.newapp.api.model.User;
import com.myapp.newapp.helper.Functions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class ForgotPasswordDialog extends Dialog {
    private OnOkClick onOkClick;
    private Context context;
    private View view;
    private TfButton btnOk;
    private TfEditTextOld edtEmailId;

    public ForgotPasswordDialog(@NonNull Context context, OnOkClick onOkClick) {
        super(context);
        this.onOkClick = onOkClick;
        this.context = context;

        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.forgot_password_dialog, null);
        setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);

        btnOk = (TfButton) view.findViewById(R.id.btnOk);
        edtEmailId = (TfEditTextOld) view.findViewById(R.id.edtEmailId);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                
            }
        });
    }


    public interface OnOkClick {
        void onOkClick(String output, String output2);
    }
}
