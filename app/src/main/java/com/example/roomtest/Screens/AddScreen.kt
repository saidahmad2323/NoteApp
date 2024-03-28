package com.example.roomtest.Screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.Note
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.model.ViewModelNoteFactory
import com.example.roomtest.navigatoin.NavRoute
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddScreen(
    viewModelNote: ViewModelNote,
    navHostController: NavHostController,
    dataStorManager: DataStorManager
) {
    val coroutine = rememberCoroutineScope()
    var title by remember {
        mutableStateOf("")
    }
    var subtitle by remember {
        mutableStateOf("")
    }
    Scaffold(
        floatingActionButton = {
            if (title.isNotEmpty() && subtitle.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    viewModelNote.addNote(
                        title = title,
                        subtitle = subtitle
                    ) {
                        coroutine.launch {
                            navHostController.navigate(NavRoute.StartScreen.route)
                        }


                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add"
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ), placeholder = {
                        Text(text = "Заголовок", fontSize = 17.sp)
                    }, textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.W500)
                )
                val stateFilter = title.count { !it.isWhitespace() }
                val stateFilterr = subtitle.count { !it.isWhitespace() }
                Text(text = "${stateFilter + stateFilterr} символов")
            }


            TextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ), placeholder = {
                    Text(text = "Подзаголовок", fontSize = 17.sp)
                }, textStyle = TextStyle(fontSize = 17.sp)
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PrevewAddScreen() {
    val context = LocalContext.current
    val dataStore = DataStorManager(context)
    val viewmodel: ViewModelNote =
        viewModel(factory = ViewModelNoteFactory(context.applicationContext as Application))
    val navController = rememberNavController()
    AddScreen(
        navHostController = navController,
        viewModelNote = viewmodel, dataStorManager = dataStore
    )
}