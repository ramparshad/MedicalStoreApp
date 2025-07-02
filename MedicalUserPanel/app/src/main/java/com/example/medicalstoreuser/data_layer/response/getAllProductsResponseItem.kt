package com.example.medicalstoreuser.data_layer.response

data class getAllProductsResponseItem(
    val category: String,
    val expiry_date: String,
    val id: Int,
    val price: Int,
    val product_id: String,
    val product_name: String,
    val stock: Int
)