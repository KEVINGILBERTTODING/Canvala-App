package com.example.canvala.data.api;

import com.example.canvala.data.model.TransactionsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdminService {

    @GET("admin/getAllTransactionsByStatus")
    Call<List<TransactionsModel>> getAllTransactionsByStatus(
            @Query("status") String status
    );
}
