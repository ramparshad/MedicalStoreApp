package com.example.medicalstoreuser.data_layer.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE uid = :userId")
    suspend fun getUser(userId: String): UserEntity?

    @Delete
    suspend fun deleteUser(user: UserEntity)
}