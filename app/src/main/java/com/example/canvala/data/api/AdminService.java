package com.example.canvala.data.api;

import com.example.canvala.data.model.CategoriesModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.RekeningModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.TransactionsDetailModel;
import com.example.canvala.data.model.TransactionsModel;
import com.example.canvala.data.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("admin/getAllRekening")
    Call<List<RekeningModel>> getAllRekening();

    @FormUrlEncoded
    @POST("admin/deleteRekening")
    Call<ResponseModel> deleteRekening(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/updateRekening")
    Call<ResponseModel> updateRekening(
            @Field("id") String id,
            @Field("bank_name") String bank_name,
            @Field("number") String number,
            @Field("rekening_name") String rekening_name
    );

    @FormUrlEncoded
    @POST("admin/insertRekening")
    Call<ResponseModel> insertRekening(
            @Field("bank_name") String bank_name,
            @Field("number") String number,
            @Field("rekening_name") String rekening_name
    );



    @GET("admin/getallusers")
    Call<List<UserModel>> getAllUsers();

    @FormUrlEncoded
    @POST("admin/insertUsers")
    Call<ResponseModel> insertUsers(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("address") String address,
            @Field("phone_number") String phone_number,
            @Field("postal_code") String postal_code,
            @Field("roles") String roles
    );

    @FormUrlEncoded
    @POST("admin/deleteUsers")
    Call<ResponseModel> deleteUser(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("admin/updateUsers")
    Call<ResponseModel> updateUsers(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("address") String address,
            @Field("phone_number") String phone_number,
            @Field("postal_code") String postal_code,
            @Field("roles") String roles
    );

    @FormUrlEncoded
    @POST("admin/deleteProduct")
    Call<ResponseModel> deleteProduct(
            @Field("id") String id
    );

    @Multipart
    @POST("admin/insertProduct")
    Call<ResponseModel> insertPorduk(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part image

            );

    // update product with image
    @Multipart
    @POST("admin/updateProduct")
    Call<ResponseModel> updateProductImage(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part filePart
    );

    // update product without update image product
    @Multipart
    @POST("admin/updateProduct")
    Call<ResponseModel> updateProduct(
            @PartMap Map<String, RequestBody> textData
    );

    @GET("admin/getProductById")
    Call<ProductModel> getProductById(
            @Query("id") String id
    );


}
