package com.example.mxkcd.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.ui.detail.ItemDetailScreen
import com.example.mxkcd.ui.lib.MComposePagerSnapHelper
import com.example.mxkcd.ui.theme.XkcdAndroidTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XkcdAndroidTheme {
                XkcdApp()
            }
        }
    }
}

@Composable
private fun XkcdApp() {
    val controller = rememberNavController()
    NavHost(controller, startDestination = Nav.HOME) {
        composable(Nav.HOME) {
            HomeScreen(controller)
        }
        composable(
            route = Nav.HOME.plus(Nav.Detail.DETAILS_PATH),
            arguments = listOf(navArgument(Nav.Detail.ID) {
                type = NavType.IntType; defaultValue = 1
            })
        ) {
            val id = it.arguments?.getInt(Nav.Detail.ID)
            id?.let { it1 -> ItemDetailScreen(it1) }
        }
    }
}

@Composable
fun HomeScreen(controller: NavHostController) {
    val homeDetailViewModel = hiltViewModel<HomeViewModel>()
    val all: MutableState<Command<List<XkcdItem>>?> = homeDetailViewModel.all

    LaunchedEffect(true) {
        homeDetailViewModel.getAll()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Text("Welcome !")
            OutlinedButton(onClick = {
                val random = (1..1000).random()
                Log.d("debug", "random=$random")
                controller.navigate(Nav.HOME.plus("/$random"))
            }) {
                Text("Get a story")
            }
            all.value?.let {
                if (it is Command.Success<List<XkcdItem>>) {
                    if (it.data.isNotEmpty()) {
                        val data: ArrayList<XkcdItem> = it.data as ArrayList<XkcdItem>

                        val listState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()

                        MComposePagerSnapHelper(width = 72.dp) { listState ->
                            LazyRow(state = listState) {
                                items(count = data.size) { index ->
                                    Card(
                                        modifier = Modifier
                                            .width(64.dp)
                                            .height(128.dp)
                                            .padding(
                                                start = if (index == 0) 16.dp else 16.dp,
                                                top = 16.dp, bottom = 16.dp,
                                                end = if (index == 4) 16.dp else 8.dp
                                            ),
                                        backgroundColor = Color.LightGray,
                                        shape = RoundedCornerShape(12)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .wrapContentHeight(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CoilImage(
                                                imageModel = data[index].img,
                                                modifier = Modifier
                                                    .width(64.dp)
                                                    .height(128.dp),
                                                imageOptions = ImageOptions(
                                                    contentScale = ContentScale.Inside,
                                                    alignment = Alignment.Center
                                                )
                                            )
                                            OutlinedButton(
                                                onClick = {
                                                    controller.navigate(Nav.HOME.plus("/${data[index].num}"))
                                                },
                                            ) {
                                                Text(text = "" + data[index].num)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        LazyRow(state = listState,
                            modifier = Modifier.onKeyEvent { keyevent ->
                                val action = keyevent.nativeKeyEvent.action
                                val keyCode = keyevent.nativeKeyEvent.keyCode
                                Log.e("Home", "action =$action")
                                Log.e("Home", "keyCode =$keyCode")
                                if (keyCode == KEYCODE_DPAD_LEFT) {
                                    coroutineScope.launch {
                                        // Animate scroll to the 10th item
                                        listState.scrollToItem(index = nonNegatif(listState.firstVisibleItemIndex - 1))
                                    }
                                }
                                if (keyCode == KEYCODE_DPAD_RIGHT) {
                                    coroutineScope.launch {
                                        // Animate scroll to the 10th item
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
                                Column(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .wrapContentHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CoilImage(
                                        imageModel = data[index].img,
                                        modifier = Modifier
                                            .width(64.dp)
                                            .height(128.dp),
                                        imageOptions = ImageOptions(
                                            contentScale = ContentScale.Inside,
                                            alignment = Alignment.Center
                                        )
                                    )
                                    OutlinedButton(
                                        onClick = {
                                            controller.navigate(Nav.HOME.plus("/${data[index].num}"))
                                        },
                                    ) {
                                        Text(text = "" + data[index].num)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

object Nav {
    const val HOME = "home"

    object Detail {
        const val ID = "id"
        const val DETAILS_PATH = "/{id}"
    }
}

fun nonNegatif(e: Int): Int {
    return if (e <= 0) 0 else e
}

fun noMas(e: Int, max: Int): Int {
    return if (e >= max) nonNegatif(max) else nonNegatif(e)
}