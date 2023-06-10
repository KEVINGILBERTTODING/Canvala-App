package com.example.canvala.data.api;

import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
