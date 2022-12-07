package com.nit.koudaisai.presentation.home.screen.settings

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nit.koudaisai.R
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.presentation.reserve.components.InputTextField
import com.nit.koudaisai.presentation.reserve.components.PasswordTextField
import com.nit.koudaisai.presentation.reserve.resereve_screen.haveNetworkAccess
import java.time.LocalDateTime
import java.util.*


@Composable
fun SettingsScreen(
    popUpToSplash: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 20.dp, top = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SettingTitle(text = "サポート")
        SettingItem(text = "よくある質問",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    modifier = Modifier.size(28.dp),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }) {
            viewModel.openQuestionPage(context) //TODO 質問ページ実装出来次第そちらに飛ばす
        }
        SettingItem(text = "お問い合わせ",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    modifier = Modifier.size(28.dp),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }) {
            viewModel.openInquiry(context)
        }
        Spacer(Modifier.height(16.dp))
        SettingTitle(text = "Webサイト・SNS")
        SettingItem(text = "工大祭ホームページ",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Gray
                )
            }
        ) {
            viewModel.openKoudaisaiHomePage(context)
        }
        SettingItem(text = "工大祭公式Twitter",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Gray
                )
            }) {
            viewModel.openKoudaisaiTwitter(context)
        }
        SettingItem(text = "工大祭公式Instagram",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Gray
                )
            }) {
            viewModel.openKoudaisaiInstagram(context)
        }
        Spacer(Modifier.height(16.dp))
        //TODO プライバシーポリシーのページに飛ばす
        if (state.hasUser) {
            SettingTitle(text = "認証")
            val context = LocalContext.current
            SettingItem(text = "ログアウト") {
                viewModel.showLogoutDialog()
            }
            if (!state.isSuperUser) {
                SettingItem(text = "パスワード再設定") {
                    viewModel.showResetPasswordDialog()
                }
                if (isEditable()) SettingItem(text = "予約情報の削除") {
                    if (haveNetworkAccess(context)) {
                        viewModel.showDeleteDataDialog()
                    } else {
                        SnackbarManager.showMessage("ネットワーク接続がありません")
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        SettingTitle(text = "アプリ情報")
        SettingItem(text = "プライバシーポリシー",
            header = {
                Icon(
                    painterResource(id = R.drawable.open_in_browser),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Gray
                )
            }) {
            viewModel.openPrivacyPolicy(context)
        }
        SettingItem(text = "権利表記") {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)
            OssLicensesMenuActivity.setActivityTitle("権利表記");
            context.startActivity(intent)
        }
        Spacer(Modifier.height(16.dp))
        if (state.showResetPasswordDialog) {
            AlertDialog(
                title = { Text("パスワードの再設定") },
                text = { Text("パスワードの再設定メールを送信します．") },
                onDismissRequest = viewModel::closeResetPasswordDialog,
                confirmButton = {
                    TextButton(onClick = viewModel::sendPasswordResetMail) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::closeResetPasswordDialog) {
                        Text("キャンセル")
                    }
                },
            )
        }
        if (state.showLogoutDialog) {
            AlertDialog(
                title = { Text("ログアウト") },
                text = { Text("ログアウトします．よろしいですか．") },
                onDismissRequest = viewModel::closeLogoutDialog,
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onLogout(popUpToSplash)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::closeLogoutDialog) {
                        Text("キャンセル")
                    }
                },
            )
        }
        if (state.showCleanUpUserDataDialog) {
            AlertDialog(
                title = { Text("予約情報の削除") },
                text = {
                    Column {
                        Text("予約情報を全て削除します．同伴者も含めて予約は全てキャンセルされます．")
                        Spacer(modifier = Modifier.height(10.dp))
                        InputTextField(
                            label = "メールアドレス",
                            mandatory = false,
                            text = state.email,
                            onChangeText = viewModel::setEmail
                        )
                        PasswordTextField(
                            label = "パスワード",
                            password = state.password,
                            mandatory = false,
                            showHint = false,
                            onChangeText = viewModel::setPassword
                        )
                    }
                    if (state.isLoading) {
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
                                    modifier = Modifier.size(50.dp),
                                    color = MaterialTheme.colors.primaryVariant,
                                )
                            }
                        }
                    }
                },
                onDismissRequest = viewModel::closeDeleteDataDialog,
                confirmButton = {
                    TextButton(onClick = { viewModel.onClickDelete(popUpToSplash) }) {
                        Text("削除する", color = MaterialTheme.colors.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::closeDeleteDataDialog) {
                        Text("戻る", color = MaterialTheme.colors.onBackground)
                    }
                }
            )
        }
    }
}

@Composable
fun SettingTitle(
    text: String
) {
    Text(text, style = MaterialTheme.typography.body1)
}

@Composable
fun SettingItem(
    header: @Composable () -> Unit = {},
    text: String,
    textColor: Color = MaterialTheme.colors.onBackground,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
        onClick()
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                header()
                Spacer(Modifier.width(6.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = text,
                    color = textColor,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal
                )
            }
            if (showDivider) Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = Color.LightGray
            )
        }

    }
}

private fun isEditable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDate = LocalDateTime.now()
        val startDate = LocalDateTime.of(2022, 11, 19, 0, 0, 0)
        currentDate.isBefore(startDate)
    } else {
        val currentDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        startDate.set(2022, 10, 19, 0, 0, 0)
        currentDate < startDate
    }
}