package com.nit.koudaisai.presentation.admn

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.ReserveUseCase
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import com.nit.koudaisai.utils.KoudaisaiDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AdmissionViewModel @Inject constructor(
    private val accountService: AccountService,
    private val accountStorageService: AccountStorageService,
    private val reserveUseCase: ReserveUseCase,
    private val logService: LogService
) : KoudaisaiViewModel(logService) {
    private val _state = MutableStateFlow(AdmissionState.empty())
    val state: StateFlow<AdmissionState> = _state

    fun startLoading() {
        _state.value = state.value.copy(isLoading = true)
    }

    fun finishLoading() {
        _state.value = state.value.copy(isLoading = false)
    }

    fun setVisitorId(id: String) {
        _state.value = state.value.copy(visitorId = id)
    }

    fun setUser(user: KoudaisaiUser) {
        _state.value = state.value.copy(user = user)
    }

    fun setIsNowEntry() {
        _state.value = state.value.copy(isNowEntry = true)
    }

    fun setAlreadyEntry() {
        _state.value = state.value.copy(isNowEntry = false)
    }

    fun resetState() {
        _state.value = AdmissionState.empty()
    }

    fun setNotReserved() {
        _state.value = state.value.copy(isNotReserve = true)
    }

    fun onScanId(visitorId: String, popUpTo: () -> Unit, popBack: () -> Unit) {
        if (state.value.isLoading) return
        resetState()
        startLoading()
        setVisitorId(visitorId)
        viewModelScope.launch {
            delay(1000L)
            val userJob = reserveUseCase.getUserData(visitorId)
            if (userJob is Result.Error) {
                onError(userJob.exception)
                finishLoading()
                return@launch
            }
            if (userJob is Result.Success) {
                val user = userJob.data
                setUser(user = user)
                when (today()) {
                    KoudaisaiDay.DayOne -> {
                        if (user.dayOneSelected) {
                            if (user.dayOneVisited) {
                                setAlreadyEntry()
                            } else {
                                val timestamps =
                                    (user.timestamps?.toMutableList() ?: mutableListOf()).apply {
                                        add(Timestamp.now())
                                    }
                                val enterJob = reserveUseCase.updateUserData(
                                    visitorId,
                                    user.copy(
                                        dayOneVisited = true,
                                        timestamps = timestamps
                                    )
                                )
                                if (enterJob is Result.Error) {
                                    onError(enterJob.exception)
                                    popBack()
                                    return@launch
                                }
                                if (enterJob is Result.Success) {
                                    setUser(user.copy(timestamps = timestamps))
                                    setIsNowEntry()
                                }
                            }
                        } else {
                            setNotReserved()
                            SnackbarManager.showMessage("1日目の予約が行われていません.")
                        }
                        popUpTo()
                        finishLoading()
                    }
                    KoudaisaiDay.DayTwo -> {
                        if (user.dayTwoSelected) {
                            if (user.dayTwoVisited) {
                                setAlreadyEntry()
                            } else {
                                val timestamps =
                                    (user.timestamps?.toMutableList() ?: mutableListOf()).apply {
                                        add(Timestamp.now())
                                    }
                                val enterJob = reserveUseCase.updateUserData(
                                    visitorId,
                                    user.copy(
                                        dayTwoVisited = true,
                                        timestamps = timestamps
                                    )
                                )
                                if (enterJob is Result.Error) {
                                    onError(enterJob.exception)
                                    popBack()
                                    return@launch
                                }
                                if (enterJob is Result.Success) {
                                    setUser(user.copy(timestamps = timestamps))
                                    setIsNowEntry()
                                }
                            }
                        } else {
                            setNotReserved()
                            SnackbarManager.showMessage("2日目の予約が行われていません.")
                        }
                        popUpTo()
                        finishLoading()
                    }
                    null -> {
                        SnackbarManager.showMessage("工大祭の日程外です．")
                        popBack()
                        finishLoading()
                        return@launch
                    }
                }
                finishLoading()
            }
        }
    }

    fun today(): KoudaisaiDay? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDateTime.now()
            val startDate = LocalDateTime.of(2022, 11, 19, 0, 0, 0)
            val dayTwoStart = LocalDateTime.of(2022, 11, 20, 0, 0, 0)
            val dayTwoEnd = LocalDateTime.of(2022, 11, 21, 0, 0, 0)
            if (currentDate.isBefore(startDate)) {
                return null
            } else if (currentDate.isBefore(dayTwoStart)) {
                return KoudaisaiDay.DayOne
            } else if (currentDate.isBefore(dayTwoEnd)) {
                return KoudaisaiDay.DayTwo
            } else {
                return null
            }
        } else {
            val currentDate = Calendar.getInstance()
            val startDate = Calendar.getInstance()
            startDate.set(2022, 10, 19, 0, 0, 0)
            val dayTwoStart = Calendar.getInstance()
            dayTwoStart.set(2022, 10, 20, 0, 0, 0)
            val dayTwoEnd = Calendar.getInstance()
            dayTwoEnd.set(2022, 10, 21, 0, 0, 0)
            if (currentDate < startDate) {
                return null
            } else if (currentDate < dayTwoStart) {
                return KoudaisaiDay.DayOne
            } else if (currentDate < dayTwoEnd) {
                return KoudaisaiDay.DayTwo
            } else {
                return null
            }
        }
    }
}