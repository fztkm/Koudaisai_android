package com.nit.koudaisai.presentation.home.screen.schedule

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nit.koudaisai.local_database.getEventScheduleList
import com.nit.koudaisai.presentation.home.screen.undone.UndoneScreen
import com.nit.koudaisai.theme.KoudaisaiTheme
import com.nit.koudaisai.utils.KoudaisaiDay
import java.time.LocalDateTime
import java.util.*

@Composable
fun ScheduleScreen() {
    if (showTimeTable()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            var selectedDay by remember { mutableStateOf(KoudaisaiDay.DayOne) }
            SwitchDayTopBar(selectedDay = selectedDay) { day ->
                selectedDay = day
            }
            val eventList = getEventScheduleList(day = selectedDay)
            TimeTableColumn(items = eventList)
        }
    } else{
        UndoneScreen()
    }
}

private fun showTimeTable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDateTime.now()
        val startDate = LocalDateTime.of(2022, 11, 9, 0, 0, 0)
        currentDate.isAfter(startDate)
    } else {
        val currentDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        startDate.set(2022, 10, 9, 0, 0, 0)
        val diff = currentDate.compareTo(startDate)
        diff > 0
    }
}


@Preview
@Composable
private fun ScheduleScreenPreview() {
    KoudaisaiTheme {
        Surface {
            ScheduleScreen()
        }
    }
}

