package com.example.canvala.data.api;

import com.example.canvala.data.model.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("user/getAllProduct")
    Call<List<ProductModel>> getAllProduct();
}
