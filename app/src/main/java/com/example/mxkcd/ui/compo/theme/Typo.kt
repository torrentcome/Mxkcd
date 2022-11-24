package com.example.mxkcd.ui.compo.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.mxkcd.R

val fonts = FontFamily(
    Font(R.font.pacfont, weight = FontWeight.Normal),
    Font(R.font.pacfont, weight = FontWeight.Medium),
    Font(R.font.pacfont, weight = FontWeight.Bold),
    Font(R.font.pacfont, weight = FontWeight.Black)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal
    ),

    h4 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium
    ),

    h5 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold
    ),

    h6 = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Black
    )
)

public lateinit var customTypeface: android.graphics.Typeface
