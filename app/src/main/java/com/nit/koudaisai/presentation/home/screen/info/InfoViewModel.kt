package com.nit.koudaisai.presentation.home.screen.info

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.nit.koudaisai.R
import com.nit.koudaisai.common.ext.isValidEmail
import com.nit.koudaisai.common.ext.isValidPhoneNumber
import com.nit.koudaisai.common.qr_code.QRSource
import com.nit.koudaisai.common.qr_code.generateQRCodeBitmap
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
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val accountService: AccountService,
    private val accountStorageService: AccountStorageService,
    private val reserveUseCase: ReserveUseCase,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {
    private val _state = MutableStateFlow(InfoState.empty())
    val state: StateFlow<InfoState> = _state

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDateTime.now()
            val startDate = LocalDateTime.of(2022, 10, 30, 0, 0, 0)
            _state.value = state.value.copy(reserveAvailable = currentDate.isAfter(startDate))
        } else {
            val currentDate = Calendar.getInstance()
            val startDate = Calendar.getInstance()
            startDate.set(2022, 9, 30, 0, 0, 0)
            val diff = currentDate.compareTo(startDate)
            _state.value = state.value.copy(reserveAvailable = diff > 0)
        }

        if (accountService.hasUser()) {
            onChangeIsUserLoading(true)
            _state.value = state.value.copy(user = KoudaisaiUser())
            //TODO ローディング状態の表示
            viewModelScope.launch(showErrorExceptionHandler) {
                if (getUser(accountService.getUserId()) { user ->
                        _state.value = state.value.copy(user = user)
                    } is Result.Error) {
                    //TODO PopBack
                    onChangeIsUserError(false)
                }
                state.value.user?.subUserIdList?.let { ids ->
                    val loadSub1 = launch {
                        ids.getOrNull(0)?.let { id ->
                            getUser(id) { subUser ->
                                _state.value = state.value.copy(subUser1 = subUser)
                                _state.value = state.value.copy(subUser1Id = id)
                            }
                        }
                    }
                    val loadSub2 = launch {
                        ids.getOrNull(1)?.let { id ->
                            getUser(id) { subUser ->
                                _state.value = state.value.copy(subUser2 = subUser)
                                _state.value = state.value.copy(subUser2Id = id)
                            }
                        }
                    }
                    loadSub1.join()
                    loadSub2.join()
                    addListener()
                    onSetPrevData()
                }
                onChangeIsUserLoading(false)

                val dayOneResult =
                    async { reserveUseCase.getReserveCapacity(KoudaisaiDay.DayOne) }
                val dayTwoResult =
                    async { reserveUseCase.getReserveCapacity(KoudaisaiDay.DayTwo) }
                val dayOne = dayOneResult.await()
                val dayTwo = dayTwoResult.await()
                if (dayOne is Result.Success) {
                    _state.value = state.value.copy(
                        dayOneFull = dayOne.data.maxCapacity <= dayOne.data.totalParticipants
                    )
                }
                if (dayTwo is Result.Success) {
                    _state.value = state.value.copy(
                        dayTwoFull = dayTwo.data.maxCapacity <= dayTwo.data.totalParticipants
                    )
                }
            }
        }
    }

    private fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountStorageService.addUserListener(accountService.getUserId(),
                onSuccess = { user ->
                    _state.value = state.value.copy(user = user)
                    onSetPrevData()
                },
                onError = {})
            state.value.subUser1Id?.let { sub1Id ->
                accountStorageService.addUserListener(sub1Id,
                    onSuccess = { user ->
                        _state.value = state.value.copy(subUser1 = user)
                        onSetPrevData()
                    }, onError = {})
            }
            state.value.subUser2Id?.let { sub2Id ->
                accountStorageService.addUserListener(sub2Id,
                    onSuccess = { user ->
                        _state.value = state.value.copy(subUser2 = user)
                        onSetPrevData()
                    }, onError = {})
            }
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { accountStorageService.removeListener() }
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
        _state.value = _state.value.copy(isUsersLoading = value)
    }

    private fun onChangeIsUserError(value: Boolean) {
        _state.value = _state.value.copy(isUsersError = value)
    }

    fun onChangeName(value: String) {
        val user = _state.value.user?.copy(name = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeEmail(value: String) {
        val user = _state.value.user?.copy(email = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangePhoneNumber(value: String) {
        val user = _state.value.user?.copy(phoneNumber = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeDayOneSelected(value: Boolean) {
        val user = _state.value.user?.copy(dayOneSelected = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeDayTwoSelected(value: Boolean) {
        val user = _state.value.user?.copy(dayTwoSelected = value)
        _state.value = _state.value.copy(user = user)
    }

    fun onChangeNameS1(value: String) {
        val user = _state.value.subUser1!!.copy(name = value)
        _state.value = _state.value.copy(subUser1 = user)
    }

    fun onChangeEmailS1(value: String) {
        val user = _state.value.subUser1!!.copy(email = value)
        _state.value = _state.value.copy(subUser1 = user)
    }

    fun onChangePhoneNumS1(value: String) {
        val user = _state.value.subUser1!!.copy(phoneNumber = value)
        _state.value = _state.value.copy(subUser1 = user)
    }

    fun onChangeDayOneSelectedS1(value: Boolean) {
        val user = _state.value.subUser1!!.copy(dayOneSelected = value)
        _state.value = _state.value.copy(subUser1 = user)
    }

    fun onChangeDayTwoSelectedS1(value: Boolean) {
        val user = _state.value.subUser1!!.copy(dayTwoSelected = value)
        _state.value = _state.value.copy(subUser1 = user)
    }

    fun onChangeNameS2(value: String) {
        val user = _state.value.subUser2!!.copy(name = value)
        _state.value = _state.value.copy(subUser2 = user)
    }

    fun onChangeEmailS2(value: String) {
        val user = _state.value.subUser2!!.copy(email = value)
        _state.value = _state.value.copy(subUser2 = user)
    }

    fun onChangePhoneNumS2(value: String) {
        val user = _state.value.subUser2!!.copy(phoneNumber = value)
        _state.value = _state.value.copy(subUser2 = user)
    }

    fun onChangeDayOneSelectedS2(value: Boolean) {
        val user = _state.value.subUser2!!.copy(dayOneSelected = value)
        _state.value = _state.value.copy(subUser2 = user)
    }

    fun onChangeDayTwoSelectedS2(value: Boolean) {
        val user = _state.value.subUser2!!.copy(dayTwoSelected = value)
        _state.value = _state.value.copy(subUser2 = user)
    }

    fun onUpdateMainUser() {
        onChangeReserve(uid = null, user = state.value.user!!, UserType.PARENT, {})
    }

    fun onUpdateSub1User() {
        state.value.subUser1?.let { subUser1 ->
            state.value.subUser1Id.let { id ->
                onChangeReserve(uid = id, user = subUser1, UserType.SUB1, {})
            }
        }
    }

    fun onUpdateSub2User() {
        state.value.subUser2?.let { subUser2 ->
            state.value.subUser2Id.let { id ->
                onChangeReserve(uid = id, user = subUser2, UserType.SUB2, {})
            }
        }
    }

    fun resetEditData() {
        _state.value = state.value.copy(
            user = state.value.prevUser,
            subUser1 = state.value.prevSubUser1,
            subUser2 = state.value.prevSubUser2
        )
        onChangeIsUserLoading(false)
    }

    enum class UserType(val index: Int) {
        PARENT(-1),
        SUB1(0),
        SUB2(1)
    }

    fun onSetPrevData() {
        _state.value = state.value.copy(
            prevUser = state.value.user,
            prevSubUser1 = state.value.subUser1,
            prevSubUser2 = state.value.subUser2
        )
    }


    private fun onChangeReserve(
        uid: String?,
        user: KoudaisaiUser,
        userType: UserType,
        popUpTo: () -> Unit
    ) {
        if (user.name.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_name_error)
            resetEditData()
            return
        }
        if (userType == UserType.PARENT) {
            if (!user.email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
                resetEditData()
                return
            }
            if (!user.phoneNumber.isValidPhoneNumber()) {
                SnackbarManager.showMessage(R.string.empty_phone_error)
                resetEditData()
                return
            }
        } else {
            if (user.email.isNotEmpty() && !user.email.isValidEmail()) {
                SnackbarManager.showMessage(R.string.email_error)
                resetEditData()
                return
            }
            if (user.phoneNumber.isNotEmpty() && !user.phoneNumber.isValidPhoneNumber()) {
                SnackbarManager.showMessage(R.string.empty_phone_error)
                resetEditData()
                return
            }
        }

        viewModelScope.launch {
            onChangeIsUserLoading(true)
            val prev = when (userType) {
                UserType.PARENT -> state.value.prevUser
                UserType.SUB1 -> state.value.prevSubUser1
                UserType.SUB2 -> state.value.prevSubUser2
            } ?: KoudaisaiUser()
            if (user == prev) {
                resetEditData()
                return@launch
            }
            val dayOneReserveAsync = dayOneReserveAsync(user, prev)
            val dayTwoReserveAsync = dayTwoReserveAsync(user, prev)
            val reserveDayOne = dayOneReserveAsync.await()
            val reserveDayTwo = dayTwoReserveAsync.await()
            if (reserveDayOne is Result.Error || reserveDayTwo is Result.Error) {
                resetEditData()
                return@launch
            }
            if (reserveDayOne is Result.Fail) {
                SnackbarManager.showMessage("11/19(土)は予約がいっぱいです．")
                resetEditData()
                return@launch
            }
            if (reserveDayTwo is Result.Fail) {
                SnackbarManager.showMessage("11/20(日)は予約がいっぱいです．")
                resetEditData()
                return@launch
            }
            val jobDayOneSetReserve = launchDayOneSetReserve(user, reserveDayOne)
            val jobDayTwoSetReserve = launchDayTwoSetReserve(user, reserveDayTwo)
            jobDayOneSetReserve.join()
            jobDayTwoSetReserve.join()
            if (reserveDayOne is Result.Success || reserveDayTwo is Result.Success ||
                user != prev
            ) {
                if (uid == null) {
                    reserveUseCase.sendUserData(user)
                } else {
                    reserveUseCase.sendUserData(uid, user)
                }
            }
            onSetPrevData()
            onChangeIsUserLoading(false)
        }
    }

    private fun CoroutineScope.dayOneReserveAsync(
        user: KoudaisaiUser,
        prevUser: KoudaisaiUser
    ): Deferred<Result<ReserveCapacity>> {
        return this.async {
            if (user.dayOneSelected != prevUser.dayOneSelected) {
                when (user.dayOneSelected) {
                    true -> reserveUseCase.isAvailableToReserve(KoudaisaiDay.DayOne)
                    false -> reserveUseCase.getReserveCapacity(KoudaisaiDay.DayOne)
                }
            } else {
                Result.Needless
            }
        }
    }

    private fun CoroutineScope.dayTwoReserveAsync(
        user: KoudaisaiUser,
        prevUser: KoudaisaiUser
    ): Deferred<Result<ReserveCapacity>> {
        return this.async {
            if (user.dayTwoSelected != prevUser.dayTwoSelected) {
                when (user.dayTwoSelected) {
                    true -> reserveUseCase.isAvailableToReserve(KoudaisaiDay.DayTwo)
                    false -> reserveUseCase.getReserveCapacity(KoudaisaiDay.DayTwo)
                }
            } else {
                Result.Needless
            }
        }
    }

    private fun CoroutineScope.launchDayOneSetReserve(
        user: KoudaisaiUser,
        reserveDayOne: Result<ReserveCapacity>
    ): Job {
        return this.launch {
            if (reserveDayOne is Result.Success) {
                if (user.dayOneSelected) {
                    reserveUseCase.incrementReserveCapacity(
                        KoudaisaiDay.DayOne,
                    )
                } else {
                    reserveUseCase.decrementReserveCapacity(
                        KoudaisaiDay.DayOne,
                    )
                }
            }
        }
    }

    private fun CoroutineScope.launchDayTwoSetReserve(
        user: KoudaisaiUser,
        reserveDayTwo: Result<ReserveCapacity>
    ): Job {
        return this.launch {
            if (reserveDayTwo is Result.Success) {
                if (user.dayTwoSelected) {
                    reserveUseCase.incrementReserveCapacity(
                        KoudaisaiDay.DayTwo,
                    )
                } else {
                    reserveUseCase.decrementReserveCapacity(
                        KoudaisaiDay.DayTwo,
                    )
                }
            }
        }
    }

    fun deleteSub1(popUpTo: () -> Unit) {
        state.value.subUser1?.let { user ->
            state.value.subUser1Id?.let { id ->
                onDeleteReserve(id, user, UserType.SUB1, popUpTo)
            }
        }
    }

    fun deleteSub2(popUpTo: () -> Unit) {
        state.value.subUser2?.let { user ->
            state.value.subUser2Id?.let { id ->
                onDeleteReserve(id, user, UserType.SUB2, popUpTo)
            }
        }
    }

    private fun onDeleteReserve(
        uid: String,
        user: KoudaisaiUser,
        userType: UserType,
        popUpTo: () -> Unit
    ) {
        viewModelScope.launch {
            onChangeIsUserLoading(true)
            val dayOneReserveAsync = async {
                if (user.dayOneSelected) {
                    reserveUseCase.getReserveCapacity(KoudaisaiDay.DayOne)
                } else {
                    Result.Needless
                }
            }
            val dayTwoReserveAsync = async {
                if (user.dayTwoSelected) {
                    reserveUseCase.getReserveCapacity(KoudaisaiDay.DayTwo)
                } else {
                    Result.Needless
                }
            }
            val reserveDayOne = dayOneReserveAsync.await()
            val reserveDayTwo = dayTwoReserveAsync.await()
            val jobDayOneSetReserve = launch {
                if (reserveDayOne is Result.Success) {
                    reserveUseCase.decrementReserveCapacity(
                        KoudaisaiDay.DayOne,
                    )
                }
            }
            val jobDayTwoSetReserve = launch {
                if (reserveDayTwo is Result.Success) {
                    reserveUseCase.decrementReserveCapacity(
                        KoudaisaiDay.DayTwo,
                    )
                }
            }
            val subIds = state.value.user!!.subUserIdList!!.toMutableList()
            subIds.removeAt(userType.index)
            reserveUseCase.updateUserData(
                uid = accountService.getUserId(),
                user = state.value.user!!.copy(subUserIdList = subIds)
            )
            jobDayOneSetReserve.join()
            jobDayTwoSetReserve.join()
            reserveUseCase.deleteUser(id = uid)
            onChangeIsUserLoading(false)
            SnackbarManager.showMessage("予約をキャンセルしました")
            popUpTo()
        }
    }

    fun displayQRCode(userType: UserType, pixSize: Int) {
        when (userType) {
            UserType.PARENT -> setDisplayUserName(state.value.user?.name)
            UserType.SUB1 -> setDisplayUserName(state.value.subUser1?.name)
            UserType.SUB2 -> setDisplayUserName(state.value.subUser2?.name)
        }
        setQRSource(userType, pixSize)
    }

    private fun setDisplayUserName(name: String?) {
        _state.value = state.value.copy(displayUserName = name)
    }

    fun closeQRCode() {
        _state.value = state.value.copy(isShowQR = false)
    }

    private fun setQRSource(userType: UserType, pixSize: Int) {
        val id = when (userType) {
            UserType.PARENT -> accountService.getUserId()
            UserType.SUB1 -> state.value.subUser1Id
            UserType.SUB2 -> state.value.subUser2Id
        }
        val qrSource = QRSource(
            data = "koudaisai/$id" ?: "",
            size = pixSize,
            errorCorrectionLevel = ErrorCorrectionLevel.M,
            charset = "UTF-8",
            margin = 4,
        )
        _state.value = state.value.copy(qrSource = qrSource)
        viewModelScope.launch(Dispatchers.Default) {
            qrSource.generateQRCodeBitmap(
                onSuccess = {
                    _state.value = state.value.copy(
                        qrBitmap = it,
                        isShowQR = true
                    )
                },
                onError = {
                    SnackbarManager.showMessage("QRコード生成にエラーが発生しました")
                }
            )
        }
    }

}