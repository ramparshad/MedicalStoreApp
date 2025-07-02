package com.example.medicalstoreuser.ui_layer.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object StartScreen

    @Serializable
    object LogInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object CartList

    @Serializable
    object OrderHistory

    @Serializable
    data class Profile(
        val name : String,
        val email : String,
        val phoneNumber : String,
        val address : String,
        val pinCode : String
    )

    @Serializable
    object Categories

    @Serializable
    data class ProductDetailsScreen(
        val product_id: String
    )

    @Serializable
    object SearchScreen

    @Serializable
    data class OrderDetailsScreen(
        val product_id: String
    )
}