package com.myapp.newapp.helper;

/**
 * Created by anish on 22-02-2017.
 */

public interface ProgressListener {

    void showProgressDialog();

    void showProgressDialog(String message);

    void hideProgressDialog();

}
