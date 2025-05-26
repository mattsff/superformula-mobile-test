package com.qrapp.presentation.screen.generate

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.usecase.ObserveAutoRefreshingSeedUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import com.qrapp.presentation.utils.bitmap.QrCodeBitmapGenerator
import com.qrapp.presentation.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

// Nueva interfaz para generar el Bitmap

// Implementación concreta


@HiltViewModel
class QrViewModel @Inject constructor(
    private val observeAutoRefreshingSeedUseCase: ObserveAutoRefreshingSeedUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val qrCodeBitmapGenerator: QrCodeBitmapGenerator
) : ViewModel() {

    data class QrUiState(
        val isLoading: Boolean = false,
        val qrSeed: QrSeed? = null,
        val qrBitmap: Bitmap? = null,
        val error: AppException? = null,
        val isSeedExpired: Boolean = false,
        val timeLeft: String = "",
    )

    private val _uiState = MutableStateFlow(QrUiState(isLoading = true))
    val uiState: StateFlow<QrUiState> = _uiState.asStateFlow()

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
                            val qrBitmap = qrCodeBitmapGenerator.generate(result.data.seed)
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
        timerJob = viewModelScope.launch(dispatcherProvider.main()) {
            while (true) {
                val now = Instant.now()
                val duration = Duration.between(now, expiresAt)
                val seconds = duration.seconds
                if (seconds <= 0) {
                    _uiState.update { it.copy(timeLeft = "-") }
                    break
                } else {
                    val min = seconds / 60
                    val sec = seconds % 60
                    val formatted = String.format("%02d:%02d", min, sec)
                    _uiState.update { it.copy(timeLeft = formatted) }
                }
                withContext(dispatcherProvider.io()) {
                    delay(1000)
                }
            }
        }
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}
