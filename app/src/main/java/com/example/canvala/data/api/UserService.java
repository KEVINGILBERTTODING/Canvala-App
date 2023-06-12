package com.example.canvala.data.api;

import com.airbnb.lottie.L;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.InformationModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.RekeningModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.TransactionsModel;
import com.example.canvala.data.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @FormUrlEncoded
    @POST("user/deletecart")
    Call<ResponseModel> deleteCart(
            @Field("id_cart") String idCart,
            @Field("id_product") String idProduct,
            @Field("stock") Integer stock
    );

    @GET("user/getallrekening")
    Call<List<RekeningModel>> getallrekening();

    @FormUrlEncoded
    @POST("user/getInformationOrder")
    Call<InformationModel> getInformationOrder(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("user/updateAlamat")
    Call<ResponseModel> updateAlamat(
            @Field("user_id") String userId,
            @Field("address") String address,
            @Field("phone_number") String phoneNumber,
            @Field("postal_code") String postalCode
    );

    @FormUrlEncoded
    @POST("user/checkOut")
    Call<ResponseModel> checkOut(
            @Field("user_id") String userId,
            @Field("total_price") Integer totalPrice,
            @Field("city") String city,
            @Field("rek_id") Integer rekId,
            @Field("weight_total") Integer weightTotal
    );


    @GET("user/getMyTransactions")
    Call<List<TransactionsModel>> getMyTransactions (
            @Query("user_id") String userId
    );

    @Multipart
    @POST("user/uploadBuktiTransfer")
    Call<ResponseModel> uploadBuktiTransfer(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part image
            );

    @GET("user/getProductByKategori")
    Call<List<ProductModel>> getProductByKategori(
            @Query("id") String id
    );


}
