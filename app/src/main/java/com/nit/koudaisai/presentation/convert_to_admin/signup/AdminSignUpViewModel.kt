package com.nit.koudaisai.presentation.convert_to_admin.signup

import androidx.lifecycle.viewModelScope
import com.nit.koudaisai.R
import com.nit.koudaisai.common.ext.isValidEmail
import com.nit.koudaisai.common.ext.isValidPassword
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class AdminSignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val reserveUseCase: ReserveUseCase,
    private val accountStorageService: AccountStorageService,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {

    private val _state = MutableStateFlow(AdminSignUpState.empty())
    val state: StateFlow<AdminSignUpState> = _state


    private suspend fun getUser(uid: String, onSuccess: (KoudaisaiUser) -> Unit): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.getUser(uid,
                onError = { error ->
                    onChangeIsUserError(true)
                    onError(error)
                    it.resume(Result.Error(error))
                },
                onSuccess = { user ->
                    onChangeIsUserLoading(false)
                    onSuccess(user)
                    it.resume(Result.Success(true))
                }
            )
        }
    }

    private fun onChangeIsUserLoading(value: Boolean) {
        _state.value = _state.value.copy(isRegisterSending = value)
    }

    private fun onChangeIsUserError(value: Boolean) {
        _state.value = _state.value.copy(isRegisterError = value)
    }


    fun onChangeName(value: String) {
        val user = _state.value.user.copy(name = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onEmailChange(value: String) {
        val user = _state.value.user.copy(email = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onPassWordChange(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun startSending() {
        _state.value = _state.value.copy(isRegisterSending = true)
    }

    fun endSending() {
        _state.value = _state.value.copy(isRegisterSending = false)
    }

    fun onError() {
        _state.value = _state.value.copy(isRegisterError = true)
    }

    /**state.value.user.
     * Reserve
     */
    fun onReserveClick(onSuccess: () -> Unit, onFail: () -> Unit) {
        if (state.value.user.name.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_name_error)
            return
        }
        if (!state.value.user.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }
        if (!state.value.password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_error)
            return
        }
        reserve(onSuccess, onFail)
    }

    private fun reserve(onSuccess: () -> Unit, onFail: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            startSending()
            if (reserveUseCase
                    .createAccount(state.value.user.email, state.value.password) is Result.Error
            ) {
                endSending()
                onFail()
                return@launch
            }
            val createAccount = reserveUseCase.sendUserData(state.value.user)
            if (createAccount is Result.Success) {
                SnackbarManager.showMessage("受付用アカウントの作成が完了しました")
            }
            if (createAccount is Result.Error) {
                onError(createAccount.exception)
            }
            endSending()
            onSuccess()
        }
    }
}