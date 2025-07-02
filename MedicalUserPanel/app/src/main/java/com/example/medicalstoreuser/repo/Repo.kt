package com.example.medicalstoreuser.repo

import android.util.Log
import com.example.medicalstoreuser.State1
import com.example.medicalstoreuser.data_layer.ApiProvider
import com.example.medicalstoreuser.data_layer.response.LogInResponse
import com.example.medicalstoreuser.data_layer.response.createOrderResponse
import com.example.medicalstoreuser.data_layer.response.getAllOrdersResponse
import com.example.medicalstoreuser.data_layer.response.getAllProductsResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificOrderResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificProductResponse
import com.example.medicalstoreuser.data_layer.response.getSpecificUserResponse
import com.example.medicalstoreuser.data_layer.response.signUpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repo {
    fun signUpRepo(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        pinCode: String,
        address: String
    ): Flow<State1<Response<signUpResponse>?>> = flow {
        emit(State1.Loading)
        try {
            val response = ApiProvider.providerApi().signUpUser(
                name, email, phoneNumber, password, pinCode, address
            )
            if (response?.isSuccessful == true) {
                Log.d("Repo", "SignUp Response: ${response.body()}")
                emit(State1.Success(response))
            } else {
                emit(State1.Error("Error: ${response?.code()} - ${response?.message()}"))
            }
        } catch (e: Exception) {
            emit(State1.Error(e.message.toString()))
        }
    }

    fun logInUser(email: String, password: String): Flow<State1<Response<LogInResponse>?>> =
        flow{
            emit(State1.Loading)
            try {
                val response = ApiProvider.providerApi().logInUser(
                    email = email,
                    password = password
                )
                emit(State1.Success(response))
                Log.d("Response" , "$response")
            }
            catch (e : Exception){
                emit(State1.Error(e.message.toString()))
            }
        }

    fun getAllProducts(): Flow<State1<Response<getAllProductsResponse>?>> = flow {
        emit(State1.Loading)

        try{
            val response = ApiProvider.providerApi().getAllProducts()
            emit(State1.Success(response))
        }
        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }
    }

    fun getSpecificProductRepo(product_id : String) : Flow<State1<Response<getSpecificProductResponse>?>> = flow {

        emit(State1.Loading)

        try{
            val response = ApiProvider.providerApi().getSpecificProduct(product_id)
            emit(State1.Success(response))
        }
        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }
    }

    fun createOrderRepo(userId : String, productId : String, quantity : String, orderDate : String) : Flow<State1<Response<createOrderResponse>?>> = flow {

        emit(State1.Loading)

        try{
            val response = ApiProvider.providerApi().createOrder(userId, productId, quantity, orderDate)
            emit(State1.Success(response))
        }

        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }

    }

    fun getAllOrdersRepo() : Flow<State1<Response<getAllOrdersResponse>?>> = flow {
        emit(State1.Loading)

        try {
            val response = ApiProvider.providerApi().getAllOrders()
            emit(State1.Success(response))
        }
        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }
    }

    fun getSpecificOrderRepo(order_id : String) : Flow<State1<Response<getSpecificOrderResponse>?>> = flow {
        emit(State1.Loading)

        try {
            val response = ApiProvider.providerApi().getSpecificOrder(order_id)
            emit(State1.Success(response))
        }
        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }
    }

    fun getSpecificUserRepo(user_id : String) : Flow<State1<Response<getSpecificUserResponse>?>> = flow {

        emit(State1.Loading)

        try {
            val response = ApiProvider.providerApi().getSpecificUser(user_id)
            emit(State1.Success(response))
        }
        catch (e : Exception){
            emit(State1.Error(e.message.toString()))
        }
    }
}