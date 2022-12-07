package com.nit.koudaisai.presentation.reserve.resereve_screen.reserve_companion

import com.nit.koudaisai.domain.model.KoudaisaiUser

data class ReserveCompanionState(
    val parentId: String,
    val parent: KoudaisaiUser,
    val user: KoudaisaiUser,
    val agreement: Boolean,
    val isRegisterSending: Boolean,
    val isRegisterError: Boolean,
    val showDialog: Boolean,
    val isDayOneFull: Boolean,
    val isDayTwoFull: Boolean
) {
    companion object {
        fun empty(): ReserveCompanionState {
            return ReserveCompanionState(
                parentId = "",
                parent = KoudaisaiUser(),
                user = KoudaisaiUser(),
                agreement = false,
                isRegisterSending = false,
                isRegisterError = false,
                showDialog = false,
                isDayOneFull = false,
                isDayTwoFull = false,
            )
        }
    }
}