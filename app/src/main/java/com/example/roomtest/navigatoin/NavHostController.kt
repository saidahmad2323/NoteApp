package com.example.roomtest.navigatoin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roomtest.Screens.*
import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.ViewModelNote

sealed class NavRoute(val route: String) {
    object StartScreen : NavRoute("start")
    object NoteScreen : NavRoute("note")
    object AddScreen : NavRoute("add")
    object SettingScreen : NavRoute("setting")
    object PassworScreen : NavRoute("password")
    object SplashScreen : NavRoute("splash")
    object ConfigScreen : NavRoute("config")
}


@Composable
fun NavHostControler(
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = NavRoute.SplashScreen.route
    ) {
        composable(NavRoute.StartScreen.route) {
            StartScreen(viewModelNote, navHostController, dataStorManager)
        }
        composable(NavRoute.AddScreen.route) {
            AddScreen(viewModelNote, navHostController, dataStorManager)
        }
        composable(NavRoute.NoteScreen.route + "/{${"id"}}") { backStack ->
            NoteScreen(
                viewModelNote, navHostController,
                noteId = backStack.arguments?.getString("id")
            )
        }
        composable(NavRoute.SettingScreen.route) {
            SettingScreen(viewModelNote, dataStorManager, navHostController)
        }
        composable(NavRoute.PassworScreen.route) {
            PasswordScreen(navController = navHostController, viewModelNote, dataStorManager)
        }
        composable(NavRoute.SplashScreen.route) {
            SplashScreen(navController = navHostController, viewModelNote)
        }
        composable(NavRoute.ConfigScreen.route) {
            ConfigScreen(viewModelNote, dataStorManager, navHostController)
        }
    }
}
