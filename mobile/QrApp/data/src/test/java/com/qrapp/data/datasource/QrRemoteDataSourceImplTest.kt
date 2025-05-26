package com.qrapp.data.datasource

import com.qrapp.data.model.QrScanResultDto
import com.qrapp.data.model.QrSeedDto
import com.qrapp.data.remote.QrApiService
import com.qrapp.domain.model.QrScanResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QrRemoteDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var api: QrApiService

    private lateinit var dataSource: QrRemoteDataSourceImpl

    @Before
    fun setUp() {
        dataSource = QrRemoteDataSourceImpl(api)
    }

    @Test
    fun `getSeed returns mapped QrSeed`() = runTest {
        val dto = QrSeedDto("seed", 123456789L)
        coEvery { api.getSeed() } returns dto
        val result = dataSource.getSeed()
        assertEquals("seed", result.seed)
        assertEquals(123456789L, result.expiresAt?.toEpochMilli())
    }

    @Test
    fun `validateScannedCode returns mapped QrScanResult`() = runTest {
        val dto = QrScanResultDto(true, "ok")
        coEvery { api.validateSeed("seed") } returns dto
        val result = dataSource.validateScannedCode("seed")
        assertEquals(QrScanResult(true, "ok"), result)
    }
}
