package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.Result
import javax.inject.Inject

class GetSeedUseCase @Inject constructor(
    private val repository: QrRepository
) {
    suspend operator fun invoke(): Result<QrSeed> {
        return repository.getSeed()
    }
}