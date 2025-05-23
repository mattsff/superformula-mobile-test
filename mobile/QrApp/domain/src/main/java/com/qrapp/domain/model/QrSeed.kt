package com.qrapp.domain.model

import java.time.Instant

data class QrSeed(
    val seed: String,
    val expiresAt: Instant
)