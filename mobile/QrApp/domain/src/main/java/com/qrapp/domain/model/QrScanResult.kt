package com.qrapp.domain.model

data class QrScanResult(
    val seed: String,
    val isValid: Boolean,
    val reason: String? = null
)
