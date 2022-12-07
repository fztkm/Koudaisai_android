package com.nit.koudaisai.presentation.reserve.resereve_screen.reserve_companion

import androidx.lifecycle.viewModelScope
import com.nit.koudaisai.R
import com.nit.koudaisai.common.ext.isValidEmail
import com.nit.koudaisai.common.ext.isValidPhoneNumber
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.model.ReserveCapacity
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import com.nit.koudaisai.utils.KoudaisaiDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ReserveCompanionViewModel @Inject constructor(
    private val accountService: AccountService,
    private val reserveUseCase: ReserveUseCase,
    private val accountStorageService: AccountStorageService,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {

    private val _state = MutableStateFlow(ReserveCompanionState.empty())
    val state: StateFlow<ReserveCompanionState> = _state

    init {
        onChangeIsUserLoading(true)
        viewModelScope.launch(showErrorExceptionHandler) {
            val parentId = accountService.getUserId()
            onChangeParentId(parentId)
            if (getUser(parentId) { user ->
                    _state.value = state.value.copy(parent = user)
                } is Result.Error) {
                //TODO PopBack
            }

            val dayOneResult = async { reserveUseCase.getReserveCapacity(KoudaisaiDay.DayOne) }
            val dayTwoResult = async { reserveUseCase.getReserveCapacity(KoudaisaiDay.DayTwo) }
            val dayOne = dayOneResult.await()
            val dayTwo = dayTwoResult.await()
            if (dayOne is Result.Success) {
                _state.value = state.value.copy(
                    isDayOneFull = dayOne.data.maxCapacity <= dayOne.data.totalParticipants
                )
            }
            if (dayTwo is Result.Success) {
                _state.value = state.value.copy(
                    isDayTwoFull = dayTwo.data.maxCapacity <= dayTwo.data.totalParticipants
                )
            }
        }

    }

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

    fun onChangePhoneNumber(value: String) {
        val user = _state.value.user.copy(phoneNumber = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeDayOne(value: Boolean) {
        val user = _state.value.user.copy(dayOneSelected = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeDayTwo(value: Boolean) {
        val user = _state.value.user.copy(dayTwoSelected = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onEmailChange(value: String) {
        val user = _state.value.user.copy(email = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeParentId(value: String) {
        val user = _state.value.user.copy(parentId = value)
        _state.value = _state.value.copy(user = user)
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

    /**
     * Validate Input Data
     */
    fun onNextClick(openAndPopup: () -> Unit) {
        if (state.value.user.email.isNotEmpty() && !state.value.user.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }
        if (state.value.user.name.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_name_error)
            return
        }
        if (!state.value.user.phoneNumber.isValidPhoneNumber()) {
            SnackbarManager.showMessage(R.string.empty_phone_error)
            return
        }
        if (!state.value.user.dayOneSelected && !state.value.user.dayTwoSelected) {
            SnackbarManager.showMessage(R.string.empty_select_day_error)
            return
        }
        openAndPopup()
    }

    fun signOut() {
        accountService.signOut()
    }

    /**state.value.user.
     * Reserve
     */
    fun onReserveClick(onSuccess: () -> Unit, onFail: () -> Unit) {
        if (state.value.user.name.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_name_error)
            return
        }
        if (state.value.user.email.isNotEmpty() && !state.value.user.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }
        if (state.value.user.phoneNumber.isNotEmpty() && !state.value.user.phoneNumber.isValidPhoneNumber()) {
            SnackbarManager.showMessage(R.string.empty_phone_error)
            return
        }
        if (!state.value.user.dayOneSelected && !state.value.user.dayTwoSelected) {
            SnackbarManager.showMessage(R.string.empty_select_day_error)
            return
        }
        reserve(onSuccess, onFail)
    }

    private fun reserve(onSuccess: () -> Unit, onFail: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            startSending()
            val dayOneReserveAsync = dayOneReserveAsync()
            val dayTwoReserveAsync = dayTwoReserveAsync()
            val dayOneReserve = dayOneReserveAsync.await()
            val dayTwoReserve = dayTwoReserveAsync.await()
            if (dayOneReserve is Result.Error || dayTwoReserve is Result.Error) {
                endSending()
                onFail()
                return@launch
            }
            if (dayOneReserve is Result.Fail) {
                SnackbarManager.showMessage("11/19(土)は予約がいっぱいです．")
                endSending()
                onFail()
                return@launch
            }
            if (dayTwoReserve is Result.Fail) {
                SnackbarManager.showMessage("11/20(日)は予約がいっぱいです．")
                endSending()
                onFail()
                return@launch
            }
            val jobSetDayOne = launchAddDayOne(dayOneReserve)
            val jobSetDayTwo = launchAddDayTwo(dayTwoReserve)
            jobSetDayOne.join()
            jobSetDayTwo.join()
            val subId = reserveUseCase.sendSubUserData(state.value.user)
            if (subId is Result.Success) {
                var subIds = state.value.parent.subUserIdList
                subIds = subIds?.plus(subId.data) ?: listOf(subId.data)
                reserveUseCase.updateUserData(
                    uid = state.value.user.parentId!!,
                    user = state.value.parent.copy(subUserIdList = subIds)
                )
            }
            endSending()
            SnackbarManager.showMessage("予約が完了しました．")
            onSuccess()
        }
    }

    private fun CoroutineScope.dayOneReserveAsync(): Deferred<Result<ReserveCapacity>> {
        return this.async {
            if (state.value.user.dayOneSelected)
                reserveUseCase.isAvailableToReserve(KoudaisaiDay.DayOne)
            else
                Result.Needless
        }
    }

    private fun CoroutineScope.dayTwoReserveAsync(): Deferred<Result<ReserveCapacity>> {
        return this.async {
            if (state.value.user.dayTwoSelected)
                reserveUseCase.isAvailableToReserve(KoudaisaiDay.DayTwo)
            else
                Result.Needless
        }
    }

    private fun CoroutineScope.launchAddDayOne(dayOneReserve: Result<ReserveCapacity>): Job {
        return this.launch {
            if (dayOneReserve is Result.Success) {
                reserveUseCase.incrementReserveCapacity(
                    KoudaisaiDay.DayOne,
                )
            }
        }
    }

    private fun CoroutineScope.launchAddDayTwo(dayTwoReserve: Result<ReserveCapacity>): Job {
        return this.launch {
            if (dayTwoReserve is Result.Success) {
                reserveUseCase.incrementReserveCapacity(
                    KoudaisaiDay.DayTwo,
                )
            }
        }
    }
}