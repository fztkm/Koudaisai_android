package com.nit.koudaisai.presentation.reserve.resereve_screen

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.presentation.reserve.components.BackButton
import com.nit.koudaisai.presentation.reserve.components.KofunmanCard
import com.nit.koudaisai.presentation.reserve.components.ReserveTopBar
import com.nit.koudaisai.presentation.reserve.components.SendingButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TermsScreen(
    onReserveSuccess: () -> Unit,
    onReserveFail: () -> Unit,
    onBackClicked: () -> Unit,
    onDisAgree: () -> Unit,
    viewModel: ReserveViewModel,
) {
    var showDialog by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            ReserveTopBar("規約") {
                onBackClicked()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KofunmanCard(message = "以下のことを\nご了承願います", kofunmanBowing = true)
            Spacer(Modifier.height(20.dp))
            Text(
                text = "参加の条件\n" +
                        "・マスクを着用すること\n" +
                        "・アルコール消毒に協力すること\n" +
                        "・進入禁止場所に入らないこと\n" +
                        "・委員会が提示する新型コロナウイルス感染症対策に従うこと\n" +
                        "・運営者の指示に従うこと\n" +
                        "・ソーシャルディスタンス確保に協力すること\n" +
                        "・入場後に体調を崩した場合は速やかにスタッフに申し出ること"
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val context = LocalContext.current
                val keyboardController = LocalSoftwareKeyboardController.current
                BackButton(text = "同意しない", onClick = { showDialog = true })
                SendingButton(text = "同意する", onClick = {
                    keyboardController?.hide()
                    if (haveNetworkAccess(context)) {
                        viewModel.onReserveClick(onReserveSuccess, onReserveFail)
                    } else {
                        SnackbarManager.showMessage("ネットワーク接続がありません")
                    }
                })
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        onDisAgree()
                        showDialog = false
                    }) {
                        Text("同意しない")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("キャンセル")
                    }
                },
                title = null,
                text = { Text(text = "同意して頂けない場合，予約することができません.") }
            )
        }
        if (state.isRegisterSending) {
            Surface(
                color = Color(0x77666666),
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        color = MaterialTheme.colors.primaryVariant,
                    )
                }
            }
        }
    }
}

fun haveNetworkAccess(context: Context): Boolean {
    val connectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    connectivityManager?.let {
        return it.activeNetwork != null
    }
    return true
}