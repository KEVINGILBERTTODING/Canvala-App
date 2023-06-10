package com.example.canvala.data.api;

import com.example.canvala.data.model.AuthModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthModel> login(
            @Field("email") String email,
            @Field("password") String password
    );
}
