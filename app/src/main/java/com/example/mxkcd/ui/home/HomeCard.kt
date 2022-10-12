package com.example.mxkcd.ui.home

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mxkcd.dto.DtoItem
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage


@Composable
internal fun HomeCard(
    index: Int,
    requester: FocusRequester,
    scale: Float,
    controller: NavHostController,
    data: ArrayList<DtoItem>
) {
    var scale1 = scale
    Card(
        modifier = Modifier
            .width(172.dp)
            .height(256.dp)
            .padding(
                start = if (index == 0) 16.dp else 16.dp,
                top = 16.dp, bottom = 16.dp,
                end = if (index == 4) 16.dp else 8.dp
            )
            .focusRequester(focusRequester = requester)
            .onFocusChanged { focusable ->
                scale1 = if (focusable.isFocused) 3f else 1f
            }
            .focusable()
            .graphicsLayer(
                scaleX = scale1,
                scaleY = scale1
            ),
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(8),

        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(
                onClick = {
                    controller.navigate(Nav.HOME.plus("/${data[index].num}"))
                },
            ) {
                Text(text = "" + data[index].num)
            }
            CoilImage(
                imageModel = data[index].img,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
        }
    }
}
