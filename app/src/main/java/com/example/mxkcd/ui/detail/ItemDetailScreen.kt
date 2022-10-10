package com.example.mxkcd.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.ui.compo.ErrorDialog
import com.example.mxkcd.ui.compo.ProgressIndicator

@Composable
fun ItemDetailScreen(id: Int) {
    val itemDetailViewModel = hiltViewModel<ItemDetailViewModel>()
    val item = itemDetailViewModel.item.collectAsState().value

    LaunchedEffect(true) {
        itemDetailViewModel.itemDetail(id)
    }

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        when (item) {
            is Command.Success<DtoItem> -> {
                ItemSuccessView(itemDetailViewModel, item)
            }
            is Command.Error -> {
                ErrorDialog(item.exception)
            }
            is Command.Loading -> {
                ProgressIndicator(
                    modifier = Modifier.progressScreenModifier(),
                    string = item.reason
                )
            }
        }
    }
}


internal fun Modifier.progressScreenModifier(
    firstItem: Boolean = false,
    lastItem: Boolean = false
): Modifier {
    val top = if (firstItem) 15.dp else 10.dp
    val bottom = if (lastItem) 15.dp else 10.dp
    return this
        .wrapContentSize()
        .padding(top = top, start = 15.dp, end = 15.dp, bottom = bottom)
}
