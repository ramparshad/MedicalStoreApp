package com.example.medicalstoreadmin.data.response

data class getAllOrdersResponseItem(
    val category: String,
    val id: Int,
    val order_date: String,
    val order_id: String,
    val product_expiry_date: String,
    val product_id: String,
    val quantity: Int,
    val total_price: Int,
    val user_id: String? = "",
    val isApproved: Int
)