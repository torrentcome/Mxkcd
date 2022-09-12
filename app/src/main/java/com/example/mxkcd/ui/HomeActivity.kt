package com.example.mxkcd.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mxkcd.ui.detail.ItemDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            XkcdTheme {
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
            HomeScreen(navController = controller,)
        }
        composable(
            route = Nav.HOME.plus(Nav.Detail.DETAILS_PATH),
            arguments = listOf(navArgument(Nav.Detail.ID) {
                type = NavType.IntType
            })
        ) { ItemDetailScreen(1) }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {

}


object Nav {
    const val HOME = "home"

    object Detail {
        const val ID = "id"
        const val DETAILS_PATH = "/{id}"
    }
}