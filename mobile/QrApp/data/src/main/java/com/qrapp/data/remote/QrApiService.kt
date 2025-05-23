package com.qrapp.data.remote
import com.qrapp.data.model.QrScanResultDto
import com.qrapp.data.model.QrSeedDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QrApiService {

    @GET("/seed")
    suspend fun getSeed(): QrSeedDto

    @GET("/seed/validate")
    suspend fun validateSeed(@Query("seed") seed: String): QrScanResultDto
}