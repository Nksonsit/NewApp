package com.myapp.newapp.api.model;

/**
 * Created by ishan on 19-09-2017.
 */

public class BaseResponse {
    private int Status;
    private String Message;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
