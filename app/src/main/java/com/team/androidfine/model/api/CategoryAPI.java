package com.team.androidfine.model.api;

import com.team.androidfine.model.entity.Category;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryAPI {

    @POST("category")
    Observable<Response<Void>> save(@Body Category category);

    @GET("category/{id}")
    Observable<Response<Category>> findById(@Path("id") int id);

    @GET("category")
    Observable<Response<List<Category>>> findAll();

    @DELETE("category/{id}")
    Observable<Response<Void>> delete(@Path("id") int id);
}
