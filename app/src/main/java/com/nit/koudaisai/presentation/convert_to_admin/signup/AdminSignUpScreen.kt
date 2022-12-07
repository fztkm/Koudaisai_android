package com.nit.koudaisai.presentation.convert_to_admin.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.presentation.reserve.components.*
import com.nit.koudaisai.presentation.reserve.resereve_screen.haveNetworkAccess

@Composable
fun AdminSignUpScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFail: () -> Unit,
    viewModel: AdminSignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            ReserveTopBar("受付用アカウント作成") {
                onBackClicked()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KofunmanCard(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                message = "以下の情報を入力してください"
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputUserDataField(
                    label = "氏名", mandatory = false,
                    text = state.user.name, onChangeText = viewModel::onChangeName
                )
                InputUserDataField(
                    label = "メールアドレス",
                    mandatory = false,
                    text = state.user.email,
                    keyboardType = KeyboardType.Email,
                    onChangeText = viewModel::onEmailChange
                )
                PasswordTextField(
                    label = "パスワード",
                    password = state.password,
                    onChangeText = viewModel::onPassWordChange
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val context = LocalContext.current
                SendingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "作成",
                    onClick = {
                        if (haveNetworkAccess(context)) {
                            viewModel.onReserveClick(onSuccess = onSuccess, onFail = onFail)
                        } else {
                            SnackbarManager.showMessage("ネットワーク接続がありません")
                        }
                    })
            }
            Spacer(modifier = Modifier.height(300.dp))
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

