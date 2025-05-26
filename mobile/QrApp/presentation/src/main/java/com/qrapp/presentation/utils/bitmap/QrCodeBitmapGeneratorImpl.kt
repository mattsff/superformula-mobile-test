package com.qrapp.presentation.utils.bitmap

import android.graphics.Bitmap

class QrCodeBitmapGeneratorImpl : QrCodeBitmapGenerator {
    override fun generate(seed: String): Bitmap? = generateQrCodeBitmap(seed)
}