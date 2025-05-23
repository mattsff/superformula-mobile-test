package com.qrapp.data.datasource

import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed

interface QrRemoteDataSource {
    suspend fun getSeed(): QrSeed
    suspend fun validateScannedCode(scannedSeed: String): QrScanResult
}