package com.nit.koudaisai.presentation.home.screen.info

import android.graphics.Bitmap
import com.nit.koudaisai.common.qr_code.QRSource
import com.nit.koudaisai.domain.model.KoudaisaiUser

data class InfoState(
    val reserveAvailable: Boolean,
    val user: KoudaisaiUser?,
    val subUser1: KoudaisaiUser?,
    val subUser2: KoudaisaiUser?,
    val subUser1Id: String?,
    val subUser2Id: String?,
    val isUsersLoading: Boolean,
    val isUsersError: Boolean,
    val prevUser: KoudaisaiUser?,
    val prevSubUser1: KoudaisaiUser?,
    val prevSubUser2: KoudaisaiUser?,
    val showDialog: Boolean,
    val qrSource: QRSource?,
    val qrBitmap: Bitmap?,
    val isShowQR: Boolean,
    val displayUserName: String?,
    val dayOneFull: Boolean,
    val dayTwoFull: Boolean
) {
    companion object {
        fun empty(): InfoState {
            return InfoState(
                reserveAvailable = false,
                user = null,
                prevUser = null,
                prevSubUser1 = null,
                prevSubUser2 = null,
                subUser1 = null,
                subUser2 = null,
                isUsersLoading = false,
                isUsersError = false,
                subUser1Id = null,
                subUser2Id = null,
                showDialog = false,
                qrSource = null,
                qrBitmap = null,
                isShowQR = false,
                displayUserName = null,
                dayOneFull = false,
                dayTwoFull = false,
            )
        }
    }
}