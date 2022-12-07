package com.nit.koudaisai.common.qr_code

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder

fun QRSource.generateQRCodeBitmap(onSuccess: (Bitmap) -> Unit, onError: () -> Unit) =
    try {
        val qrSource = this
        // 生成に関するパラメータ
        val hints = mapOf(
            // マージン
            EncodeHintType.MARGIN to qrSource.margin,
            // 誤り訂正レベル
            EncodeHintType.ERROR_CORRECTION to qrSource.errorCorrectionLevel,
            // 文字コード
            EncodeHintType.CHARACTER_SET to qrSource.charset
        )

        val pxSize = qrSource.size
        val bitmap = BarcodeEncoder().encodeBitmap(
            qrSource.data,
            BarcodeFormat.QR_CODE,
            pxSize, pxSize,
            hints
        ).also { encoder ->
            // 黒白以外にしたいならここで適当にQRコードを色付けする
            val pixels = IntArray(pxSize * pxSize)
            encoder.getPixels(pixels, 0, pxSize, 0, 0, pxSize, pxSize)
            for (idx in pixels.indices) {
                pixels[idx] =
                    if (pixels[idx] == Color.BLACK) qrSource.foregroundColor
                    else qrSource.backgroundColor
            }
            encoder.setPixels(pixels, 0, pxSize, 0, 0, pxSize, pxSize)
        }
        onSuccess(bitmap)
    } catch (e: Throwable) {
        Log.e("genQRCode", Log.getStackTraceString(e))
        onError()
        null
    }