package com.qrapp.domain.util

sealed class AppException : Exception() {
    object NetworkError : AppException()
    data class ApiError(val code: Int, val errorMessage: String?) : AppException()
    object UnknownError : AppException()
}