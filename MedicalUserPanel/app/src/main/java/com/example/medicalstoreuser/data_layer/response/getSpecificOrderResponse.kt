package com.example.medicalstoreuser.data_layer.response

data class getSpecificOrderResponse(
    val category: String,
    val id: Int,
    val order_date: String,
    val order_id: String,
    val product_expiry_date: String,
    val product_id: String,
    val quantity: Int,
    val total_price: Int,
    val user_id: Any,
    val isApproved : Int
)