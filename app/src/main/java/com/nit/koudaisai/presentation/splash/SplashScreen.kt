package com.nit.koudaisai.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nit.koudaisai.R
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    openAndPopUpHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    linkId: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(id = R.drawable.koudai_logo),
                contentDescription = "60th工大祭ロゴ",
                contentScale = ContentScale.Inside
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.koudai_logo),
                        contentDescription = "60th工大祭ロゴ",
                        contentScale = ContentScale.Inside
                    )
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = "名古屋工業大学\n第60回工大祭",
                        style = MaterialTheme.typography.caption,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(id = R.drawable.c0de_logo),
                        contentDescription = "C0deロゴ",
                        contentScale = ContentScale.Inside
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        modifier = Modifier.width(100.dp),
                        text = "名古屋工業大学\nプログラミング\nC0de",
                        style = MaterialTheme.typography.caption,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart(openAndPopUpHome)
    }
}