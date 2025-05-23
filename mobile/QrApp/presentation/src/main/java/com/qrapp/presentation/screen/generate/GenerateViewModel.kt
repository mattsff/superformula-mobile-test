package com.qrapp.presentation.screen.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.usecase.ObserveAutoRefreshingSeedUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val observeAutoRefreshingSeedUseCase: ObserveAutoRefreshingSeedUseCase,
) : ViewModel() {

    data class GenerateUiState(
        val isLoading: Boolean = false,
        val qrSeed: QrSeed? = null,
        val error: AppException? = null,
        val isSeedExpired: Boolean = false,
    )

    private val _uiState = MutableStateFlow(GenerateUiState(isLoading = true))
    val uiState: StateFlow<GenerateUiState> = _uiState.asStateFlow()

    init {
        observeSeed()
    }

    private fun observeSeed() {
        observeAutoRefreshingSeedUseCase()
            .onEach { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is Result.Success -> {
                            currentState.copy(
                                isLoading = false,
                                qrSeed = result.data,
                                error = null
                            )
                        }

                        is Result.Error -> {
                            currentState.copy(
                                isLoading = false,
                                qrSeed = null,
                                error = result.exception
                            )
                        }
                    }
                }
            }
            .catch { e -> // Captura excepciones del Flow mismo
                _uiState.update { currentState ->
                    val exception = if (e is AppException) {
                        e
                    } else {
                        AppException.UnknownError
                    }
                    currentState.copy(
                        isLoading = false,
                        error = exception
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun errorShown() {
        _uiState.update { it.copy(error = null) }
    }
}