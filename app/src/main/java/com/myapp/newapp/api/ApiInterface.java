package com.myapp.newapp.api;

import com.myapp.newapp.api.model.BaseResponse;
import com.myapp.newapp.api.model.CategoryRes;
import com.myapp.newapp.api.model.LoginReq;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.NewsReq;
import com.myapp.newapp.api.model.NewsRes;
import com.myapp.newapp.api.model.PublisherRes;
import com.myapp.newapp.api.model.RegisterReq;
import com.myapp.newapp.api.model.RegisterRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ishan on 18-09-2017.
 */

public interface ApiInterface {

    @POST("ContributorLogin.php")
    Call<RegisterRes> getLogin(@Body LoginReq loginReq);

    @POST("ContributorRegister.php")
    Call<RegisterRes> getRegister(@Body RegisterReq registerReq);

    @GET("GetCategories.php")
    Call<CategoryRes> getCategory();

    @GET("GetPublisher.php")
    Call<PublisherRes> getPublisher();

    @POST("GetNews.php")
    Call<NewsRes> getNews(@Body NewsReq newsReq);


    @POST("AddNews.php")
    Call<BaseResponse> addNews(@Body News news);
}
