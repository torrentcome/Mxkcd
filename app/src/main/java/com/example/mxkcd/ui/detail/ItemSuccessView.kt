package com.example.mxkcd.ui.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
internal fun ItemSuccessView(
    itemDetailViewModel: ItemDetailViewModel,
    item: Command.Success<DtoItem>
) {
    itemDetailViewModel.saveItemDetail(item.data)
    Text(item.data.title)
    Text(item.data.safe_title)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Text(text = item.data.year)
    }
    CoilImage(
        imageModel = item.data.img,
        imageOptions = ImageOptions(
            contentScale = ContentScale.None,
            alignment = Alignment.Center
        )
    )
}