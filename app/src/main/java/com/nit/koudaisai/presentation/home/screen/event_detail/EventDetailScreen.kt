package com.nit.koudaisai.presentation.home.screen.event_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.domain.model.Event

@Composable
fun EventDetailScreen(
    event: Event?,
    onClickLocation: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            event?.latLngList?.let {
                FloatingActionButton(
                    onClick = { onClickLocation(event.name) },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colors.surface
                    )
                }
            }
        }
    ) { paddingValues ->
        event?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(28.dp))
                event.img?.let {
                    Image(
                        painterResource(id = it), contentDescription = null,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Inside
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = event.name,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "場所", style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = event.location, style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
                Divider(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .height(1.dp), color = Color.LightGray
                )
                Text(
                    text = "時刻", style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = event.date, style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
                for (eventContent in event.description) {
                    eventContent.title?.let { title ->
                        Divider(
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                                .height(1.dp), color = Color.LightGray
                        )
                        Text(
                            text = title, style = MaterialTheme.typography.body1,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                    if (eventContent.subtitle != null || eventContent.img != null ||
                        eventContent.text != null || eventContent.video != null
                    )
                        Column(
                            modifier = Modifier.padding(start = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            eventContent.subtitle?.let { subtitle ->
                                Text(
                                    text = subtitle, style = MaterialTheme.typography.body1,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                            eventContent.img?.let { img ->
                                Image(painterResource(id = img), contentDescription = null)
                            }
                            eventContent.text?.let { text ->
                                Text(
                                    text = text, style = MaterialTheme.typography.body2,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start =
                                            if (eventContent.subtitle != null &&
                                                eventContent.img == null
                                            ) 8.dp else 0.dp
                                        ),
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start
                                )
                            }
                            eventContent.video?.let {

                            }
                        }
                }
                Spacer(Modifier.height((58 + 12).dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

}