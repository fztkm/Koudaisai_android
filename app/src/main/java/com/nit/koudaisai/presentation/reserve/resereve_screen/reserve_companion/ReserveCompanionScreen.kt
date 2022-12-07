package com.nit.koudaisai.presentation.reserve.resereve_screen.reserve_companion

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.presentation.reserve.components.*
import com.nit.koudaisai.presentation.reserve.resereve_screen.CheckTicketPossession
import com.nit.koudaisai.presentation.reserve.resereve_screen.haveNetworkAccess

@Composable
fun ReserveCompanionScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFail: () -> Unit,
    viewModel: ReserveCompanionViewModel,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            ReserveTopBar("同伴者予約") {
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
                message = "同行する人の情報を教えてね"
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputUserDataField(
                    label = "お名前", mandatory = true,
                    text = state.user.name, onChangeText = viewModel::onChangeName
                )
                InputUserDataField(
                    label = "メールアドレス",
                    mandatory = false,
                    text = state.user.email,
                    keyboardType = KeyboardType.Email,
                    onChangeText = viewModel::onEmailChange
                )
                InputUserDataField(
                    label = "電話番号", mandatory = false,
                    text = state.user.phoneNumber,
                    onChangeText = viewModel::onChangePhoneNumber,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            CheckTicketPossession(
                modifier = Modifier.padding(horizontal = 40.dp),
                label = "希望入場日",
                dayOneSelected = state.user.dayOneSelected,
                dayTwoSelected = state.user.dayTwoSelected,
                setDayOne = viewModel::onChangeDayOne,
                setDayTwo = viewModel::onChangeDayTwo,
                dayOneFull = state.isDayOneFull,
                dayTwoFull = state.isDayTwoFull
            )

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
                    text = "予約",
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

@Composable
fun CheckTicketPossessionForCompanion(
    modifier: Modifier = Modifier,
    label: String,
    dayOneSelected: Boolean,
    dayTwoSelected: Boolean,
    mandatory: Boolean = true,
    dayOneEnable: Boolean,
    dayTwoEnable: Boolean,
    setDayOne: (Boolean) -> Unit = {},
    setDayTwo: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(MaterialTheme.colors.onBackground)) {
                    append(label)
                }
                if (mandatory) withStyle(style = SpanStyle(MaterialTheme.colors.error)) {
                    append("(必須)")
                }
            },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        SelectedDayCheckBoxForCompanion(
            dayOneSelected = dayOneSelected,
            dayTwoSelected = dayTwoSelected,
            setDayOne = setDayOne,
            setDayTwo = setDayTwo,
            dayOneEnable = true,
            dayTwoEnable = true,
        )
    }
}

@Composable
fun SelectedDayCheckBoxForCompanion(
    dayOneSelected: Boolean,
    dayTwoSelected: Boolean,
    dayOneEnable: Boolean,
    dayTwoEnable: Boolean,
    setDayOne: (Boolean) -> Unit = {},
    setDayTwo: (Boolean) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextCheckbox(
            text = "11/19(土)",
            checked = dayOneSelected,
            onCheckedChange = { isCheck ->
                setDayOne(isCheck)
            },
            enabled = dayOneEnable,
            disabledColor = Color.Gray
        )
        TextCheckbox(
            text = "11/20(日)",
            checked = dayTwoSelected,
            onCheckedChange = { isCheck ->
                setDayTwo(isCheck)
            },
            enabled = dayTwoEnable,
            disabledColor = Color.Gray
        )
    }
}