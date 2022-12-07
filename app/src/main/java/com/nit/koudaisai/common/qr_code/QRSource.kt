package com.nit.koudaisai.common.qr_code

import android.graphics.Color
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

data class QRSource(
    /** QR化するデータ */
    val data: String,

    /**
     * QRコードサイズ(px)
     */
    val size: Int,

    /** 誤り訂正レベル */
    val errorCorrectionLevel: ErrorCorrectionLevel,

    /** 文字コード */
    val charset: String,

    /**
     * マージン(セル数, 4セル以上の余白が必要)
     */
    val margin: Int = 4,

    /** 前景色 */
    val foregroundColor: Int = Color.BLACK,

    /** 背景色 */
    val backgroundColor: Int = Color.WHITE
) {
}