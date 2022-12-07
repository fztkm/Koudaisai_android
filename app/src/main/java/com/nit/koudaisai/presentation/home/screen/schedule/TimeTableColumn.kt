package com.nit.koudaisai.presentation.home.screen.schedule

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.nit.koudaisai.R
import com.nit.koudaisai.domain.model.EventSchedule
import com.nit.koudaisai.presentation.home.screen.settings.SettingItem
import com.nit.koudaisai.theme.KoudaisaiTheme

@Composable
fun TimeTableColumn(
    modifier: Modifier = Modifier,
    items: List<EventSchedule>
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 25.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items.forEach {
                    EventCard(event = it)
                }
            }
        }
    }
}

@Composable
private fun EventCard(
    event: EventSchedule,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.onSecondary
    ) {
        var isOpen by remember(event.day) {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = event.timeString,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = event.name,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            if (isOpen) {
                Spacer(modifier = Modifier.height(10.dp))
                ExpandItem(event = event)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                modifier = Modifier.padding(0.dp),
                onClick = { isOpen = !isOpen }) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(
                        id = if (isOpen) R.drawable.expand_less
                        else R.drawable.expand_more
                    ),
                    contentDescription = "",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}


@Composable
private fun ExpandItem(
    event: EventSchedule
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "場所",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Normal,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                text = event.location,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        }
        val context = LocalContext.current
        event.link?.let { map ->
            for ((label, url) in map) {
                SettingItem(text = label,
                    textColor = MaterialTheme.colors.onBackground,
                    header = {
                        Icon(
                            painterResource(id = R.drawable.open_in_browser),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.Gray
                        )
                    }) {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    ContextCompat.startActivity(context, intent, null)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = event.description,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
    }
}
