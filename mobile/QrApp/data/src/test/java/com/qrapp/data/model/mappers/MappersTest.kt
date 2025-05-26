package com.qrapp.data.model.mappers

import com.qrapp.data.model.QrScanResultDto
import com.qrapp.data.model.QrSeedDto
import com.qrapp.domain.model.QrScanResult
import com.qrapp.domain.model.QrSeed
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class MappersTest {

    @Test
    fun `QrSeedDto toDomain maps fields correctly`() {
        val now = System.currentTimeMillis()
        val dto = QrSeedDto(seed = "abc", expires_at = now)
        val domain = dto.toDomain()
        assertEquals("abc", domain.seed)
        assertEquals(Instant.ofEpochMilli(now), domain.expiresAt)
    }

    @Test
    fun `QrScanResultDto toDomain maps fields correctly`() {
        val dto = QrScanResultDto(valid = true, reason = "ok")
        val domain = dto.toDomain()
        assertEquals(QrScanResult(isValid = true, reason = "ok"), domain)
    }
}
