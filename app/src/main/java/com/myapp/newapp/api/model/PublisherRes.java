package com.myapp.newapp.api.model;

import java.util.List;

/**
 * Created by ishan on 19-09-2017.
 */

public class PublisherRes extends BaseResponse {
    private List<Publisher> Data;

    public List<Publisher> getData() {
        return Data;
    }

    public void setData(List<Publisher> data) {
        Data = data;
    }
}
