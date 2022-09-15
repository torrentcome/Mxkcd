package com.example.mxkcd.ui.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.decathlon.vitamin.compose.prices.VitaminPriceSizes
import com.decathlon.vitamin.compose.prices.VitaminPrices
import com.decathlon.vitamin.compose.progressbars.VitaminCircularProgressBarSizes
import com.decathlon.vitamin.compose.progressbars.VitaminProgressBars
import com.example.mxkcd.R
import com.example.mxkcd.base.DataState
import com.example.mxkcd.dto.XkcdItem

@OptIn(ExperimentalCoilApi::class)
@ExperimentalMaterialApi
@Composable
fun ItemDetailScreen(id: Int) {
    val itemDetailViewModel = hiltViewModel<ItemDetailViewModel>()
    val item = itemDetailViewModel.item

    LaunchedEffect(true) {
        Log.d("debug", "pass here")
        itemDetailViewModel.itemDetail(id)
    }

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item.value.let {
            if (it is DataState.Success<XkcdItem>) {
                itemDetailViewModel.saveItemDetail(it.data)
                Text(it.data.title)
                Text(it.data.safe_title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    VitaminPrices.Accent(
                        text = it.data.year,
                        sizes = VitaminPriceSizes.small()
                    )
                }
                val painter = rememberImagePainter(data = it.data.img)
                Image(
                    painter = painter,
                    contentDescription = it.data.transcript,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            } else {
                VitaminProgressBars.Circular(
                    modifier = Modifier.progressScreenModifier(),
                    sizes = VitaminCircularProgressBarSizes.small(),
                    content = {
                        ImageCircular(
                            painter = painterResource(id = R.drawable.xkcd),
                            contentDescription = "Waiting..."
                        )
                    }
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
