package com.myapp.newapp.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.myapp.newapp.helper.Functions;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by dhruvil on 27-07-2016.
 */

public class TfEditText extends MaterialEditText {

    private Context _ctx;

    public TfEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    public TfEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this._ctx = context;
            init();
        }
    }

    private void init() {
        try {
            setFloatingLabel(FLOATING_LABEL_HIGHLIGHT);
//            setHintTextColor(ContextCompat.getColor(_ctx, R.color.half_black));
//            setPrimaryColor(ContextCompat.getColor(_ctx, R.color.half_black));
            setAccentTypeface(Functions.getRegularFont(_ctx));
            setTypeface(Functions.getRegularFont(_ctx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
