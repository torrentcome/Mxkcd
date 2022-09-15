package com.example.mxkcd.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.decathlon.vitamin.compose.buttons.VitaminButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.example.mxkcd.base.DataState
import com.example.mxkcd.dto.XkcdItem
import com.example.mxkcd.ui.detail.ItemDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VitaminTheme {
                XkcdApp()
            }
        }
    }
}

@ExperimentalMaterialApi
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
            Log.d("debug", "id=$id")
            id?.let { it1 -> ItemDetailScreen(it1) }
        }
    }
}

@Composable
fun HomeScreen(controller: NavHostController) {
    val homeDetailViewModel = hiltViewModel<HomeViewModel>()
    val all = homeDetailViewModel.all

    LaunchedEffect(true) {
        Log.d("debug", "pass here")
        homeDetailViewModel.getAll()
    }
    all.value.let {
        if (it is DataState.Success<List<XkcdItem>>) {
            val data: ArrayList<XkcdItem> = it.data as ArrayList<XkcdItem>
            LazyColumn {
                items(data) { item ->
                    VitaminButtons.Secondary(text = "" + item.num) {
                        controller.navigate(Nav.HOME.plus("/${item.num}"))
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Text("Welcome !")
            VitaminButtons.Primary(text = "Get a story") {
                val random = (1..1000).random()
                Log.d("debug", "random=$random")
                controller.navigate(Nav.HOME.plus("/$random"))
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
