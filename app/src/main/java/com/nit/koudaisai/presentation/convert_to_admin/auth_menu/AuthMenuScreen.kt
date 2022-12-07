package com.nit.koudaisai.presentation.convert_to_admin.auth_menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.presentation.home.screen.info.BorderLoginButton
import com.nit.koudaisai.presentation.reserve.components.CircularButton
import com.nit.koudaisai.presentation.reserve.components.KofunmanCard

@Composable
fun AuthMenuScreen(
    onClickCreateAccount: () -> Unit,
    onClickLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KofunmanCard(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp),
            message = "受付処理を追加します,\nログインまたはアカウントの新規作成をして下さい．"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "工大祭予約アカウントを持っている場合はそちらでログイン．",
            fontWeight = FontWeight.Normal
        )
        BorderLoginButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(250.dp)
                .height(45.dp),
            onClickLogin = {
                onClickLogin()
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            "作成していない場合は受付用アカウントを新規作成",
            fontWeight = FontWeight.Normal
        )
        CircularCreateAccountButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(250.dp)
                .height(45.dp),
            onClickReserve = {
                onClickCreateAccount()
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CircularCreateAccountButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickReserve: () -> Unit = {}
) {
    CircularButton(
        modifier = modifier,
        onClick = onClickReserve,
        enabled = enabled,
        text = "新規作成"
    )
}