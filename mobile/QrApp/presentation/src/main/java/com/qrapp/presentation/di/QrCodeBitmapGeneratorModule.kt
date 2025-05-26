package com.qrapp.presentation.di

import com.qrapp.presentation.utils.bitmap.QrCodeBitmapGenerator
import com.qrapp.presentation.utils.bitmap.QrCodeBitmapGeneratorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QrCodeBitmapGeneratorModule {
    @Provides
    @Singleton
    fun provideQrCodeBitmapGenerator(): QrCodeBitmapGenerator = QrCodeBitmapGeneratorImpl()
}