package com.nit.koudaisai.presentation.home.screen.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.theme.KoudaisaiTheme
import com.nit.koudaisai.utils.KoudaisaiDay


@Composable
fun SwitchDayTopBar(
    selectedDay: KoudaisaiDay,
    onClickDay: (index: KoudaisaiDay) -> Unit
) {
    KoudaisaiTheme{
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SwitchDayTopBarTile(
                modifier = Modifier.weight(1f),
                text = "1日目",
                day = KoudaisaiDay.DayOne,
                selectedDay = selectedDay,
                onClick = onClickDay
            )
            SwitchDayTopBarTile(
                modifier = Modifier.weight(1f),
                text = "2日目",
                day = KoudaisaiDay.DayTwo,
                selectedDay = selectedDay,
                onClick = onClickDay
            )
        }
    }
}

@Composable
private fun SwitchDayTopBarTile(
    modifier: Modifier = Modifier,
    text: String,
    day: KoudaisaiDay,
    selectedDay: KoudaisaiDay,
    onClick: (index: KoudaisaiDay) -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick(day) }
            .fillMaxWidth()
            .padding(top = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 3.dp),
                text = text,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle1
            )
            if (selectedDay == day) {
                Divider(
                    color = MaterialTheme.colors.primary,
                    thickness = 3.dp
                )
            } else {
                val strokeWidth = 1
                Divider(
                    modifier = Modifier.padding(top = (3 - strokeWidth).dp),
                    color = MaterialTheme.colors.onSurface,
                    thickness = strokeWidth.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopBarTilePreview() {
    KoudaisaiTheme{
        SwitchDayTopBarTile(
            text = "一日目",
            day = KoudaisaiDay.DayOne,
            selectedDay = KoudaisaiDay.DayOne,
            onClick = {})
    }
}

@Preview(heightDp = 100)
@Composable
private fun TopBarPreview() {
    KoudaisaiTheme {
        SwitchDayTopBar(selectedDay = KoudaisaiDay.DayTwo) {}
    }
}