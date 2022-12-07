package com.nit.koudaisai.presentation.reserve.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R
import com.nit.koudaisai.theme.KoudaisaiTheme

@Composable
fun KofunmanCard(
    modifier: Modifier = Modifier,
    kofunmanBowing: Boolean = false,
    message: String = "古墳マンだよ!",
) {
    Row(
        modifier = modifier,
    ) {
        Image(
            modifier = Modifier
                .width(50.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(
                id = if(kofunmanBowing) R.drawable.kofunman_bow else R.drawable.kohunman),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = message,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(widthDp = 400, heightDp = 500, showBackground = true)
@Composable
private fun KofunmanCard() {
    KoudaisaiTheme {
        Surface {
            KofunmanCard(message = "チケットを購入してくれてありがとう!")
        }
    }
}