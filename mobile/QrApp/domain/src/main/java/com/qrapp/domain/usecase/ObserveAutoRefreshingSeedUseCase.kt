package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAutoRefreshingSeedUseCase @Inject constructor(
    private val repository: QrRepository
) {
    operator fun invoke(): Flow<Result<QrSeed>> {
        return repository.observeAutoRefreshingSeed()
    }
}