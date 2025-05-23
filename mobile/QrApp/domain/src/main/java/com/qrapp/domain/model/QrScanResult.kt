package com.qrapp.domain.model

data class QrScanResult(
    val isValid: Boolean,
    val reason: String? = null
)
