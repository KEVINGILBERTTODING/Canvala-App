package com.example.canvala.data.api;

import com.example.canvala.data.model.CategoriesModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.TransactionsDetailModel;
import com.example.canvala.data.model.TransactionsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminService {

    @GET("admin/getAllTransactionsByStatus")
    Call<List<TransactionsModel>> getAllTransactionsByStatus(
            @Query("status") String status
    );

    @FormUrlEncoded
    @POST("admin/konfirmasiTransaction")
    Call<ResponseModel> konfirmasiTrans(
            @Field("trans_id") String transId
    );

    @FormUrlEncoded
    @POST("admin/terkirimTransaction")
    Call<ResponseModel> terkirimTransaction(
            @Field("trans_id") String transId,
            @Field("penerima") String penerima
    );

    @GET("admin/getDetailTransactions")
    Call<List<TransactionsDetailModel>> getDetailTransactions(
            @Query("trans_id") String transId
    );

    @GET("admin/getCategories")
    Call<List<CategoriesModel>> getCategories();

    @FormUrlEncoded
    @POST("admin/deleteCategories")
    Call<ResponseModel> deleteCategories(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/insertCategories")
    Call<ResponseModel> insertCategories(
            @Field("category_name") String categoryName
    );


}
