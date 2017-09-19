package com.myapp.newapp.api.model;

import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class RegisterRes extends BaseResponse{
    private List<User> Data;

    public List<User> getData() {
        return Data;
    }

    public void setData(List<User> data) {
        Data = data;
    }
}
