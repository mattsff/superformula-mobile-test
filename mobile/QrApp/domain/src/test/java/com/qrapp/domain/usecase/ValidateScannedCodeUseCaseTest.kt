package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateScannedCodeUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: QrRepository

    private lateinit var useCase: ValidateScannedCodeUseCase

    @Before
    fun setUp() {
        useCase = ValidateScannedCodeUseCase(repository)
    }

    @Test
    fun `returns success when repository returns success`() = runTest {
        val result = Result.Success(QrScanResult(true, null))
        coEvery { repository.validateScannedCode("seed") } returns result
        val response = useCase("seed")
        assertEquals(result, response)
    }

    @Test
    fun `returns error when repository returns error`() = runTest {
        val error = Result.Error(AppException.ApiError(400, "error"))
        coEvery { repository.validateScannedCode("seed") } returns error
        val response = useCase("seed")
        assertEquals(error, response)
    }
}
