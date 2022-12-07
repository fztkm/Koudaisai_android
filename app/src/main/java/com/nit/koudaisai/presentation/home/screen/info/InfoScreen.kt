package com.nit.koudaisai.presentation.home.screen.info

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R
import com.nit.koudaisai.common.ext.toIntPixel
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.domain.model.KoudaisaiUser
import com.nit.koudaisai.presentation.reserve.components.CircularButton
import com.nit.koudaisai.presentation.reserve.components.ColorBorderButton
import com.nit.koudaisai.presentation.reserve.components.KofunmanCard
import com.nit.koudaisai.presentation.reserve.components.TextCheckbox
import com.nit.koudaisai.presentation.reserve.resereve_screen.haveNetworkAccess
import com.nit.koudaisai.theme.KoudaisaiTheme
import java.time.LocalDateTime
import java.util.*

enum class OpenState {
    CLOSE,
    OPEN,
    EDIT;

    fun next() = when (this) {
        CLOSE -> OPEN
        OPEN -> EDIT
        EDIT -> CLOSE
    }

    fun toggleOpen() = when (this) {
        CLOSE -> OPEN
        OPEN -> CLOSE
        EDIT -> CLOSE
    }

    fun close() = CLOSE
}

@Composable
fun InfoScreen(
    onClickReserve: () -> Unit,
    onClickLogin: () -> Unit,
    onClickAddCompanion: () -> Unit,
    onReload: () -> Unit,
    onClickAdmin: () -> Unit,
    viewModel: InfoViewModel
) {
    val state by viewModel.state.collectAsState()
    var parentCardState by remember {
        mutableStateOf(OpenState.CLOSE)
    }
    var sub1CardState by remember {
        mutableStateOf(OpenState.CLOSE)
    }
    var sub2CardState by remember {
        mutableStateOf(OpenState.CLOSE)
    }
    KoudaisaiTheme {
        if (state.user != null) {
            Scaffold(
                floatingActionButton = {
                    if (state.user?.isAdmin == true) FloatingActionButton(
                        onClick = {
                            onClickAdmin()
                        },
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ) {
                        Icon(
                            painterResource(id = R.drawable.follow_the_signs),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            ) { paddingValues ->
                Surface(modifier = Modifier.padding(paddingValues)) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        val qrPixSize = 180.dp.toIntPixel()
                        Text(
                            "工大祭当日は入場時に，QRコードアイコンをタップして表示されるQRコードを係員に提示して下さい",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body1
                        )
                        MainUserCard(
                            user = state.user!!,
                            onChangeName = viewModel::onChangeName,
                            onSend = viewModel::onUpdateMainUser,
                            showQR = {
                                viewModel.displayQRCode(
                                    InfoViewModel.UserType.PARENT,
                                    qrPixSize
                                )
                            },
                            state = parentCardState,
                            closeOtherCards = {
                                sub1CardState = sub1CardState.close()
                                sub2CardState = sub2CardState.close()
                            },
                            setOpenState = { openState ->
                                parentCardState = openState
                            },
                            setPrevUserData = { viewModel.onSetPrevData() },
                            cancelEdit = { viewModel.resetEditData() },
                            onChangeEmail = viewModel::onChangeEmail,
                            onChangePhoneNum = viewModel::onChangePhoneNumber,
                            onChangeDayOne = viewModel::onChangeDayOneSelected,
                            onChangeDayTwo = viewModel::onChangeDayTwoSelected,
                            isDayOneVisited = state.user!!.dayOneVisited,
                            isDayTwoVisited = state.user!!.dayTwoVisited,
                            isDayOneSelected = state.user!!.dayOneSelected,
                            isDayTwoSelected = state.user!!.dayTwoSelected,
                            dayOneFull = state.dayOneFull,
                            dayTwoFull = state.dayTwoFull
                        )
                        state.subUser1?.let { sub1 ->
                            SubUserCard(
                                user = sub1,
                                onChangeName = viewModel::onChangeNameS1,
                                onDelete = { viewModel.deleteSub1(onReload) },
                                onSend = viewModel::onUpdateSub1User,
                                showQR = {
                                    viewModel.displayQRCode(
                                        InfoViewModel.UserType.SUB1,
                                        qrPixSize
                                    )
                                },
                                state = sub1CardState,
                                closeOtherCards = {
                                    parentCardState = parentCardState.close()
                                    sub2CardState = sub2CardState.close()
                                },
                                setOpenState = { openState ->
                                    sub1CardState = openState
                                },
                                setPrevUserData = { viewModel.onSetPrevData() },
                                cancelEdit = { viewModel.resetEditData() },
                                onChangeEmail = viewModel::onChangeEmailS1,
                                onChangePhoneNum = viewModel::onChangePhoneNumS1,
                                onChangeDayOne = viewModel::onChangeDayOneSelectedS1,
                                onChangeDayTwo = viewModel::onChangeDayTwoSelectedS1,
                                isDayOneVisited = sub1.dayOneVisited,
                                isDayTwoVisited = sub1.dayTwoVisited,
                                isDayOneSelected = sub1.dayOneSelected,
                                isDayTwoSelected = sub1.dayTwoSelected,
                                dayOneFull = state.dayOneFull,
                                dayTwoFull = state.dayTwoFull
                            )
                        }
                        state.subUser2?.let { sub2 ->
                            SubUserCard(
                                user = sub2,
                                onChangeName = viewModel::onChangeNameS2,
                                onDelete = { viewModel.deleteSub2(onReload) },
                                onSend = viewModel::onUpdateSub2User,
                                showQR = {
                                    viewModel.displayQRCode(
                                        InfoViewModel.UserType.SUB2,
                                        qrPixSize
                                    )
                                },
                                state = sub2CardState,
                                closeOtherCards = {
                                    parentCardState = parentCardState.close()
                                    sub1CardState = sub1CardState.close()
                                },
                                setOpenState = { openState ->
                                    sub2CardState = openState
                                },
                                setPrevUserData = { viewModel.onSetPrevData() },
                                cancelEdit = { viewModel.resetEditData() },
                                onChangeEmail = viewModel::onChangeEmailS2,
                                onChangePhoneNum = viewModel::onChangePhoneNumS2,
                                onChangeDayOne = viewModel::onChangeDayOneSelectedS2,
                                onChangeDayTwo = viewModel::onChangeDayTwoSelectedS2,
                                isDayOneVisited = sub2.dayOneVisited,
                                isDayTwoVisited = sub2.dayTwoVisited,
                                isDayOneSelected = sub2.dayOneSelected,
                                isDayTwoSelected = sub2.dayTwoSelected,
                                dayOneFull = state.dayOneFull,
                                dayTwoFull = state.dayTwoFull
                            )
                        }
                        AddSubUserCard(
                            onClickAddCompanion = onClickAddCompanion,
                            enabled = (state.user?.subUserIdList?.size ?: 0) < 2 && isEditable()
                        )
                        Spacer(Modifier.height(56.dp))
                    }
                    if (state.isUsersLoading) {
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
                    if (state.isShowQR) {
                        AlertDialog(
                            title = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "${state.displayUserName}様",
                                    textAlign = TextAlign.Center
                                )
                            },
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "入場時に係員にご提示ください")
                                    Text(
                                        text = "モバイル回線の不具合が懸念される方やスマホを" +
                                                "ご持参されない方にはこの画面のスクリーンショットや" +
                                                "印刷することをお勧めします。",
                                        style = MaterialTheme.typography.caption,
                                        color = Color.Gray
                                    )
                                    state.qrBitmap?.let {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            },
                            onDismissRequest = viewModel::closeQRCode,
                            buttons = {},
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KofunmanCard(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(horizontal = 40.dp),
                    message = "僕は古墳マン\n工大祭の案内をするよ!"
                )
                Spacer(modifier = Modifier.height(40.dp))
                if (!state.reserveAvailable) {
                    Text(
                        "10/30(日)から予約することが出来ます",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (!isEditable()) {
                    Text(
                        "予約は終了しました",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                CircularStartReserveButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(250.dp)
                        .height(45.dp),
                    enabled = state.reserveAvailable && isEditable(),
                    onClickReserve = {
                        onClickReserve()
                    }
                )
                Spacer(modifier = Modifier.height(60.dp))
                if (state.reserveAvailable) {
                    Text(
                        "すでに工大祭に予約されている場合", modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BorderLoginButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(250.dp)
                            .height(45.dp),
                        onClickLogin = {
                            onClickLogin()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
    DisposableEffect(viewModel) {
        onDispose { viewModel.removeListener() }
    }
}

@Composable
fun CircularStartReserveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickReserve: () -> Unit = {}
) {
    CircularButton(
        modifier = modifier,
        onClick = onClickReserve,
        enabled = enabled,
        text = "新規予約"
    )
}

@Composable
fun BorderLoginButton(
    modifier: Modifier = Modifier,
    onClickLogin: () -> Unit = {}
) {
    ColorBorderButton(
        modifier = modifier,
        onClick = onClickLogin,
        borderColor = Color.Gray,
        text = "ログイン",
        textStyle = MaterialTheme.typography.body1
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainUserCard(
    user: KoudaisaiUser,
    onChangeName: (String) -> Unit,
    onSend: () -> Unit,
    showQR: () -> Unit,
    state: OpenState,
    closeOtherCards: () -> Unit,
    setOpenState: (OpenState) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangePhoneNum: (String) -> Unit,
    onChangeDayOne: (Boolean) -> Unit,
    onChangeDayTwo: (Boolean) -> Unit,
    setPrevUserData: () -> Unit,
    cancelEdit: () -> Unit,
    isDayOneSelected: Boolean,
    isDayTwoSelected: Boolean,
    isDayOneVisited: Boolean,
    isDayTwoVisited: Boolean,
    dayOneFull: Boolean,
    dayTwoFull: Boolean
) {
    val isEdit = state == OpenState.EDIT
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        val kofunmanHeadSize = 200
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(id = R.drawable.kofunman_head_high),
                contentDescription = null,
                modifier = Modifier.size(kofunmanHeadSize.dp),
                contentScale = ContentScale.Fit
            )
        }
        UserCard(
            modifier = Modifier.padding(top = (kofunmanHeadSize - 20).dp),
            name = user.name,
            onChangeName = onChangeName,
            showQR = showQR,
            state = state,
            closeOtherCards = closeOtherCards,
            setOpenState = setOpenState,
            setPrevUserData = setPrevUserData,
            cancelEdit = cancelEdit,
            isDayOneSelected = isDayOneSelected,
            isDayTwoSelected = isDayTwoSelected,
            isDayOneVisited = isDayOneVisited,
            isDayTwoVisited = isDayTwoVisited,
            editItems = {
                Column() {
                    //TODO email の変更
                    if (!isEdit) SimpleTextEditField(
                        title = "email",
                        value = user.email,
                        keyboardType = KeyboardType.Email,
                        onValueChange = onChangeEmail,
                        enabled = false
                    )
                    SimpleTextEditField(
                        title = "電話番号",
                        value = user.phoneNumber,
                        keyboardType = KeyboardType.Phone,
                        onValueChange = onChangePhoneNum,
                        enabled = isEdit
                    )
                    Spacer(Modifier.height(15.dp))
                    if (isEdit) EditCheckTicketPossession(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        label = "入場日",
                        dayOneSelected = user.dayOneSelected,
                        dayTwoSelected = user.dayTwoSelected,
                        setDayOne = onChangeDayOne,
                        setDayTwo = onChangeDayTwo,
                        dayOneFull = dayOneFull,
                        dayTwoFull = dayTwoFull
                    ) else {
                        Text("入場日", modifier = Modifier.padding(start = 15.dp, bottom = 15.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (user.dayOneSelected) {
                                Text("　・11/19(土)")
                            }
                            if (user.dayTwoSelected) {
                                Text("　・11/20(日)")
                            }
                            if (!user.dayOneSelected && !user.dayTwoSelected) {
                                Text("　なし")
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                    }
                    if (isEdit) Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        val context = LocalContext.current
                        val keyboardController = LocalSoftwareKeyboardController.current
                        TextButton(
                            onClick = {
                                keyboardController?.hide()
                                if (haveNetworkAccess(context)) {
                                    onSend()
                                    setOpenState(OpenState.OPEN)
                                } else {
                                    SnackbarManager.showMessage("ネットワーク接続がありません")
                                }
                            }
                        ) {
                            Text("保存", color = MaterialTheme.colors.primary)
                        }
                    }
                }
            }
        )
        Row(
            modifier = Modifier.padding(top = (kofunmanHeadSize - 52).dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .height(50.dp)
                        .width(65.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, color = Color.Black),
                    color = Color(0xff8b714d)
                ) {}
            }
            Spacer(Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .height(50.dp)
                        .width(65.dp),
                    shape = CircleShape,
                    border = BorderStroke(2.dp, color = Color.Black),
                    color = Color(0xff8b714d)
                ) {}
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SubUserCard(
    user: KoudaisaiUser,
    onChangeName: (String) -> Unit,
    onSend: () -> Unit,
    onDelete: () -> Unit,
    showQR: () -> Unit,
    state: OpenState,
    closeOtherCards: () -> Unit,
    setOpenState: (OpenState) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangePhoneNum: (String) -> Unit,
    onChangeDayOne: (Boolean) -> Unit,
    onChangeDayTwo: (Boolean) -> Unit,
    setPrevUserData: () -> Unit,
    cancelEdit: () -> Unit,
    isDayOneSelected: Boolean,
    isDayTwoSelected: Boolean,
    isDayOneVisited: Boolean,
    isDayTwoVisited: Boolean,
    dayOneFull: Boolean,
    dayTwoFull: Boolean,
) {
    val isEdit = state == OpenState.EDIT
    UserCard(
        name = user.name,
        borderColor = Color(0xff25488A),
        onChangeName = onChangeName,
        showQR = showQR,
        state = state,
        closeOtherCards = closeOtherCards,
        setOpenState = setOpenState,
        setPrevUserData = setPrevUserData,
        cancelEdit = cancelEdit,
        isDayOneSelected = isDayOneSelected,
        isDayTwoSelected = isDayTwoSelected,
        isDayOneVisited = isDayOneVisited,
        isDayTwoVisited = isDayTwoVisited,
        editItems = {
            var showDialog by remember { mutableStateOf(false) }
            Column() {
                //TODO email の変更
                SimpleTextEditField(
                    title = "email",
                    value = user.email,
                    keyboardType = KeyboardType.Email,
                    onValueChange = onChangeEmail,
                    enabled = isEdit
                )
                SimpleTextEditField(
                    title = "電話番号",
                    value = user.phoneNumber,
                    keyboardType = KeyboardType.Phone,
                    onValueChange = onChangePhoneNum,
                    enabled = isEdit
                )
                Spacer(Modifier.height(15.dp))
                if (isEdit) EditCheckTicketPossession(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    label = "入場日",
                    dayOneSelected = user.dayOneSelected,
                    dayTwoSelected = user.dayTwoSelected,
                    setDayOne = onChangeDayOne,
                    setDayTwo = onChangeDayTwo,
                    dayOneFull = dayOneFull,
                    dayTwoFull = dayTwoFull
                ) else {
                    Text("入場日", modifier = Modifier.padding(start = 15.dp, bottom = 15.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (user.dayOneSelected) {
                            Text("　・11/19(土)")
                        }
                        if (user.dayTwoSelected) {
                            Text("　・11/20(日)")
                        }
                        if (!user.dayOneSelected && !user.dayTwoSelected) {
                            Text("　なし")
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                if (isEdit) Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val context = LocalContext.current
                    val keyboardController = LocalSoftwareKeyboardController.current
                    TextButton(
                        onClick = {
                            keyboardController?.hide()
                            if (haveNetworkAccess(context)) {
                                showDialog = true
                            } else {
                                SnackbarManager.showMessage("ネットワーク接続がありません")
                            }
                        }
                    ) {
                        Text("削除", color = MaterialTheme.colors.onBackground)
                    }
                    TextButton(
                        onClick = {
                            keyboardController?.hide()
                            if (haveNetworkAccess(context)) {
                                onSend()
                                setOpenState(OpenState.OPEN)
                            } else {
                                SnackbarManager.showMessage("ネットワーク接続がありません")
                            }
                        }
                    ) {
                        Text("保存", color = MaterialTheme.colors.primary)
                    }
                }
            }
            if (showDialog) {
                AlertDialog(
                    title = { Text("同伴者のキャンセル") },
                    text = { Text("予約情報を削除します．") },
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            onDelete()
                            showDialog = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                        }) {
                            Text("削除しない")
                        }
                    },
                )
            }
        }
    )
}

@Composable
private fun UserCard(
    modifier: Modifier = Modifier,
    name: String,
    isDayOneSelected: Boolean,
    isDayTwoSelected: Boolean,
    isDayOneVisited: Boolean,
    isDayTwoVisited: Boolean,
    borderColor: Color? = null,
    onChangeName: (String) -> Unit,
    showQR: () -> Unit,
    state: OpenState,
    closeOtherCards: () -> Unit,
    setOpenState: (OpenState) -> Unit,
    setPrevUserData: () -> Unit,
    cancelEdit: () -> Unit,
    editItems: @Composable () -> Unit,
) {
    val isClose = state == OpenState.CLOSE
    val isEdit = state == OpenState.EDIT
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(4.dp, borderColor ?: MaterialTheme.colors.primary),
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .padding(top = 20.dp)
            ) {
                if (isDayOneVisited || isDayTwoVisited) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (isDayOneVisited) Card(
                            shape = CircleShape,
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                        ) {
                            Text(
                                "1日目入場済",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.caption,
                                color = Color.White
                            )
                        }
                        if (isDayTwoVisited) Card(
                            shape = CircleShape,
                            backgroundColor = MaterialTheme.colors.primary,
                        ) {
                            Text(
                                "2日目入場済",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.caption,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (isClose) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            text = name + "様",
                            textAlign = TextAlign.Start
                        )
                    } else {
                        SimpleTextEditField(
                            title = "氏名",
                            modifier = Modifier.weight(1f),
                            value = name,
                            enabled = isEdit,
                            onValueChange = onChangeName,
                        )
                    }
                    IconButton(onClick = {
                        closeOtherCards()
                        cancelEdit()
                        setOpenState(state.toggleOpen())
                    }) {
                        Icon(
                            painterResource(
                                id = if (isClose) R.drawable.expand_more else
                                    R.drawable.expand_less
                            ),
                            modifier = Modifier.size(28.dp),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        closeOtherCards()
                        if (isClose || !isEditable()) {
                            showQR()
                        } else if (!isEdit) {
                            setOpenState(OpenState.EDIT)
                            setPrevUserData()
                        } else {
                            cancelEdit()
                            setOpenState(OpenState.OPEN)
                        }
                    }) {
                        Icon(
                            painterResource(
                                id = if (isClose || !isEditable()) R.drawable.qr_code_2 else R.drawable.edit
                            ),
                            tint = if (isEdit) Color.Gray else MaterialTheme.colors.onBackground,
                            modifier = Modifier.size(28.dp),
                            contentDescription = null
                        )
                    }
                }
            }
            if (isClose) Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (!isDayOneSelected && !isDayTwoSelected) {
                    Surface(modifier = Modifier.weight(1f), color = Color.Gray) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 12.dp),
                            text = "入場日なし",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (isDayOneSelected) {
                    Surface(
                        modifier = Modifier
                            .weight(1f),
                        color = MaterialTheme.colors.primaryVariant
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 12.dp),
                            text = "11/19(土)",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (isDayTwoSelected) {
                    Surface(
                        modifier = Modifier
                            .weight(1f),
                        color = MaterialTheme.colors.primary
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 12.dp),
                            text = "11/20(日)",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            if (!isClose) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .padding(bottom = 20.dp)
                ) {
                    editItems()
                }
            }
        }
    }
}

@Composable
private fun AddSubUserCard(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClickAddCompanion: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 3.dp, color = MaterialTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(text = "同行者を追加する", style = MaterialTheme.typography.h5)
            Text(
                text = "代表者様に同行する方の予約を\n" +
                        "２人まですることができます"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.kohunman_face_big),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(30.dp))
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.tamago_face),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
            CircularButton(
                enabled = enabled,
                text = "追加予約する",
                textStyle = MaterialTheme.typography.h6,
                textColor = Color.White
            ) {
                onClickAddCompanion()
            }
        }
    }
}

@Composable
fun SimpleTextEditField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    TextField(
        label = { Text(title) },
        modifier = modifier,
        value = value,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = Color.Gray,
            unfocusedIndicatorColor = Color.LightGray,
            backgroundColor = Color(0x00000000),
            disabledTextColor = MaterialTheme.colors.onBackground,
            disabledLabelColor = Color.Gray,
            disabledIndicatorColor = MaterialTheme.colors.surface
        )
    )
}

@Composable
fun EditCheckTicketPossession(
    modifier: Modifier = Modifier,
    label: String,
    dayOneSelected: Boolean,
    dayTwoSelected: Boolean,
    dayOneFull: Boolean,
    dayTwoFull: Boolean,
    setDayOne: (Boolean) -> Unit = {},
    setDayTwo: (Boolean) -> Unit = {},
    enable: Boolean = true,
    disableColor: Color? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
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
        EditSelectedDayCheckBox(
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
fun EditSelectedDayCheckBox(
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
            enabled = enable,
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
            enabled = enable,
            disabledColor = disableColor
        )
        if (dayTwoFull) Text(
            text = "20日(日)はすでに予約がいっぱいです",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error
        )
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

