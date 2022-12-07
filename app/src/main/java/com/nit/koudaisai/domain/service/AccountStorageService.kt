package com.nit.koudaisai.domain.service

import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.domain.model.ReserveCapacity
import com.nit.koudaisai.utils.KoudaisaiDay

interface AccountStorageService {

    fun getUser(userId: String, onError: (Throwable) -> Unit, onSuccess: (KoudaisaiUser) -> Unit)
    fun addUserListener(
        userId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (KoudaisaiUser) -> Unit
    )

    fun removeListener()

    fun saveUser(uid: String, user: KoudaisaiUser, onResult: (Throwable?) -> Unit)
    fun saveSubUser(
        user: KoudaisaiUser,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun updateUser(uid: String, user: KoudaisaiUser, onResult: (Throwable?) -> Unit)
    fun deleteUser(userId: String, onResult: (Throwable?) -> Unit)
    fun getDayCapacity(
        day: KoudaisaiDay,
        onError: (Throwable) -> Unit,
        onSuccess: (ReserveCapacity) -> Unit
    )

    fun setDayCapacity(day: KoudaisaiDay, totalParticipants: Int, onResult: (Throwable?) -> Unit)
    fun incrementTotalParticipants(day: KoudaisaiDay, onResult: (Throwable?) -> Unit)
    fun decrementTotalParticipants(day: KoudaisaiDay, onResult: (Throwable?) -> Unit)
    fun decrementTotalParticipants(day: KoudaisaiDay, count: Int, onResult: (Throwable?) -> Unit)
}