package com.example.coffeein;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @Headers("Accept: application/json")
    @GET("product/read.php")
    Call<List<Product>> getProducts();

    @POST("orders/create.php")
    Call<Orders> createNewOrder(@Body Orders apiOrder);

    @POST("account/auth.php")
    Call<Account> authorization(@Body Account apiLogin);

    @POST("account/reg.php")
    Call<Account> registration(@Body Account apiRegistration);

    @POST("account/update.php")
    Call<Account> updateAcc(@Body Account apiUpdate);
}
