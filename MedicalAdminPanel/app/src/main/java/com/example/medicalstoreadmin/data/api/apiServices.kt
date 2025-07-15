package com.example.medicalstoreadmin.data.api


import com.example.medicalstoreadmin.data.response.AddProductResponse
import com.example.medicalstoreadmin.data.response.UpdateProductResponse
import com.example.medicalstoreadmin.data.response.deleteSpecificUserResponse
import com.example.medicalstoreadmin.data.response.getAllOrdersResponse
import com.example.medicalstoreadmin.data.response.getAllProductsResponse
import com.example.medicalstoreadmin.data.response.getAllUsersResponse
import com.example.medicalstoreadmin.data.response.getSpecificProductResponse
import com.example.medicalstoreadmin.data.response.updateOrdersDetailsResponse
import com.example.medicalstoreadmin.data.response.updateUserDetailsResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface apiServices {

    @GET("getAllUsers")
    suspend fun getAllUsers() : Response<getAllUsersResponse>?

    @FormUrlEncoded
    @PATCH("updateUserDetails")
    suspend fun updateUserDetails(
        @Field("user_id") user_id : String,
        @Field("isApproved") isApproved : Int
    ) : Response<updateUserDetailsResponse>?

    @FormUrlEncoded
    @PATCH("updateOrderDetails")
    suspend fun updateOrderDetails(
        @Field("orderID") order_id : String,
        @Field("isApproved") isApproved : Int
    ) : Response<updateOrdersDetailsResponse>?

    @FormUrlEncoded
    @POST("ipProduct")
    suspend fun addProduct(
        @Field("Product Name") product_name : String,
        @Field("Expiry Date") product_expiry_date : String,
        @Field("Price") product_price : Float,
        @Field("Stock") stock : String,
        @Field("Category") product_category : String,
    ): Response<AddProductResponse>?

    @GET("get_all_products")
    suspend fun getAllProducts(): Response<getAllProductsResponse>?

    @GET("getAllOrders")
    suspend fun getAllOrdersServices(): Response<getAllOrdersResponse>?

    @DELETE("deleteSpecificUser/{userID}")
    suspend fun deleteSpecificUserServices(
        @Path("userID") user_id : String
    ) : Response<deleteSpecificUserResponse>?

    @FormUrlEncoded
    @POST("getSpecificProduct")
    suspend fun getSpecificProduct(
        @Field("productID") product_id : String
    ) : Response<getSpecificProductResponse>?

    @FormUrlEncoded
    @PATCH("updateProductDetails")
    suspend fun updateProductDetails(
        @Field("product_id") product_id : String,
        @Field("stock") stock : String
    ) : Response<UpdateProductResponse>?

    //   
    @DELETE("deleteAllUsers")
    suspend fun deleteAllUsers(): Response<GenericResponse>?

    @DELETE("deleteSpecificProduct/{productID}")
    suspend fun deleteSpecificProduct(
        @Path("productID") product_id: String
    ): Response<GenericResponse>?

    @DELETE("deleteAllProducts")
    suspend fun deleteAllProducts(): Response<GenericResponse>?

    @DELETE("deleteSpecificOrder/{orderID}")
    suspend fun deleteSpecificOrder(
        @Path("orderID") OrderId: String
    ): Response<GenericResponse>?
}

// Generic response data class to handle the Flask JSON response
data class GenericResponse(
    val status: Int,
    val message: String
)
