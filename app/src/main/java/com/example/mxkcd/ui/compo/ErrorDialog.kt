package com.example.mxkcd.ui.compo

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ErrorDialog(message: Exception?) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Problem occurred")
            },
            text = {
                Text(message?.message.toString() + "")
            },
            confirmButton = {
                openDialog.value = false
            }
        )
    }
}