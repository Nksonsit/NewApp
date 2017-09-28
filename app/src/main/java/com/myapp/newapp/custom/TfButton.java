package com.myapp.newapp.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import com.myapp.newapp.helper.Functions;


/**
 * Created by dhruvil on 27-07-2016.
 */

public class TfButton extends AppCompatButton {

    private Context _ctx;

    public TfButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    public TfButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    private void init() {
        try {
            setTypeface(Functions.getBoldFont(_ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
