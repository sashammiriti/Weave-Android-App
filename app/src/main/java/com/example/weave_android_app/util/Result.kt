package com.example.weave_android_app.util

// Sealed class to handle success and failure states in repository operations
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}