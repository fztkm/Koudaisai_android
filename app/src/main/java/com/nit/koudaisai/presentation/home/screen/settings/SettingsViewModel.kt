package com.nit.koudaisai.presentation.home.screen.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.nit.koudaisai.R
import com.nit.koudaisai.common.KoudaisaiLink
import com.nit.koudaisai.common.ext.isValidEmail
import com.nit.koudaisai.common.ext.toJpString
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import com.nit.koudaisai.utils.KoudaisaiDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountStorageService: AccountStorageService,
    private val accountService: AccountService,
    private val useCase: ReserveUseCase,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state

    init {
        _state.value = state.value.copy(hasUser = accountService.hasUser())
        if (state.value.hasUser) {
            _state.value = state.value.copy(email = accountService.getUserEmail())
            _state.value =
                state.value.copy(isSuperUser = accountService.getUserEmail() == KoudaisaiLink.superUserEmail)
        }
    }

    fun showLogoutDialog() {
        _state.value = state.value.copy(showLogoutDialog = true)
    }

    fun closeLogoutDialog() {
        _state.value = state.value.copy(showLogoutDialog = false)
    }

    fun showDeleteDataDialog() {
        _state.value = state.value.copy(showCleanUpUserDataDialog = true)
    }

    fun closeDeleteDataDialog() {
        _state.value = state.value.copy(showCleanUpUserDataDialog = false)
        resetPassword()
    }

    fun showResetPasswordDialog() {
        _state.value = state.value.copy(showResetPasswordDialog = true)
    }

    fun closeResetPasswordDialog() {
        _state.value = state.value.copy(showResetPasswordDialog = false)
    }

    fun setEmail(value: String) {
        _state.value = state.value.copy(email = value)
    }

    fun setPassword(value: String) {
        _state.value = state.value.copy(password = value)
    }

    private fun resetPassword() {
        _state.value = state.value.copy(password = "")
    }

    fun onLoading() {
        _state.value = state.value.copy(isLoading = true)
    }

    fun finishLoading() {
        _state.value = state.value.copy(isLoading = false)
    }

    fun sendPasswordResetMail() {
        closeResetPasswordDialog()
        val email = accountService.getUserEmail()
        viewModelScope.launch(showErrorExceptionHandler) {
            accountService.sendRecoveryEmail(email) { error ->
                if (error != null) {
                    onError(error)
                } else {
                    SnackbarManager.showMessage("$email 宛に送信しました．（迷惑メールとして扱われる場合があります）")
                }
            }
        }
    }

    fun onLogout(popUpToSplash: () -> Unit) {
        accountService.signOut()
        popUpToSplash()
        closeLogoutDialog()
    }

    fun onClickDelete(popUpToSplash: () -> Unit) {
        if (!state.value.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            closeDeleteDataDialog()
            return
        }
        if (state.value.password.isEmpty()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            closeDeleteDataDialog()
            return
        }
        onLoading()
        viewModelScope.launch {
            accountService.reAuthenticate(
                state.value.email,
                state.value.password
            ) { error ->
                if (error == null) {
                    cleanUpUserData(popUpToSplash)
                } else {
                    if (error is FirebaseAuthException) {
                        Log.i("AUTH", error.errorCode)
                        SnackbarManager.showMessage(error.toJpString())
                    } else {
                        onError(error)
                    }
                    closeDeleteDataDialog()
                    finishLoading()
                }
            }
        }
    }

    private fun cleanUpUserData(popUpToSplash: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            val userId = accountService.getUserId()
            val parentUserResult = useCase.getUserData(userId)
            if (parentUserResult is Result.Error) {
                onError(parentUserResult.exception)
                closeDeleteDataDialog()
                finishLoading()
                return@launch
            }
            if (parentUserResult is Result.Success) {
                _state.value = state.value.copy(parentUser = parentUserResult.data)
            }
            state.value.parentUser.subUserIdList?.let { ids ->
                ids.getOrNull(0)?.let {
                    decrementCapacity(it)
                    useCase.deleteUser(it)
                }
                ids.getOrNull(1)?.let {
                    decrementCapacity(it)
                    useCase.deleteUser(it)
                }
            }
            decrementCapacity(userId)
            useCase.deleteUser(userId)
            accountService.deleteAccount { }
            finishLoading()
            popUpToSplash()
            closeDeleteDataDialog()
        }
    }

    private suspend fun decrementCapacity(uid: String) {
        val user = useCase.getUserData(uid)
        if (user is Result.Success) {
            if (user.data.dayOneSelected)
                useCase.decrementReserveCapacity(KoudaisaiDay.DayOne)
            if (user.data.dayTwoSelected)
                useCase.decrementReserveCapacity(KoudaisaiDay.DayTwo)
        }
    }

    fun openKoudaisaiHomePage(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.homepage)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }

    fun openQuestionPage(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.questions)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }

    fun openInquiry(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.inquiry)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }


    fun openKoudaisaiTwitter(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.twitter)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }

    fun openKoudaisaiInstagram(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.instagram)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }

    fun openPrivacyPolicy(context: Context) {
        val uri = Uri.parse(KoudaisaiLink.privacyPolicy)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        ContextCompat.startActivity(context, intent, null)
    }
}