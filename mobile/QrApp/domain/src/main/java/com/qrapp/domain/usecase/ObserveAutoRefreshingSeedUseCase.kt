package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

class ObserveAutoRefreshingSeedUseCase @Inject constructor(
    private val repository: QrRepository
) {
    companion object {
        private const val DEFAULT_REFRESH_INTERVAL_MS = 30_000L
    }

    operator fun invoke(): Flow<Result<QrSeed>> = flow {
        while (currentCoroutineContext().isActive) {
            val result = repository.getSeed()
            emit(result)
            val expiresAt = (result as? Result.Success)?.data?.expiresAt
            val delayMillis = expiresAt?.let { it.toEpochMilli() - Instant.now().toEpochMilli() } ?: DEFAULT_REFRESH_INTERVAL_MS
            if (delayMillis > 0) delay(delayMillis) else delay(DEFAULT_REFRESH_INTERVAL_MS)
        }
    }
}

