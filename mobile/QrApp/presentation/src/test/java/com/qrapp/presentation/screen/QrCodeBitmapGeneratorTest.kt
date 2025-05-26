package com.qrapp.presentation.screen

import android.graphics.Bitmap
import com.qrapp.presentation.utils.bitmap.QrCodeBitmapGenerator

class QrCodeBitmapGeneratorTest(private val bitmap: Bitmap?) : QrCodeBitmapGenerator {
    override fun generate(seed: String): Bitmap? = bitmap
}