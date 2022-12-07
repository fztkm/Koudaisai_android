package com.nit.koudaisai.presentation.reserve.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nit.koudaisai.R

@Composable
fun InputUserDataField(
    modifier: Modifier = Modifier,
    label: String,
    text: String = "",
    mandatory: Boolean,
    enable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onChangeText: (newText: String) -> Unit = {}
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                if (enable) MaterialTheme.colors.onBackground
                else Color.Gray
            )
        ) {
            append(label)
        }
        if (mandatory) {
            withStyle(style = SpanStyle(MaterialTheme.colors.error)) {
                append("(必須)")
            }
        }
    }
    InputField(
        modifier = modifier,
        label = annotatedLabel,
        text = text,
        keyboardType = keyboardType,
        imeAction = imeAction,
        onChangeText = onChangeText, enable = enable
    )
}

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String = "",
    mandatory: Boolean = false,
    enable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onChangeText: (newText: String) -> Unit = {}
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                if (enable) MaterialTheme.colors.onBackground
                else Color.Gray
            )
        ) {
            append(label)
        }
        if (mandatory) {
            withStyle(style = SpanStyle(MaterialTheme.colors.error)) {
                append("(必須)")
            }
        }
    }
    InputField(
        modifier = modifier, label = annotatedLabel,
        text = text,
        keyboardType = keyboardType,
        imeAction = imeAction,
        onChangeText = onChangeText, enable = enable
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: AnnotatedString,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    enable: Boolean = true,
    onChangeText: (newText: String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onChangeText,
        label = {
            Text(
                text = label,
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        enabled = enable
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(
    label: String,
    password: String,
    mandatory: Boolean = false,
    onChangeText: (newText: String) -> Unit,
    showHint: Boolean = true,
    imeAction: ImeAction = ImeAction.Done
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(style = SpanStyle(MaterialTheme.colors.onBackground)) {
            append(label)
        }
        if (mandatory) {
            withStyle(style = SpanStyle(MaterialTheme.colors.error)) {
                append("(必須)")
            }
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = password,
            onValueChange = onChangeText,
            label = { Text(annotatedLabel) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.visibility_fill
                else R.drawable.visibility_off

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painterResource(id = image), description, modifier = Modifier.size(28.dp))
                }
            }
        )
    }
    if (showHint) Text(
        "パスワードは6文字以上で数字および英文字で構成される必要があります",
        color = Color.Gray,
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .padding(horizontal = 45.dp)
            .fillMaxWidth()
    )
}

