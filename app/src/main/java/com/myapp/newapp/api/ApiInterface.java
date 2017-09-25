package com.myapp.newapp.api;

import com.myapp.newapp.api.model.BaseResponse;
import com.myapp.newapp.api.model.CategoryRes;
import com.myapp.newapp.api.model.EncPasswordReq;
import com.myapp.newapp.api.model.GcmReq;
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

    @POST("mobileapi/ContributorLogin.php")
    Call<RegisterRes> getLogin(@Body LoginReq loginReq);

    @POST("mobileapi/ContributorRegister.php")
    Call<RegisterRes> getRegister(@Body RegisterReq registerReq);

    @POST("api/encpassword")
    Call<BaseResponse> getEncPassword(@Body EncPasswordReq encPasswordReq);

    @POST("api/checkpassword")
    Call<BaseResponse> getCheckPassword(@Body EncPasswordReq encPasswordReq);

    @GET("mobileapi/GetCategories.php")
    Call<CategoryRes> getCategory();

    @GET("mobileapi/GetPublisher.php")
    Call<PublisherRes> getPublisher();

    @POST("mobileapi/GetNews.php")
    Call<NewsRes> getNews(@Body NewsReq newsReq);

    @POST("mobileapi/AddNews.php")
    Call<BaseResponse> addNews(@Body News news);

    @POST("mobileapi/AddGcm.php")
    Call<BaseResponse> addGcm(@Body GcmReq gcmReq);
}
