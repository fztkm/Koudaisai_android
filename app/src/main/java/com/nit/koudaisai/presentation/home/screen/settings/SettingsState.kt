package com.nit.koudaisai.presentation.home.screen.settings

import com.nit.koudaisai.domain.model.KoudaisaiUser

data class SettingsState(
    val showLogoutDialog: Boolean = false,
    val showCleanUpUserDataDialog: Boolean = false,
    val showResetPasswordDialog: Boolean = false,
    val hasUser: Boolean = false,
    val email: String = "",
    val password: String = "",
    val parentUser: KoudaisaiUser = KoudaisaiUser(),
    val isLoading: Boolean = false,
    val isSuperUser: Boolean = false
)