package com.myapp.newapp.api.model;

import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class CategoryRes extends BaseResponse {
    private List<Category> Data;

    public List<Category> getData() {
        return Data;
    }

    public void setData(List<Category> data) {
        Data = data;
    }
}
