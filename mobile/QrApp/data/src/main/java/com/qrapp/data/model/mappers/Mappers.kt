package com.qrapp.data.model.mappers

import com.qrapp.data.model.QrScanResultDto
import com.qrapp.data.model.QrSeedDto
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import java.time.Instant

fun QrSeedDto.toDomain(): QrSeed {
    return QrSeed(
        seed = this.seed,
        expiresAt = Instant.ofEpochMilli(this.expires_at)
    )
}

fun QrScanResultDto.toDomain(): QrScanResult {
    return QrScanResult(
        isValid = this.valid,
        reason = this.reason
    )
}