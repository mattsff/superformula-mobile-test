package com.qrapp.data.repository

import com.qrapp.data.datasource.QrRemoteDataSource
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class QrRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var remoteDataSource: QrRemoteDataSource

    private lateinit var repository: QrRepositoryImpl

    @Before
    fun setUp() {
        repository = QrRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getSeed returns success when remoteDataSource returns seed`() = runTest {
        val seed = QrSeed("seed", Instant.now())
        coEvery { remoteDataSource.getSeed() } returns seed
        val result = repository.getSeed()
        assertEquals(Result.Success(seed), result)
    }

    @Test
    fun `getSeed returns error when remoteDataSource throws`() = runTest {
        coEvery { remoteDataSource.getSeed() } throws AppException.UnknownError
        val result = repository.getSeed()
        assert(result is Result.Error)
        assertEquals(AppException.UnknownError, (result as Result.Error).exception)
    }

    @Test
    fun `validateScannedCode returns success when remoteDataSource returns result`() = runTest {
        val scanResult = QrScanResult(true, null)
        coEvery { remoteDataSource.validateScannedCode("seed") } returns scanResult
        val result = repository.validateScannedCode("seed")
        assertEquals(Result.Success(scanResult), result)
    }

    @Test
    fun `validateScannedCode returns error when remoteDataSource throws`() = runTest {
        coEvery { remoteDataSource.validateScannedCode("seed") } throws AppException.UnknownError
        val result = repository.validateScannedCode("seed")
        assert(result is Result.Error)
        assertEquals(AppException.UnknownError, (result as Result.Error).exception)
    }
}
