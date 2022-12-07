package com.nit.koudaisai.domain.model

import com.nit.koudaisai.utils.KoudaisaiDay

data class EventSchedule(
    val timeString: String,
    val name: String,
    val location: String,
    val description: String,
    val day: KoudaisaiDay = KoudaisaiDay.DayOne,
    val link: Map<String, String>? = null
)