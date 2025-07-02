package com.example.medicalstoreadmin.ui_layer.navigations

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    object HomeScreen

    @Serializable
    object UserDetailScreen

    @Serializable
    data class OrderDetailScreen(
        val orderID: String,
        val userID: String? = "",
        val category: String,
        val orderDate: String,
        val productID: String,
        val isApproved: String,
        val productExpiryDate: String? = "",
        val productQuantity: String? = "",
        val totalPrice: String? = ""

    )

    @Serializable
    object AddProductScreen

    @Serializable
    object ProductScreen

    @Serializable
    object OrdersScreen

    @Serializable
    data class CheckOutScreen(
        val userID: String? = "",
        val value: Int? = 0
    )
}