package com.example.mxkcd.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun XkcdTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = when {
        darkTheme -> XDarkColorPalette
        else -> XLightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = XTypography,
        shapes = XShapes,
        content = content
    )
}

private val XDarkColorPalette = darkColors(
    primary = Color(0xFF96A8C8),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF6E7B91),
    background = Color(0xFF121212)
)

private val XLightColorPalette = lightColors(
    primary = Color(0xFF6200EE),//Purple200
    primaryVariant = Color(0xFF3700B3),//Purple500
    secondary = Color(0xFF03DAC5),//Purple700
    background = Color(0xFF121212)//Black200
)

val XShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val XTypography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)