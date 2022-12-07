package com.nit.koudaisai.presentation.admn

import com.nit.koudaisai.domain.model.KoudaisaiUser

data class AdmissionState(
    val visitorId: String?,
    val user: KoudaisaiUser?,
    val isLoading: Boolean,
    val isNotReserve: Boolean,
    val isNowEntry: Boolean?
) {
    companion object {
        fun empty() =
            AdmissionState(
                visitorId = null,
                user = null,
                isLoading = false,
                isNotReserve = false,
                isNowEntry = null
            )
    }
}