package com.qrapp.domain.util

sealed class AppException : Exception() {
    object NetworkError : AppException()
    data class ApiError(val code: Int, val errorBody: String?) : AppException()
    object UnknownError : AppException()
}