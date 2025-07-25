package com.example.medicalstoreadmin

sealed class State<out T> {
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
    object Loading : State<Nothing>()
}
// It is main backend state

