package com.qrapp.domain.repository

import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import kotlinx.coroutines.flow.Flow
import com.qrapp.domain.util.Result

interface QrRepository {
    suspend fun getSeed(): Result<QrSeed>
    suspend fun validateScannedCode(scannedSeed: String): Result<QrScanResult>
    fun observeAutoRefreshingSeed(): Flow<Result<QrSeed>>
}