package com.nit.koudaisai.presentation.home.screen.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    message: String = "第60回工大祭へようこそ!",
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            modifier = Modifier
                .width(50.dp),
            painter = painterResource(id = R.drawable.kohunman),
            contentDescription = "古墳マン",
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            border = BorderStroke(2.dp, MaterialTheme.colors.primary),
            shape = RoundedCornerShape(15.dp),
            elevation = 6.dp,
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = message,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "InfoCard", backgroundColor = 0x000000)
@Composable
private fun PreviewInfoCard() {
    InfoCard()
}

@Preview(name = "InfoCard", widthDp = 300, heightDp = 200)
@Composable
private fun InfoCardPreview() {
    InfoCard(
        message = "新型コロナウイルス感染対策のため,\n" +
                "体調の悪い方の入場を" +
                "ご遠慮いただいております.\n" +
                "つきましては皆様に入場日に" +
                "体調フォームの送信を" +
                "お願いしています.\n" +
                "体調フォームはこちら"
    )
}