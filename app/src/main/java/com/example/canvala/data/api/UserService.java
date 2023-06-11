package com.example.canvala.data.api;

import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("user/getAllProduct")
    Call<List<ProductModel>> getAllProduct();

    @GET("user/getUserById")
    Call<UserModel> getMyProfile(
            @Query("id") String id
    );

    @GET("user/getCart")
    Call<List<CartModel>> getMyCart(
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseModel> register(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("password") String password,
            @Field("phone_number") String phoneNumber
    );


    @FormUrlEncoded
    @POST("user/addProductCart")
    Call<ResponseModel> addProductToCart(
            @Field("qty") Integer qty,
            @Field("user_id") String userId,
            @Field("product_id") String productId
    );
}
