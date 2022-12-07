package com.nit.koudaisai.presentation.convert_to_admin.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.nit.koudaisai.R
import com.nit.koudaisai.common.ext.isValidEmail
import com.nit.koudaisai.common.ext.toJpString
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSignInViewModel @Inject constructor(
    private val accountService: AccountService,
    private val useCase: ReserveUseCase,
    logService: LogService
) : KoudaisaiViewModel(logService) {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var showDialog by mutableStateOf(false)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPassWordChange(newPass: String) {
        password = newPass
    }

    fun onSignInClick(openAndPopUp: () -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }
        if (password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            isLoading = true
            accountService.authenticate(email, password) { error ->
                if (error != null) {
                    if (error is FirebaseAuthException) {
                        Log.i("AUTH", error.errorCode)
                        SnackbarManager.showMessage(error.toJpString())
                    } else {
                        onError(error)
                    }
                    isLoading = false
                } else {
                    viewModelScope.launch {
                        val userResult = useCase.getUserData(accountService.getUserId())
                        if (userResult is Result.Success) {
                            val user = userResult.data
                            val convertToAdmin = useCase.updateUserData(
                                accountService.getUserId(), user.copy(isAdmin = true)
                            )
                            if (convertToAdmin is Result.Error) {
                                onError(convertToAdmin.exception)
                            } else if (convertToAdmin is Result.Success) {
                                SnackbarManager.showMessage("入場処理権限を付与しました．")
                            }
                        }
                        openAndPopUp()
                        isLoading = false
                    }
                }
            }
        }
    }

    fun showSendRecoveryMailDialog() {
        showDialog = true
    }

    fun closeSendRecoveryMailDialog() {
        showDialog = false
    }

    fun onSendRecoveryMail() {
        closeSendRecoveryMailDialog()
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }
        viewModelScope.launch(showErrorExceptionHandler) {
            isLoading = true
            accountService.sendRecoveryEmail(email) { error ->
                if (error != null) {
                    onError(error)
                } else {
                    SnackbarManager.showMessage("メールを送信しました．メールボックスを確認してください．（迷惑メールとして扱われる場合があります）")
                }
                isLoading = false
            }
        }
    }
}