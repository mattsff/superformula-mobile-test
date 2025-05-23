package com.qrapp.data.datasource

import com.qrapp.data.model.mappers.toDomain
import com.qrapp.data.remote.QrApiService
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import javax.inject.Inject

class QrRemoteDataSourceImpl @Inject constructor(
    private val api: QrApiService
) : QrRemoteDataSource {

    override suspend fun getSeed(): QrSeed {
        return api.getSeed().toDomain()
    }

    override suspend fun validateScannedCode(scannedSeed: String): QrScanResult {
        return api.validateSeed(scannedSeed).toDomain()
    }
}