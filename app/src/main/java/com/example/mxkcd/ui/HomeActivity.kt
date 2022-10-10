package com.example.mxkcd.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.ext.noMas
import com.example.mxkcd.ext.nonNegatif
import com.example.mxkcd.ui.compo.ErrorDialog
import com.example.mxkcd.ui.compo.ProgressIndicator
import com.example.mxkcd.ui.detail.ItemDetailScreen
import com.example.mxkcd.ui.detail.progressScreenModifier
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
    val all: Command<List<DtoItem>> = homeDetailViewModel.all.collectAsState().value

    LaunchedEffect(true) {
        homeDetailViewModel.getAll()
    }
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
                                    KEYCODE_DPAD_LEFT -> coroutineScope.launch {
                                        listState.scrollToItem(
                                            index = nonNegatif(listState.firstVisibleItemIndex - 1)
                                        )
                                    }
                                    KEYCODE_DPAD_RIGHT -> coroutineScope.launch {
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
                                            scale = if (focusable.isFocused) 3f else 1f
                                        }
                                        .focusable()
                                        .graphicsLayer(
                                            scaleX = scale,
                                            scaleY = scale
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
                        }
                    }
                    is Command.Error -> {
                        ErrorDialog(it.exception)
                    }
                    is Command.Loading -> {
                        ProgressIndicator(
                            modifier = Modifier.progressScreenModifier()
                        )
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
