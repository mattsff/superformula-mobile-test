package com.qrapp.data.repository

import com.qrapp.data.datasource.QrRemoteDataSource
import com.qrapp.data.model.mappers.toRemoteException
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val remoteDataSource: QrRemoteDataSource,
) : QrRepository {

    override suspend fun getSeed(): Result<QrSeed> {
        return try {
            val seed = remoteDataSource.getSeed()
            Result.Success(seed)
        } catch (e: Exception) {
            Result.Error(e.toRemoteException())
        }
    }

    override suspend fun validateScannedCode(scannedSeed: String): Result<QrScanResult> {
        return try {
            val result = remoteDataSource.validateScannedCode(scannedSeed)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e.toRemoteException())
        }
    }

    override fun observeAutoRefreshingSeed(): Flow<Result<QrSeed>> = flow {
        while (true) {
            val result = getSeed()
            emit(result)

            val now = Instant.now()
            val seedTime = (result as? Result.Success)?.data?.expiresAt ?: now
            val delayDuration = Duration.between(now, seedTime).toMillis()
            delay(delayDuration.coerceAtLeast(1000L))
        }
    }
}