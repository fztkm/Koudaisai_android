package com.nit.koudaisai.presentation.signIn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nit.koudaisai.presentation.reserve.components.*
import com.nit.koudaisai.theme.KoudaisaiTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    onBackPressed: () -> Unit,
    popUpToHome: () -> Unit
) {
    val viewModel: SignInViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        KofunmanCard(message = "メールアドレスと予約時に入力したパスワードを入力してね")
        Spacer(modifier = Modifier.height(20.dp))
        InputTextField(
            label = "メールアドレス", text = viewModel.email,
            keyboardType = KeyboardType.Email, onChangeText = viewModel::onEmailChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        PasswordTextField(
            label = "パスワード",
            password = viewModel.password,
            onChangeText = viewModel::onPassWordChange,
            imeAction = ImeAction.Done,
            showHint = false
        )
        TextButton(onClick = { viewModel.showSendRecoveryMailDialog() }) {
            Text(
                text = "パスワードを忘れた方はこちら"
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 0.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            BackButton(text = "戻る", onClick = {
                keyboardController?.hide()
                onBackPressed()
            })
            SendingButton(
                text = "ログイン", onClick = {
                    keyboardController?.hide()
                    viewModel.onSignInClick(popUpToHome)
                })
        }
        Spacer(modifier = Modifier.weight(4f))
    }
    if (viewModel.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x10555555))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = MaterialTheme.colors.primaryVariant,
            )
        }
    }
    if (viewModel.showDialog) {
        AlertDialog(
            title = { Text("パスワードの再設定") },
            text = { Text("パスワード再設定メールを送信します．") },
            onDismissRequest = viewModel::closeSendRecoveryMailDialog,
            confirmButton = {
                TextButton(onClick = viewModel::onSendRecoveryMail) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::closeSendRecoveryMailDialog) {
                    Text("キャンセル")
                }
            }
        )
    }
}


@Preview
@Composable
private fun SignInScreenPreview() {
    KoudaisaiTheme {
        Surface {
            SignInScreen({}, {})
        }
    }
}