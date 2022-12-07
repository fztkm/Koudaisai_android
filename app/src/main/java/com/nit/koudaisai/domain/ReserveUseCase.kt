package com.nit.koudaisai.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.nit.koudaisai.common.ext.toJpString
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.model.ReserveCapacity
import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.presentation.reserve.resereve_screen.Result
import com.nit.koudaisai.utils.KoudaisaiDay
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReserveUseCase @Inject constructor(
    private val accountService: AccountService,
    private val accountStorageService: AccountStorageService,
) {
    suspend fun getReserveCapacity(day: KoudaisaiDay): Result<ReserveCapacity> {
        return suspendCoroutine {
            accountStorageService.getDayCapacity(day,
                onError = { error ->
                    it.resume(Result.Error(error))
                },
                onSuccess = { reserveCapacity ->
                    it.resume(Result.Success(reserveCapacity))
                }
            )
        }
    }

    suspend fun incrementReserveCapacity(day: KoudaisaiDay): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.incrementTotalParticipants(day) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }

    suspend fun decrementReserveCapacity(day: KoudaisaiDay): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.decrementTotalParticipants(day) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }


    suspend fun isAvailableToReserve(day: KoudaisaiDay): Result<ReserveCapacity> {
        return suspendCoroutine {
            accountStorageService.getDayCapacity(day = day,
                onError = { error ->
                    it.resume(Result.Error(error))
                },
                onSuccess = { reserveCapacity ->
                    val remain = reserveCapacity.maxCapacity - reserveCapacity.totalParticipants
                    if (remain >= 1) {
                        it.resume(Result.Success(reserveCapacity))
                    } else {
                        it.resume(Result.Fail)
                    }
                }
            )
        }
    }

    suspend fun createAccount(email: String, password: String): Result<Boolean> {
        return suspendCoroutine {
            accountService.createAccount(
                email, password
            ) { error ->
                if (error != null) {
                    if (error is FirebaseAuthException) {
                        Log.i("AUTH", error.errorCode)
                        SnackbarManager.showMessage(error.toJpString())
                    } else {
                        SnackbarManager.showMessage(error.toSnackbarMessage())
                    }
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }

    suspend fun getUserData(uid: String): Result<KoudaisaiUser> {
        return suspendCoroutine {
            accountStorageService.getUser(
                userId = uid,
                onError = { error ->
                    it.resume(Result.Error(error))
                },
                onSuccess = { user ->
                    it.resume(Result.Success(user))
                }
            )
        }
    }

    suspend fun updateUserData(uid: String, user: KoudaisaiUser): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.updateUser(
                uid = uid,
                user = user
            ) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }

    suspend fun sendUserData(user: KoudaisaiUser): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.saveUser(
                uid = accountService.getUserId(),
                user = user
            ) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }

    suspend fun sendUserData(id: String, user: KoudaisaiUser): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.saveUser(
                uid = id,
                user = user
            ) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }

    suspend fun sendSubUserData(user: KoudaisaiUser): Result<String> {
        return suspendCoroutine {
            accountStorageService.saveSubUser(
                user = user,
                onError = { error ->
                    it.resume(Result.Error(error))
                },
                onSuccess = { id ->
                    it.resume(Result.Success(id))
                }
            )
        }
    }

    suspend fun deleteUser(id: String): Result<Boolean> {
        return suspendCoroutine {
            accountStorageService.deleteUser(userId = id) { error ->
                if (error != null) {
                    it.resume(Result.Error(error))
                } else {
                    it.resume(Result.Success(true))
                }
            }
        }
    }
}