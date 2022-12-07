package com.nit.koudaisai.presentation.home.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.nit.koudaisai.common.KoudaisaiLink
import com.nit.koudaisai.domain.service.impl.FirebaseAccountServiceImpl

@Composable
fun HomeDrawer(
    popUpToSplash: () -> Unit,
    closeDrawer: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(start = 10.dp, end = 20.dp, top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        var showDialog by remember{ mutableStateOf(false)}
        DrawerItem(text = "予約情報") {
            /*TODO*/
            closeDrawer()
        }
        DrawerItem(text = "工大祭ホームページ") {
            val uri = Uri.parse(KoudaisaiLink.homepage)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            ContextCompat.startActivity(context, intent, null)
            closeDrawer()
        }
        DrawerItem(text = "よくある質問") {
            val uri = Uri.parse(KoudaisaiLink.homepage)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            ContextCompat.startActivity(context, intent, null)
            closeDrawer()
        }
        DrawerItem(text = "ログアウト") {
            showDialog = true
            val accountService = FirebaseAccountServiceImpl()
            accountService.signOut()
            popUpToSplash()
            closeDrawer()
        }
        /**TODO クレジット*/
        DrawerItem(text = "クレジット") {
            closeDrawer()
        }
        if(showDialog){
            AlertDialog(
                title = {Text("ログアウト")},
                text = {Text("ログアウトします．よろしいですか．")},
                onDismissRequest = {showDialog = false},
                confirmButton = {
                    TextButton(onClick = {
                        val accountService = FirebaseAccountServiceImpl()
                        accountService.signOut()
                        popUpToSplash()
                        closeDrawer()
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = { TextButton(onClick = { showDialog = false}) {
                    Text("キャンセル")
                }},
            )
        }
    }
}

@Composable
private fun DrawerItem(
    text: String,
    onClick: () -> Unit,
) {
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
        onClick()
    }) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = text,
            color = MaterialTheme.colors.onBackground,
            fontSize = 20.sp
        )
    }
}
