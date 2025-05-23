package com.qrapp.data.model.mappers

import com.qrapp.domain.util.AppException
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

fun Exception.toRemoteException(): AppException {
    return when (this) {
        is HttpException -> {
            val code = this.code()
            val errorBody = this.response()?.errorBody()?.string()
            val errorMessage = extractErrorMessage(errorBody)

            AppException.ApiError(code, errorMessage)
        }

        is IOException -> AppException.NetworkError

        else -> AppException.UnknownError
    }
}

private fun extractErrorMessage(errorBody: String?): String? {
    return try {
        val json = JSONObject(errorBody ?: return null)
        json.optString("message", null)
    } catch (_: Exception) {
        null
    }
}