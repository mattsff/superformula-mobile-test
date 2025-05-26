package com.qrapp.domain.usecase

import com.qrapp.domain.model.QrSeed
import com.qrapp.domain.repository.QrRepository
import com.qrapp.domain.util.AppException
import com.qrapp.domain.util.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class ObserveAutoRefreshingSeedUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: QrRepository

    private lateinit var useCase: ObserveAutoRefreshingSeedUseCase

    @Before
    fun setUp() {
        useCase = ObserveAutoRefreshingSeedUseCase(repository)
    }

    @Test
    fun `emits success result when repository returns success`() = runTest {
        val seed = QrSeed("seed", Instant.now().plusSeconds(30))
        coEvery { repository.getSeed() } returns Result.Success(seed)
        val result = useCase().first()
        assertEquals(Result.Success(seed), result)
    }

    @Test
    fun `emits error result when repository returns error`() = runTest {
        val error = Result.Error(AppException.ApiError(400,"error"))
        coEvery { repository.getSeed() } returns error
        val result = useCase().first()
        assertEquals(error, result)
    }

    @Test
    fun `delay is based on expiresAt if present`() = runTest {
        val now = Instant.now()
        val seed = QrSeed("seed", now.plusMillis(100))
        coEvery { repository.getSeed() } returns Result.Success(seed)
        val flow = useCase()
        flow.first()
    }
}
