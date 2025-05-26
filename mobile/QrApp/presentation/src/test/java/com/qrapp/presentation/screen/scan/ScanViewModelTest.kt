package com.qrapp.presentation.screen.scan

import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.usecase.ValidateScannedCodeUseCase
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import com.qrapp.presentation.screen.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScanViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var validateScannedCodeUseCase: ValidateScannedCodeUseCase

    private lateinit var viewModel: ScanViewModel

    @Before
    fun setUp() {
        viewModel = ScanViewModel(validateScannedCodeUseCase, TestDispatcherProvider())
    }

    @Test
    fun `onQrCodeScanned sets scanResult on success`() = runTest {
        val result = Result.Success(QrScanResult(true, null))
        coEvery { validateScannedCodeUseCase("seed") } returns result
        viewModel.onQrCodeScanned("seed")
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(result.data, state.scanResult)
        assertNull(state.error)
    }

    @Test
    fun `onQrCodeScanned sets error on failure`() = runTest {
        val error = AppException.ApiError(400, "fail")
        coEvery { validateScannedCodeUseCase("seed") } returns Result.Error(error)
        viewModel.onQrCodeScanned("seed")
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.scanResult)
        assertEquals(error, state.error)
        assertTrue(state.shouldResetScanner)
    }

    @Test
    fun `resetScan clears scanResult and error and sets shouldResetScanner`() {
        viewModel.resetScan()
        val state = viewModel.uiState.value
        assertNull(state.scanResult)
        assertNull(state.error)
        assertFalse(state.isLoading)
        assertTrue(state.shouldResetScanner)
    }

    @Test
    fun `resetScanIfNeeded sets shouldResetScanner if not permission error`() {
        viewModel.resetScanIfNeeded()
        assertTrue(viewModel.uiState.value.shouldResetScanner)
    }

    @Test
    fun `onCameraPermissionResult updates permission state`() {
        viewModel.onCameraPermissionResult(granted = false, shouldShowRationale = false)
        val state = viewModel.uiState.value
        assertTrue(state.wasPermissionDenied)
        assertFalse(state.showPermissionDialog)
    }

    @Test
    fun `dismissCameraPermissionsDialog resets permission dialog state`() {
        viewModel.dismissCameraPermissionsDialog()
        val state = viewModel.uiState.value
        assertFalse(state.showPermissionDialog)
        assertFalse(state.wasPermissionDenied)
    }

    @Test
    fun `showCameraPermissionDialog sets showPermissionDialog true`() {
        viewModel.showCameraPermissionDialog()
        assertTrue(viewModel.uiState.value.showPermissionDialog)
    }
}
