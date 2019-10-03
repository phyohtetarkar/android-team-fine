package com.team.androidfine.model.api;

import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.FineTuple;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemberFineAPI {

    @POST("fine")
    Observable<Response<Void>> save(@Body MemberFine fine);

    @GET("fine/{id}")
    Observable<Response<MemberFine>> findById(@Path("id") long id);

    @GET("fine")
    Observable<Response<List<FineTuple>>> find(@Query("offset") int offset, @Query("limit") int limit);

    @DELETE("fine/{id}")
    Observable<Response<Void>> delete(@Path("id") long id);
}
