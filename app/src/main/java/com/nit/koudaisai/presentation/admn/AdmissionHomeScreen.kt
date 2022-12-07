package com.nit.koudaisai.presentation.admn

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R
import com.nit.koudaisai.theme.KoudaisaiTheme
import com.nit.koudaisai.utils.KoudaisaiDay
import java.time.LocalDateTime
import java.util.*

@Composable
fun AdmissionHomeScreen(
    popUpToCamera: () -> Unit,
    popBack: () -> Unit
) {
    KoudaisaiTheme {
        Scaffold(
            topBar = {
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
                        Text("受付対応", modifier = Modifier.weight(1f))
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = popUpToCamera,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        modifier = Modifier.size(34.dp),
                        tint = Color.Black,
                        contentDescription = null
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(30.dp)
            ) {
                Text(
                    "手順\n\n1.検温\n" +
                            "2.QRコード読み取り右下のボタン\n" +
                            "3.パンフレットなどの入った袋とリストバンドを渡す"
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}