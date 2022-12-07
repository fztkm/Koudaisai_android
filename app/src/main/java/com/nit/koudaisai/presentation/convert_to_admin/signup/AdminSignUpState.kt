package com.nit.koudaisai.presentation.convert_to_admin.signup

import com.nit.koudaisai.domain.model.KoudaisaiUser

data class AdminSignUpState(
    val user: KoudaisaiUser,
    val password: String,
    val isRegisterSending: Boolean,
    val isRegisterError: Boolean,
    val showDialog: Boolean,
) {
    companion object {
        fun empty(): AdminSignUpState {
            return AdminSignUpState(
                user = KoudaisaiUser(isAdmin = true),
                password = "",
                isRegisterSending = false,
                isRegisterError = false,
                showDialog = false,
            )
        }
    }
}