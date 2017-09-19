package com.myapp.newapp.api.model;

import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class NewsRes extends BaseResponse {
    private List<News> Data;

    public List<News> getData() {
        return Data;
    }

    public void setData(List<News> data) {
        Data = data;
    }
}
