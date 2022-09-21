package com.example.mxkcd.ui.base

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
) {
    CircularProgressIndicator(
        modifier = modifier,
        strokeWidth = strokeWidth
    )
}