package com.example.mxkcd.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mxkcd.R
import com.example.mxkcd.base.Command
import com.example.mxkcd.dto.DtoItem
import com.example.mxkcd.ui.compo.theme.XkcdAndroidTheme
import com.example.mxkcd.ui.compo.theme.customTypeface
import com.example.mxkcd.ui.detail.ItemDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customTypeface = resources.getFont(R.font.pacfont)
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
    // HomeCompoScreen(all, controller)
    GameBorder(all, controller)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameBorder(all: Command<List<DtoItem>>, controller: NavHostController) {
    var gestOffsetX by remember { mutableStateOf(0f) }
    var gestOffsetY by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .border(6.dp, color = Color.Red)
            .padding(6.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val (x, y) = dragAmount
                    when {
                        x > 0 -> { /* right */
                            Log.e("xkcd", "right")
                        }
                        x < 0 -> { /* left */
                            Log.e("xkcd", "left")
                        }
                    }
                    when {
                        y > 0 -> {
                            Log.e("xkcd", "down")
                            /* down */
                        }
                        y < 0 -> {
                            Log.e("xkcd", "up")
                            /* up */
                        }
                    }
                    gestOffsetX += dragAmount.x
                    gestOffsetY += dragAmount.y
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            val height = this.size.height
            val width = this.size.width
            Log.d("canvas", "width: $width, height: $height ")

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "c",
                    size.width / 2,
                    size.height / 2,
                    Paint().apply {
                        textSize = 100f
                        color = Color.Yellow.toArgb()
                        textAlign = Paint.Align.CENTER
                        typeface = customTypeface
                    }
                )
            }

            val borderPath = Path()
            borderPath.apply {
                // border
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                lineTo(0f, 0f)

                // second border
                moveTo(50f, 50f)
                lineTo(size.width - 50f, 50f)
                lineTo(size.width - 50f, size.height - 50f)
                lineTo(50f, size.height - 50f)
                lineTo(50f, 50f)
            }

            drawPath(
                path = borderPath,
                color = Color.Black,
                style = Stroke(width = 6.dp.toPx())
            )
        }
    }
}


object Nav {
    const val HOME = "home"

    object Detail {
        const val ID = "id"
        const val DETAILS_PATH = "/{id}"
    }
}
