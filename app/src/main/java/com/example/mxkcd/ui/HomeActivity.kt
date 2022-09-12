package com.example.mxkcd.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XkcdTheme(true) {
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
            HomeScreen()
        }
//        composable(
//            route = Nav.HOME.plus(Nav.Detail.DETAILS_PATH),
//            arguments = listOf(navArgument(Nav.Detail.ID) {
//                type = NavType.IntType
//            })
//        ) { ItemDetailScreen(1) }
    }
}

@Composable
fun HomeScreen() {
    val focusRequester = FocusRequester()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Text("Welcome !")
            Button(
                modifier = Modifier.padding(16.dp).focusRequester(focusRequester),
                elevation = ButtonDefaults.elevation(5.dp),
                onClick = {}) {
                Text(text = "Simple button", modifier = Modifier.padding(16.dp))
            }
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

object Nav {
    const val HOME = "home"

    object Detail {
        const val ID = "id"
        const val DETAILS_PATH = "/{id}"
    }
}