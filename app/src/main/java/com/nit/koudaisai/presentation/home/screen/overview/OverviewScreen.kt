package com.nit.koudaisai.presentation.home.screen.overview

import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.nit.koudaisai.common.KoudaisaiLink
import com.nit.koudaisai.local_database.EventDatabase
import com.nit.koudaisai.theme.KoudaisaiTheme
import java.time.LocalDateTime
import java.util.*


@Composable
fun OverviewScreen(
    onClickEvent: (String) -> Unit = {},
) {
    val eventList = remember {
        EventDatabase.generalEventList
    }
    val specialList = remember {
        EventDatabase.specialEventList
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "お知らせ", modifier = Modifier.padding(start = 12.dp),
                color = MaterialTheme.colors.primary, style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    "中夜祭・後夜祭の観覧エリアに入場するには当日配布する整理券が必要です。",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Normal
                )
                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ){
                    TextButton(
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        ),
                        onClick = {
                        onClickEvent("中夜祭")
                    }) {
                        Text("中夜祭", color = Color.White,
                            style = MaterialTheme.typography.caption)
                    }
                    TextButton(
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        ),
                        onClick = {
                            onClickEvent("後夜祭")
                        }) {
                        Text("後夜祭", color = Color.White,
                            style = MaterialTheme.typography.caption)
                    }
                }

            }
        }
        LazyRow() {
            items((0..5).toList()) { index ->
                EventCard(event = eventList[index]) {
                    onClickEvent(eventList[index].name)
                }
            }
        }
        LazyRow() {
            items((6..11).toList()) { index ->
                EventCard(event = eventList[index]) {
                    onClickEvent(eventList[index].name)
                }
            }
        }
        LazyRow() {
            items((12 until eventList.size).toList()) { index ->
                EventCard(event = eventList[index]) {
                    onClickEvent(eventList[index].name)
                }
            }
        }
        LazyRow() {
            items(specialList) { event ->
                EventCard(event = event) {
                    onClickEvent(event.name)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        //TODO すべての企画を表示する
        if (isShowWebView()) {
            MyWebClient(url = KoudaisaiLink.mystery)
            Spacer(Modifier.height(12.dp))
            MyWebClient(url = KoudaisaiLink.ranking)
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
fun MyWebClient(url: String) {
    AndroidView(
        factory = ::WebView,
        update = { webView ->
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
            val settings = webView.settings
            settings.javaScriptEnabled = true
        }
    )
}

private fun isShowWebView(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDateTime.now()
        val startDate = LocalDateTime.of(2022, 11, 19, 0, 0, 0)
        currentDate.isAfter(startDate)
    } else {
        val currentDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        startDate.set(2022, 10, 19, 0, 0, 0)
        currentDate > startDate
    }
}

@Preview
@Composable
private fun OverviewScreenPreview() {
    KoudaisaiTheme {
        Surface {
            OverviewScreen()
        }
    }
}




