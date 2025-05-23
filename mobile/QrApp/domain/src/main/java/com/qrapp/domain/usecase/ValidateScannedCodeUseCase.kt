package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.Result
import javax.inject.Inject

class ValidateScannedCodeUseCase @Inject constructor(
    private val repository: QrRepository
) {
    suspend operator fun invoke(scannedSeed: String): Result<QrScanResult> {
        return repository.validateScannedCode(scannedSeed)
    }
}