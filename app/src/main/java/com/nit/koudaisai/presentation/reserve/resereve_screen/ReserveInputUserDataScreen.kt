package com.nit.koudaisai.presentation.reserve.resereve_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.presentation.reserve.components.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReserveInputUserDataScreen(
    onClickNext: () -> Unit,
    onBackClicked: () -> Unit,
    viewModel: ReserveViewModel,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            ReserveTopBar("新規予約") {
                onBackClicked()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(40.dp))
            KofunmanCard(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                message = "予約のため\n次のことを教えてね"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputUserDataField(
                    label = "メールアドレス",
                    mandatory = true,
                    text = state.user.email,
                    keyboardType = KeyboardType.Email,
                    onChangeText = viewModel::onEmailChange
                )
                PasswordTextField(
                    label = "パスワード",
                    mandatory = true,
                    password = state.password,
                    onChangeText = viewModel::onPassWordChange
                )
                InputUserDataField(
                    label = "お名前", mandatory = true,
                    text = state.user.name, onChangeText = viewModel::onChangeName
                )
                InputUserDataField(
                    label = "電話番号", mandatory = true,
                    text = state.user.phoneNumber,
                    onChangeText = viewModel::onChangePhoneNumber,
                    keyboardType = KeyboardType.Phone
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            CheckTicketPossession(
                modifier = Modifier.padding(horizontal = 40.dp),
                label = "希望入場日",
                dayOneSelected = state.user.dayOneSelected,
                dayTwoSelected = state.user.dayTwoSelected,
                setDayOne = viewModel::onChangeDayOne,
                setDayTwo = { selected ->
                    viewModel.onChangeDayTwo(selected)
                },
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
                val keyboardController = LocalSoftwareKeyboardController.current
                SendingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "予約"
                ) {
                    keyboardController?.hide()
                    viewModel.onNextClick {
                        onClickNext()
                    }
                }
            }
            Spacer(modifier = Modifier.height(300.dp))
        }
    }
}


@Composable
fun CheckTicketPossession(
    modifier: Modifier = Modifier,
    label: String,
    dayOneSelected: Boolean,
    dayTwoSelected: Boolean,
    dayOneFull: Boolean,
    dayTwoFull: Boolean,
    mandatory: Boolean = true,
    setDayOne: (Boolean) -> Unit = {},
    setDayTwo: (Boolean) -> Unit = {},
    enable: Boolean = true,
    disableColor: Color? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "e+で声優トークショーやバンドのチケットを購入された方は11/20(日)の予約が不要です\n",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.height(10.dp))
        SelectedDayCheckBox(
            dayOneSelected = dayOneSelected,
            dayTwoSelected = dayTwoSelected,
            enable = enable,
            setDayOne = setDayOne,
            setDayTwo = setDayTwo,
            disableColor = disableColor,
            dayOneFull = dayOneFull,
            dayTwoFull = dayTwoFull
        )
    }
}

@Composable
fun SelectedDayCheckBox(
    dayOneSelected: Boolean,
    dayTwoSelected: Boolean,
    enable: Boolean,
    dayOneFull: Boolean,
    dayTwoFull: Boolean,
    setDayOne: (Boolean) -> Unit = {},
    setDayTwo: (Boolean) -> Unit = {},
    disableColor: Color? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TextCheckbox(
            text = "11/19(土)",
            checked = dayOneSelected,
            onCheckedChange = { isCheck ->
                setDayOne(isCheck)
            },
            enabled = enable && !dayOneFull,
            disabledColor = disableColor
        )
        if (dayOneFull) Text(
            text = "19日(土)はすでに予約がいっぱいです",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error
        )
        TextCheckbox(
            text = "11/20(日)",
            checked = dayTwoSelected,
            onCheckedChange = { isCheck ->
                setDayTwo(isCheck)
            },
            enabled = enable && !dayTwoFull,
            disabledColor = disableColor
        )
        if (dayTwoFull) Text(
            text = "20日(日)はすでに予約がいっぱいです",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error
        )
    }
}
