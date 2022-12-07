package com.nit.koudaisai.presentation.convert_to_admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nit.koudaisai.R
import com.nit.koudaisai.presentation.convert_to_admin.splash.AdminSplashViewModel
import kotlinx.coroutines.delay

@Composable
fun AdminSplashScreen(
    openAndPopUpHome: () -> Unit,
    openAndPopUpMenu: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AdminSplashViewModel = hiltViewModel(),
    linkId: String? = null,
) {
    val isConvertAdminDone by viewModel.isCompletedMakeCurrentUserAdmin.collectAsState()
    val haveUser by viewModel.haveUser.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xff25488A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xff25488A)),
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
    }

    if (isConvertAdminDone) {
        LaunchedEffect(true) {
            delay(1000L)
            if (haveUser) {
                viewModel.onAppStart(openAndPopUpHome)
            } else {
                viewModel.onAppStart(openAndPopUpMenu)
            }
        }
    }
}