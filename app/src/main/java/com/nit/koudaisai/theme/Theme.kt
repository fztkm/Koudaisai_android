package com.nit.koudaisai.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.nit.koudaisai.R


private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = BlueAccent,
    secondary = Color.Black,
    onSecondary = OrangeThin
)

private val LightColorPalette = lightColors(
    primary = Orange,
    primaryVariant = BlueAccent,
    secondary = YellowThin,
    secondaryVariant = Gray,
    surface = Color.White,
    onSurface = Gray,
    onSecondary = Yellow,
    onBackground = Color.Black,
    error = SoftRed
)

val NotoSansJP = FontFamily(
    Font(R.font.noto_sans_jp_regular),
    Font(R.font.noto_sans_jp_medium)
)

@OptIn(ExperimentalTextApi::class)
val KoudaisaiTypography = Typography(
    defaultFontFamily = NotoSansJP,
    body1 = Typography.body1.copy(
        fontWeight = FontWeight.Bold,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    body2 = Typography.body2.copy(
        fontWeight = FontWeight.Bold,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h1 = Typography.h1.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h2 = Typography.h2.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h3 = Typography.h3.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h4 = Typography.h4.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h5 = Typography.h5.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    h6 = Typography.h6.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    subtitle1 = Typography.subtitle1.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    subtitle2 = Typography.subtitle2.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    button = Typography.button.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    caption = Typography.caption.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    overline = Typography.overline.copy(
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
)


@Composable
fun KoudaisaiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = KoudaisaiTypography,
        shapes = Shapes,
        content = content
    )
}