package com.example.medicalstoreuser.data_layer

import com.example.medicalstoreuser.data_layer.response.LogInResponse
import com.example.medicalstoreuser.data_layer.response.createOrderResponse
import com.example.medicalstoreuser.data_layer.response.getAllOrdersResponse
import com.example.medicalstoreuser.data_layer.response.getAllProductsResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificOrderResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificProductResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificUserResponse
import com.example.medicalstoreuser.data_layer.response.signUpResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface apiServices {

    @FormUrlEncoded
    @POST("signUp")
    suspend fun signUpUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String,
        @Field("pinCode") pinCode: String,
        @Field("address") address: String
    ): Response<signUpResponse>


    @FormUrlEncoded
    @POST("login")
    suspend fun logInUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LogInResponse>


    @GET("get_all_products")
    suspend fun getAllProducts() : Response<getAllProductsResponse>?

    @FormUrlEncoded
    @POST("getSpecificProduct")
    suspend fun getSpecificProduct(
        @Field("productID") product_id : String
    ) : Response<getSpecificProductResponse>?

    @FormUrlEncoded
    @POST("getOrders")
    suspend fun createOrder(
        @Field("userID") userId : String,
        @Field("productID") productId : String,
        @Field("quantity") quantity : String,
        @Field("orderDate") orderDate : String
    ) : Response<createOrderResponse>?

    @GET("getAllOrders")
    suspend fun getAllOrders() : Response<getAllOrdersResponse>?

    @FormUrlEncoded
    @POST("getSpecificOrder")
    suspend fun getSpecificOrder(
        @Field("orderID") orderId : String
    ): Response<getSpecificOrderResponse>?

    @FormUrlEncoded
    @POST("getSpecificUser")
    suspend fun getSpecificUser(
        @Field("userID") userId : String
    ): Response<getSpecificUserResponse>?


}