package com.example.medicalstoreuser.data_layer.Dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,        // Firebase user ID
    val apiUserId: String? = null, // Flask API user_id
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val pinCode: String
)