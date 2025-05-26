package com.qrapp.data.model.mappers

import com.qrapp.domain.util.AppException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ExceptionsMapperTest {

    @Test
    fun `HttpException maps to ApiError with code and message`() {
        val errorJson = """{"message":"Invalid seed"}"""
        val response = Response.error<String>(
            400,
            ResponseBody.create("application/json".toMediaTypeOrNull(), errorJson)
        )
        val exception = HttpException(response)
        val mapped = exception.toRemoteException()
        assert(mapped is AppException.ApiError)
        val apiError = mapped as AppException.ApiError
        assertEquals(400, apiError.code)
    }

    @Test
    fun `IOException maps to NetworkError`() {
        val exception = IOException("Network fail")
        val mapped = exception.toRemoteException()
        assertEquals(AppException.NetworkError, mapped)
    }

    @Test
    fun `Unknown exception maps to UnknownError`() {
        val exception = IllegalStateException("fail")
        val mapped = exception.toRemoteException()
        assertEquals(AppException.UnknownError, mapped)
    }

    @Test
    fun `HttpException with no message returns null message`() {
        val response = Response.error<String>(
            400,
            ResponseBody.create("application/json".toMediaTypeOrNull(), "{}")
        )
        val exception = HttpException(response)
        val mapped = exception.toRemoteException()
        val apiError = mapped as AppException.ApiError
        assertEquals(null, apiError.message)
    }
}
