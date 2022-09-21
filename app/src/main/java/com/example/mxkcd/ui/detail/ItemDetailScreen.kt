package com.example.mxkcd.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.ui.base.ProgressIndicator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@ExperimentalMaterialApi
@Composable
fun ItemDetailScreen(id: Int) {
    val itemDetailViewModel = hiltViewModel<ItemDetailViewModel>()
    val item = itemDetailViewModel.item

    LaunchedEffect(true) {
        itemDetailViewModel.itemDetail(id)
    }

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item.value.let {
            if (it is Command.Success<XkcdItem>) {
                itemDetailViewModel.saveItemDetail(it.data)
                Text(it.data.title)
                Text(it.data.safe_title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(text = it.data.year)
                }
                CoilImage(
                    imageModel = it.data.img, // loading a network image or local resource using an URL.
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.None,
                        alignment = Alignment.Center
                    )
                )
            } else {
                ProgressIndicator(
                    modifier = Modifier.progressScreenModifier()
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
