package com.nit.koudaisai.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toIntPixel(): Int {
    val dp = this
    return with(LocalDensity.current) { dp.toPx() }.toInt()
}