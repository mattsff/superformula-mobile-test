package com.qrapp.data.di

import com.qrapp.data.datasource.QrRemoteDataSource
import com.qrapp.data.datasource.QrRemoteDataSourceImpl
import com.qrapp.data.remote.QrApiService
import com.qrapp.data.repository.QrRepositoryImpl
import com.qrapp.domain.repository.QrRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa el OkHttpClient con el logger
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideQrApiService(retrofit: Retrofit): QrApiService =
        retrofit.create(QrApiService::class.java)

    @Provides
    fun provideRemoteDataSource(api: QrApiService): QrRemoteDataSource = QrRemoteDataSourceImpl(api)

    @Provides
    fun provideQrRepository(remote: QrRemoteDataSource): QrRepository = QrRepositoryImpl(remote)
}