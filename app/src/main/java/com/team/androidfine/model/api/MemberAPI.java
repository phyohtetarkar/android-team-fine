package com.team.androidfine.model.api;

import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.tuple.MemberTuple;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MemberAPI {

    @POST("member")
    Observable<Response<Void>> save(@Body Member member);

    @Multipart
    @POST("member/image")
    Observable<Response<String>> upload(@Part MultipartBody.Part image);

    @GET("member/{id}")
    Observable<Response<Member>> findById(@Path("id") int id);

    @GET("member")
    Observable<Response<List<Member>>> findAll();

    @GET("member/summary")
    Observable<Response<List<MemberTuple>>> findSummary();

    @DELETE("member/{id}")
    Observable<Response<Void>> delete(@Path("id") int id);
}
