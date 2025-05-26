package com.qrapp.presentation.utils.bitmap

import android.graphics.Bitmap

interface QrCodeBitmapGenerator {
    fun generate(seed: String): Bitmap?
}
