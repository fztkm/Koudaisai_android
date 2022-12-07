package com.nit.koudaisai.presentation.admn

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.theme.KoudaisaiTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdmissionResultScreen(
    viewModel: AdmissionViewModel,
    popUpToHome: () -> Unit,
    popBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    KoudaisaiTheme {
        Scaffold(topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = popBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                    Text("読み取り結果", modifier = Modifier.weight(1f))
                    TextButton(onClick = popUpToHome, modifier = Modifier.padding(12.dp)) {
                        Text(
                            "完了", color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }) {
            Surface(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.isNowEntry == false) {
                        ResultText("既に入場処理の行われたIDです．")
                    } else if (state.isNowEntry == true) {
                        ResultText("入場OKです．")
                    } else if (state.isNotReserve) {
                        ResultText("本日の予約が行われていません")
                    }
                    Spacer(Modifier.height(20.dp))
                    state.visitorId?.let {
                        LabeledText(label = "id", text = it)
                    }
                    state.user?.let { user ->
                        LabeledText(label = "氏名", text = user.name)
                        Text(
                            "予約日程:",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                        if (user.dayOneSelected || user.dayTwoSelected) {
                            if (user.dayOneSelected) Text("・19日(土)")
                            if (user.dayTwoSelected) Text("・20日(日)")
                        } else {
                            Text("なし")
                        }
                    }
                    Divider(color = Color.LightGray, modifier = Modifier.height(1.dp))
                    Text("入場時間履歴")
                    state.user?.timestamps?.let { times ->
                        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE)
                        for (timestamp in times.reversed()) {
                            Text(
                                DateFormat.format(
                                    "yyyy/MM/dd (E) HH:mm:ss", timestamp.toDate()
                                ).toString(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultText(text: String) {
    Text(
        text, modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center, style = MaterialTheme.typography.h5
    )
}

@Composable
fun LabeledText(
    label: String,
    text: String
) {
    Column() {
        Text(
            "$label :",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body2
        )
        Text(text)
    }
}