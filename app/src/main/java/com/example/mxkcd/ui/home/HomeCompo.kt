package com.example.mxkcd.ui.home

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.ext.noMas
import com.example.mxkcd.ext.nonNegatif
import com.example.mxkcd.ui.compo.ErrorDialog
import com.example.mxkcd.ui.compo.ProgressIndicator
import com.example.mxkcd.ui.detail.progressScreenModifier
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch


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

@Composable
fun HomeCompoScreen(
    all: Command<List<DtoItem>>,
    controller: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            all.let {
                when (it) {
                    is Command.Success<List<DtoItem>> -> {
                        Text("Welcome !")
                        OutlinedButton(onClick = {
                            val random = (1..1000).random()
                            Log.d("debug", "random=$random")
                            controller.navigate(Nav.HOME.plus("/$random"))
                        }) {
                            Text("Get a story")
                        }

                        val data: ArrayList<DtoItem> = it.data as ArrayList<DtoItem>
                        val listState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()
                        val requester = FocusRequester()
                        var scale by remember { mutableStateOf(1f) }
                        LazyRow(state = listState,
                            modifier = Modifier.onKeyEvent { keyevent ->
                                val action = keyevent.nativeKeyEvent.action
                                val keyCode = keyevent.nativeKeyEvent.keyCode
                                Log.e("Home", "action =$action")
                                Log.e("Home", "keyCode =$keyCode")
                                when (keyCode) {
                                    KeyEvent.KEYCODE_DPAD_LEFT -> coroutineScope.launch {
                                        listState.scrollToItem(
                                            index = nonNegatif(listState.firstVisibleItemIndex - 1)
                                        )
                                    }
                                    KeyEvent.KEYCODE_DPAD_RIGHT -> coroutineScope.launch {
                                        listState.scrollToItem(
                                            index = noMas(
                                                listState.firstVisibleItemIndex + 1,
                                                data.size
                                            )
                                        )
                                    }
                                }
                                return@onKeyEvent false
                            }) {
                            items(data.size) { index ->
                                HomeCard(index, requester, scale, controller, data)
                            }
                        }
                    }
                    is Command.Error -> {
                        ErrorDialog(it.exception)
                    }
                    is Command.Loading -> {
                        ProgressIndicator(
                            modifier = Modifier.progressScreenModifier(),
                            string = it.reason
                        )
                    }
                }
            }
        }
    )
}