package com.nit.koudaisai.domain.model

data class ReserveCapacity(
    val totalParticipants: Int = -1,
    val maxCapacity: Int = Int.MAX_VALUE,
)