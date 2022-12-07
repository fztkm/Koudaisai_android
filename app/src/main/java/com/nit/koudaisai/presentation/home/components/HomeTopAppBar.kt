package com.nit.koudaisai.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.graph.Overview
import com.nit.koudaisai.graph.Schedule
import com.nit.koudaisai.theme.KoudaisaiTheme

@Composable
fun HomeTopAppBar(
    currentTitle: String?,
) {
    val appBarHorizontalPadding = 4.dp
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .width(72.dp - appBarHorizontalPadding)

    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = if (currentTitle == Schedule.title) 0.dp else 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {

        //TopAppBar Content
        Box(Modifier.height(32.dp)) {

            //Navigation Icon
            Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                //ハンバーガーアイコン削除
                //TODO 戻るボタン必要なときはここに
            }
            //Title
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = currentTitle ?: "第60回工大祭",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeTopAppBar() {
    KoudaisaiTheme {
        HomeTopAppBar(currentTitle = Overview.title)
    }
}
