package com.qrapp.presentation.screen.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.usecase.ValidateScannedCodeUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import com.qrapp.presentation.utils.dispatcher.DispatcherProvider
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
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    data class ScanUiState(
        val isLoading: Boolean = false,
        val scanResult: QrScanResult? = null,
        val error: AppException? = null,
        val showPermissionDialog: Boolean = false,
        val wasPermissionDenied: Boolean = false,
        val shouldResetScanner: Boolean = false
    ) {
        val hasScanResult: Boolean get() = scanResult != null
    }

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onQrCodeScanned(scannedCode: String) {
        _uiState.update { it.copy(isLoading = true, error = null, shouldResetScanner = false) }

        viewModelScope.launch(dispatcherProvider.io()) {

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
                            error = result.exception,
                            shouldResetScanner = true // Reset scanner if API error
                        )
                    }
                }
            }


        }
    }

    fun resetScan() {
        _uiState.update {
            it.copy(
                scanResult = null,
                isLoading = false,
                error = null,
                shouldResetScanner = true
            )
        }
    }

    fun resetScanIfNeeded() {
        // Only reset scanner if error is not permission related
        _uiState.update {
            if (!it.showPermissionDialog && !it.wasPermissionDenied) {
                it.copy(shouldResetScanner = true)
            } else {
                it
            }
        }
    }

    fun onCameraPermissionResult(granted: Boolean, shouldShowRationale: Boolean) {
        _uiState.update {
            it.copy(
                wasPermissionDenied = !granted && !shouldShowRationale,
                showPermissionDialog = false
            )
        }
    }

    fun dismissCameraPermissionsDialog() {
        _uiState.update { it.copy(showPermissionDialog = false, wasPermissionDenied = false) }
    }

    fun showCameraPermissionDialog() {
        _uiState.update { it.copy(showPermissionDialog = true) }
    }

}
