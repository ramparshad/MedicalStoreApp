package com.example.medicalstoreuser

sealed class State1<out T> {
    data class Success<out T>(val data: T) : State1<T>()
    data class Error(val message: String) : State1<Nothing>()
    object Loading : State1<Nothing>()
}




