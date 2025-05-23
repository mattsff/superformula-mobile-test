package com.qrapp.data.model
import kotlinx.serialization.Serializable

@Serializable
data class QrScanResultDto(
    val valid: Boolean,
    val reason: String
)