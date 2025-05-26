package com.qrapp.presentation.utils

import android.content.Context
import com.qrapp.domain.util.AppException
import com.qrapp.presentation.R

fun AppException.toErrorMessage(context: Context): String {
    return when (this) {
        is AppException.NetworkError -> context.getString(R.string.error_network)
        is AppException.UnknownError -> context.getString(R.string.error_unknown)
        is AppException.ApiError -> {
            errorMessage?.takeIf { it.isNotBlank() } ?: context.getString(R.string.error_unknown)
        }
    }
}