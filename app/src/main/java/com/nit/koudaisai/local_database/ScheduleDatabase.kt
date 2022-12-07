package com.nit.koudaisai.local_database

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nit.koudaisai.R
import com.nit.koudaisai.domain.model.EventSchedule
import com.nit.koudaisai.utils.KoudaisaiDay

@Composable
fun getEventScheduleList(day: KoudaisaiDay): List<EventSchedule> {
    return when (day) {
        KoudaisaiDay.DayOne -> dayOneEventList
        KoudaisaiDay.DayTwo -> dayTwoEventList
    }
}

/**
 * 1日目のタイムテーブルのリスト
 */
private val dayOneEventList: List<EventSchedule>
    @Composable
    get() =
        listOf(
            EventSchedule(
                timeString = getTimeString(s1 = "10:00", s2 = "13:45"),
                name = "学生企画",
                location = "2号館ステージ",
                description = ""
            ),
            EventSchedule(
                timeString = getTimeString(s1 = "14:00", s2 = "14:20"),
                name = "模擬店PR",
                location = "2号館前ステージ",
                description = ""
            ),
            EventSchedule(
                timeString = getTimeString(s1 = "16:30", s2 = "18:00"),
                name = "中夜祭",
                location = "2号館前ステージ",
                description = "",
            )
        )

/**
 * 2日目のタイムテーブルのリスト
 */
private val dayTwoEventList: List<EventSchedule>
    @Composable
    get() = listOf(
        EventSchedule(
            timeString = getTimeString(s1 = "10:00", s2 = "10:40"),
            name = "模擬店PR",
            location = "2号館前ステージ",
            description = "",
            day = KoudaisaiDay.DayTwo,
        ),
        EventSchedule(
            timeString = getTimeString(s1 = "11:00", s2 = "13:30"),
            name = "昼戦会",
            location = "2号館前ステージ",
            description = "",
            day = KoudaisaiDay.DayTwo,
        ),
        EventSchedule(
            timeString = getTimeString(s1 = "12:30", s2 = "15:30"),
            name = "NIT LIVE FES 2022",
            location = "Nitech Hall",
            description = "",
            day = KoudaisaiDay.DayTwo,
        ),
        EventSchedule(
            timeString = getTimeString(s1 = "14:00", s2 = "15:00 (開場:12:30)"),
            name = "声優トークショー",
            location = "5111講義室",
            description = "",
            day = KoudaisaiDay.DayTwo,
        ),
        EventSchedule(
            timeString = getTimeString(s1 = "14:00", s2 = "15:00"),
            name = "Nitech25",
            location = "2号館前ステージ",
            description = "",
            day = KoudaisaiDay.DayTwo,
        ),
        EventSchedule(
            timeString = getTimeString(s1 = "15:30", s2 = "18:00"),
            name = "後夜祭",
            location = "2号館前ステージ",
            description = "",
            day = KoudaisaiDay.DayTwo,
        )
    )

/**
 *　stringResource()はComposableスコープでないと使えない．
 */
@Composable
fun getTimeString(s1: String, s2: String): String {
    return stringResource(id = R.string.event_time, s1, s2)
}

