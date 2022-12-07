package com.nit.koudaisai.presentation.admn

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    //サポートする色情報の表現形式を選択．
    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
            val bytes = image.planes.first().buffer.toByteArray()

            //bytes(YUVデータ)を最適化されたものに変換する
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )

            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                BarcodeFormat.QR_CODE
                            )
                        )
                    )
                }.decode(binaryBmp)
                onQrCodeScanned(result.text)
            } catch (e: Exception) {
                //読み取った画像がQRコードでなかった場合等
                e.printStackTrace()
            } finally {
                image.close()
            }
        }
    }

    //ByteBufferのデータをByteArrayに変換する
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        //Bufferにあるbyte数;remaining() の長さのByte配列を生成.
        return ByteArray(remaining()).also {
            //BufferからByte配列にデータを転送
            get(it)
        }
    }

}