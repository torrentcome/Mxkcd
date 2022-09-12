package com.example.mxkcd.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mxkcd.base.DataState
import com.example.mxkcd.dto.XkcdItem

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
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        CircularIndeterminateProgressBar()
        item.value.let { it as DataState.Success<XkcdItem> }.apply {
            ListItem(text = {
                Text(
                    text = this.data.title,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, secondaryText = {
                Text(
                    text = "Month: ${this.data.month}",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, fontSize = 15.sp,
                        fontWeight = FontWeight.Light, color = Color.DarkGray
                    )
                )
            }, icon = {
                println(this.data.img)
            })
        }
    }
}

@Composable
fun CircularIndeterminateProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}
