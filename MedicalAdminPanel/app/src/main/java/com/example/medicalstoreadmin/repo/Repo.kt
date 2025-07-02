package com.example.medicalstoreadmin.repo

import android.util.Log
import com.example.medicalstoreadmin.data.response.getAllUsersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.medicalstoreadmin.State
import com.example.medicalstoreadmin.data.api.ApiProvider
import com.example.medicalstoreadmin.data.api.GenericResponse
import com.example.medicalstoreadmin.data.response.AddProductResponse
import com.example.medicalstoreadmin.data.response.UpdateProductResponse
import com.example.medicalstoreadmin.data.response.deleteSpecificUserResponse
import com.example.medicalstoreadmin.data.response.getAllOrdersResponse
import com.example.medicalstoreadmin.data.response.getAllProductsResponse
import com.example.medicalstoreadmin.data.response.getSpecificProductResponse
import com.example.medicalstoreadmin.data.response.updateOrdersDetailsResponse
import com.example.medicalstoreadmin.data.response.updateUserDetailsResponse
import retrofit2.Response

class Repo {

    fun getAllProductsRepo(): Flow<State<Response<getAllProductsResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().getAllProducts()
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getAllUsersRepo(): Flow<State<Response<getAllUsersResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().getAllUsers()
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun updateUserDetailsRepo(
        user_id: String,
        isApproved: Int
    ): Flow<State<Response<updateUserDetailsResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi()
                .updateUserDetails(user_id = user_id, isApproved = isApproved)
            if (response!!.isSuccessful) {
                Log.d("Repo", "Response Body: ${response.body()}")
                emit(State.Success(response))
            } else {
                emit(State.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun updateOrderDetailsRepo(
        order_id: String,
        isApproved: Int
    ): Flow<State<Response<updateOrdersDetailsResponse>?>> = flow {

        emit(State.Loading)

        try {
            val response = ApiProvider.providerApi()
                .updateOrderDetails(order_id = order_id, isApproved = isApproved)
            if (response!!.isSuccessful) {
                Log.d("Repo", "Response Body: ${response.body()}")
                emit(State.Success(response))
            } else {
                emit(State.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun addProductDetailsRepo(
        product_name: String,
        product_expiry_date: String,
        product_price: String,
        stock: String,
        product_category: String
    ): Flow<State<Response<AddProductResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().addProduct(
                product_name = product_name,
                product_expiry_date = product_expiry_date,
                product_price = product_price.toFloat(),
                stock = stock,
                product_category = product_category
            )
            if (response!!.isSuccessful) {
                Log.d("Repo", "Response Body: ${response.body()}")
                emit(State.Success(response))
            } else {
                emit(State.Error("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getAllOrdersRepo(): Flow<State<Response<getAllOrdersResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().getAllOrdersServices()
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun deleteSpecificUserRepo(userId: String): Flow<State<Response<deleteSpecificUserResponse>?>> =
        flow {
            emit(State.Loading)

            try {
                val response = ApiProvider.providerApi().deleteSpecificUserServices(userId)
                emit(State.Success(response))
                Log.d("TAG", "I am in Repo for $userId")
            } catch (e: Exception) {
                emit(State.Error(e.message.toString()))
            }
        }

    fun getSpecificProduct(product_id: String): Flow<State<Response<getSpecificProductResponse>?>> =
        flow {
            emit(State.Loading)

            try {
                val response = ApiProvider.providerApi().getSpecificProduct(product_id = product_id)
                emit(State.Success(response))
            } catch (e: Exception) {
                emit(State.Error(e.message.toString()))
            }
        }

    fun updateProductDetails(
        product_id: String,
        stock: String
    ): Flow<State<Response<UpdateProductResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi()
                .updateProductDetails(product_id = product_id, stock = stock)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun deleteAllUsersRepo(): Flow<State<Response<GenericResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().deleteAllUsers()
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun deleteSpecificProductRepo(productId: String): Flow<State<Response<GenericResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().deleteSpecificProduct(productId)
            response?.let {
                if (it.isSuccessful) {
                    Log.d("Repo", "Product deleted: ${response.body()}")
                    emit(State.Success(response))
                } else {
                    emit(State.Error("Failed to delete product: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun deleteSpecificOrderRepo(orderId: String): Flow<State<Response<GenericResponse>?>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.providerApi().deleteSpecificOrder(orderId)
            if (response != null) {
                if (response.isSuccessful) {
                    Log.d("Repo", "Order deleted: ${response.body()}")
                    emit(State.Success(response))
                } else {
                    emit(State.Error("Failed to delete order: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }
}