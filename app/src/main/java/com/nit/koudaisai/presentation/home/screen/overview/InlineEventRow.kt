package com.nit.koudaisai.presentation.home.screen.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R
import com.nit.koudaisai.domain.model.Event
import com.nit.koudaisai.local_database.EventDatabase
import com.nit.koudaisai.theme.KoudaisaiTheme

@Composable
fun InlineEventRow(
    items: List<Event>,
    header: @Composable () -> Unit,
) {
    KoudaisaiTheme {
        Column {
            header()
            LazyRow {
                items(items = items) {
                    EventCard(event = it) {}
                }
            }
        }
    }
}

@Composable
fun HeaderText(
    header: String
) {
    Text(
        modifier = Modifier
            .padding(start = 16.dp)
            .padding(bottom = 10.dp),
        text = header,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun EventCard(
    event: Event,
    onClickEvent: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clickable { onClickEvent() }
            .height(200.dp)
            .width(IntrinsicSize.Min),
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            painter = painterResource(id = event.img ?: R.drawable.kikaku),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxHeight(),
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0x88000000))
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    event.name,
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Preview
@Composable
private fun InlineEventRowPreview() {
    KoudaisaiTheme {
        Surface {
            InlineEventRow(items = EventDatabase.generalEventList) {
                HeaderText(header = "企画")
            }
        }
    }
}

@Preview
@Composable
private fun HeaderTextPreview() {
    KoudaisaiTheme {
        Surface {
            HeaderText(header = "企画")
        }
    }
}

@Preview(name = "EventCard")
@Composable
private fun PreviewEventCard() {
    EventCard(event = EventDatabase.generalEventList[0]) {

    }
}