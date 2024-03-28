package com.example.roomtest.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roomtest.R
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.navigatoin.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(
    navController: NavController,
    viewModelNote: ViewModelNote
) {
    val state = remember { mutableStateOf(true) }
    val stateLoad = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.value) {
            LaunchedEffect(key1 = state) {
                delay(1000)
                if (viewModelNote.password.value == "1") {
                    navController.navigate(NavRoute.StartScreen.route)
                } else navController.navigate(NavRoute.PassworScreen.route)
            }
        }
        Image(
            painter = painterResource(id = R.drawable.black),
            contentDescription = "black",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        CircularProgressIndicator(
            color = Color.Gray, modifier = Modifier.size(40.dp)
        )
        return@Column
    }
}