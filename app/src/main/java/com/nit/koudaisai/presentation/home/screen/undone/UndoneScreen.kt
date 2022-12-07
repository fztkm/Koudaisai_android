package com.nit.koudaisai.presentation.home.screen.undone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.presentation.reserve.components.KofunmanCard

@Composable
fun UndoneScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        KofunmanCard(message = "ただいま準備中です\nしばらくお待ち下さい", kofunmanBowing = true)
        Spacer(Modifier.weight(4f))
    }
}