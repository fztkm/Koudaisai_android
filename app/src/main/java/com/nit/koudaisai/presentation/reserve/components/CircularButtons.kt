package com.nit.koudaisai.presentation.reserve.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SendingButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary
        ),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        Text(text = text, color = MaterialTheme.colors.surface)
    }
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Gray
        ),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        Text(text = text, color = MaterialTheme.colors.surface)
    }
}


@Composable
fun ColorBorderButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    borderColor: Color = MaterialTheme.colors.primary,
    text: String = "",
    textStyle: TextStyle = MaterialTheme.typography.h5,
) {
    TextButton(
        modifier = modifier,
        border = BorderStroke(
            width = 2.dp,
            color = borderColor,
        ),
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 0.dp),
            text = text,
            color = MaterialTheme.colors.primaryVariant,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = true,
    color: Color = MaterialTheme.colors.primary,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    textColor: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        border = if(enabled) BorderStroke(
            width = 2.dp,
            color = color,
        ) else null,
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            disabledBackgroundColor = Color.LightGray,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 0.dp),
            text = text,
            color = if(enabled) textColor else Color.White,
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}


