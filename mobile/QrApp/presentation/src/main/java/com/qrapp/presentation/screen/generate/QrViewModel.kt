package com.qrapp.presentation.screen.generate

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.usecase.ObserveAutoRefreshingSeedUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import com.qrapp.presentation.utils.generateQrCodeBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(
    private val observeAutoRefreshingSeedUseCase: ObserveAutoRefreshingSeedUseCase,
) : ViewModel() {

    data class GenerateUiState(
        val isLoading: Boolean = false,
        val qrSeed: QrSeed? = null,
        val qrBitmap: Bitmap? = null,
        val error: AppException? = null,
        val isSeedExpired: Boolean = false,
        val timeLeft: String = ""
    )

    private val _uiState = MutableStateFlow(GenerateUiState(isLoading = true))
    val uiState: StateFlow<GenerateUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        observeSeed()
    }

    private fun observeSeed() {
        observeAutoRefreshingSeedUseCase()
            .onEach { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is Result.Success -> {
                            val qrBitmap = generateQrCodeBitmap(result.data.seed)
                            startTimer(result.data.expiresAt)
                            currentState.copy(
                                isLoading = false,
                                qrSeed = result.data,
                                qrBitmap = qrBitmap,
                                error = null
                            )
                        }

                        is Result.Error -> {
                            timerJob?.cancel()
                            currentState.copy(
                                isLoading = false,
                                qrSeed = null,
                                qrBitmap = null,
                                error = result.exception,
                                timeLeft = ""
                            )
                        }
                    }
                }
            }
            .catch { e ->
                _uiState.update { currentState ->
                    timerJob?.cancel()
                    val exception = if (e is AppException) {
                        e
                    } else {
                        AppException.UnknownError
                    }
                    currentState.copy(
                        isLoading = false,
                        error = exception,
                        timeLeft = ""
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer(expiresAt: Instant) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                val now = Instant.now()
                val duration = Duration.between(now, expiresAt)
                val seconds = duration.seconds
                if (seconds <= 0) {
                    _uiState.update { it.copy(timeLeft = "Expirado") }
                    break
                } else {
                    val min = seconds / 60
                    val sec = seconds % 60
                    val formatted = String.format("%02d:%02d", min, sec)
                    _uiState.update { it.copy(timeLeft = formatted) }
                }
                delay(1000)
            }
        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}

