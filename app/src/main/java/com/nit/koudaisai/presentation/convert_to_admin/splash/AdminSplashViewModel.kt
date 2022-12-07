package com.nit.koudaisai.presentation.convert_to_admin.splash

import androidx.lifecycle.viewModelScope
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSplashViewModel @Inject constructor(
    private val accountService: AccountService,
    private val useCase: ReserveUseCase,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {

    private var _isCompletedMakeCurrentUserAdmin = MutableStateFlow(false)
    val isCompletedMakeCurrentUserAdmin: StateFlow<Boolean> = _isCompletedMakeCurrentUserAdmin
    private var _haveUser = MutableStateFlow(false)
    val haveUser: StateFlow<Boolean> = _haveUser

    init {
        if (accountService.hasUser()) {
            _haveUser.value = true
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
            }
        } else {
            _haveUser.value = false
        }
        _isCompletedMakeCurrentUserAdmin.value = true
    }

    fun onAppStart(popUpToHome: () -> Unit) {
        popUpToHome()
    }
}