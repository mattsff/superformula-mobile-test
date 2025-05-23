package com.qrapp.presentation.screen.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.usecase.ValidateScannedCodeUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val validateScannedCodeUseCase: ValidateScannedCodeUseCase,
) : ViewModel() {

    data class ScanUiState(
        val isLoading: Boolean = false,
        val scanResult: QrScanResult? = null,
        val error: AppException? = null,
    )

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onQrCodeScanned(scannedCode: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {

            when (val result = validateScannedCodeUseCase(scannedCode)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            scanResult = result.data,
                            error = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            scanResult = null,
                            error = result.exception
                        )
                    }
                }
            }


        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}