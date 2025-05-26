package com.qrapp.presentation.screen.generate

import android.graphics.Bitmap
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.usecase.ObserveAutoRefreshingSeedUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import com.qrapp.presentation.screen.QrCodeBitmapGeneratorTest
import com.qrapp.presentation.screen.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class QrViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var observeAutoRefreshingSeedUseCase: ObserveAutoRefreshingSeedUseCase

    @MockK
    lateinit var bitmap: Bitmap

    private lateinit var viewModel: QrViewModel

    @Before
    fun setUp() {
    }

    @Test
    fun `emits success state when use case returns success`() = runTest {
        val seed = QrSeed("seed", Instant.now().plusSeconds(60))
        coEvery { observeAutoRefreshingSeedUseCase.invoke() } returns flow {
            emit(Result.Success(seed))
        }
        viewModel = QrViewModel(
            observeAutoRefreshingSeedUseCase,
            TestDispatcherProvider(),
            QrCodeBitmapGeneratorTest(bitmap)
        )
        val state = viewModel.uiState.first { it.qrSeed != null }
        assertFalse(state.isLoading)
        assertEquals(seed, state.qrSeed)
        assertEquals(bitmap, state.qrBitmap)
        assertNull(state.error)
    }

    @Test
    fun `emits error state when use case returns error`() = runTest {
        val error = AppException.NetworkError
        coEvery { observeAutoRefreshingSeedUseCase.invoke() } returns flow {
            emit(Result.Error(error))
        }
        viewModel = QrViewModel(
            observeAutoRefreshingSeedUseCase,
            TestDispatcherProvider(),
            QrCodeBitmapGeneratorTest(bitmap)
        )
        val state = viewModel.uiState.first { it.error != null }
        assertNull(state.qrSeed)
        assertNull(state.qrBitmap)
        assert(state.error == AppException.NetworkError)
        assertEquals("", state.timeLeft)
    }

    @Test
    fun `errorShown clears error`() = runTest {
        val error = AppException.NetworkError
        coEvery { observeAutoRefreshingSeedUseCase.invoke() } returns flow {
            emit(Result.Error(error))
        }
        viewModel = QrViewModel(
            observeAutoRefreshingSeedUseCase,
            TestDispatcherProvider(),
            QrCodeBitmapGeneratorTest(bitmap)
        )
        viewModel.errorShown()
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `observeSeed handles unknown exception in catch`() = runTest {
        coEvery { observeAutoRefreshingSeedUseCase.invoke() } returns flow {
            throw IllegalStateException("fail")
        }
        viewModel = QrViewModel(
            observeAutoRefreshingSeedUseCase,
            TestDispatcherProvider(),
            QrCodeBitmapGeneratorTest(bitmap)
        )
        val state = viewModel.uiState.first { it.error != null }
        assertEquals(AppException.UnknownError, state.error)
        assertEquals("", state.timeLeft)
    }
}
