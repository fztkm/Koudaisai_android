package com.nit.koudaisai.presentation.reserve.resereve_screen

import com.nit.koudaisai.domain.model.KoudaisaiUser

data class ReserveState(
    val user: KoudaisaiUser,
    val password: String,
    val agreement: Boolean,
    val isRegisterSending: Boolean,
    val isRegisterError: Boolean,
    val showDialog: Boolean,
    val isButtonEnabled: Boolean,
    val isDayOneFull: Boolean,
    val isDayTwoFull: Boolean
) {
    companion object {
        fun empty(): ReserveState {
            return ReserveState(
                user = KoudaisaiUser(),
                password = "",
                agreement = false,
                isRegisterSending = false,
                isRegisterError = false,
                showDialog = false,
                isButtonEnabled = false,
                isDayOneFull = false,
                isDayTwoFull = false,
            )
        }
    }
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    object Fail : Result<Nothing>()
    object Needless : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}