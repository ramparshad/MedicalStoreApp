package com.example.medicalstoreuser.data_layer.response

data class getSpecificUserResponse(
    val address: String,
    val block: Int,
    val date_of_account_creation: String,
    val email: String,
    val id: Int,
    val isApproved: Int,
    val level: Int,
    val name: Any,
    val password: String,
    val phone_number: String,
    val pinCode: String,
    val user_id: String
)