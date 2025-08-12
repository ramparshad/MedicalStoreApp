package com.example.medicalstoreuser.data_layer

import com.example.medicalstoreuser.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    fun providerApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
            build()
        )
        .addConverterFactory(
            GsonConverterFactory
            .create()
        ).build()
        .create(apiServices::class.java)
}



