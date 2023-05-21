package com.asignment.gymmanager.retrofit;

import com.asignment.gymmanager.dto.AdminRequest;
import com.asignment.gymmanager.model.Blog;
import com.asignment.gymmanager.model.admin.Admin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("blog/")
    public List<Blog> getBlogForHome();
    @POST("login/")
    public Call<Admin> adminLogin(@Body AdminRequest admin);
}
