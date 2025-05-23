package com.qrapp.domain.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: AppException) : Result<Nothing>()
}