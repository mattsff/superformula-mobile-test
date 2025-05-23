package com.qrapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QrSeedDto(
    val seed: String,
    val expires_at: Long
)