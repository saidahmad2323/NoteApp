package com.example.roomtest.Screens

import android.app.Activity
import android.opengl.Visibility
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.roomtest.DrawerMenu.DrawerMenu
import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.Note
import com.example.roomtest.model.SettingsData
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.navigatoin.NavRoute

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.KeyManagementException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun StartScreen(
    viewModelNote: ViewModelNote, navHostController: NavHostController,
    dataStorManager: DataStorManager
) {
    val stateAlert = remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler(
        onBack = {
            stateAlert.value = true
        }
    )
    val scaffold = rememberScaffoldState()
    var searchstate by remember {
        mutableStateOf(false)
    }
    val stateDrawer = remember {
        mutableStateOf(false)
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var search by remember { mutableStateOf("") }
    val notes = viewModelNote.readNote.observeAsState(listOf()).value
    val view = viewModelNote.getAllNotes().observeAsState(listOf()).value
    val sortNotes = notes.sortedByDescending { it.id }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(NavRoute.AddScreen.route)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
        },
        scaffoldState = scaffold,
        topBar = {
            TopAppBar(
                title = {
                    if (searchstate) {

                        TextField(
                            value = search,
                            onValueChange = { newText ->
                                search = newText.capitalize()
                            }, colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Gray
                            ), placeholder = {
                                Text(text = "Поиск...")
                            }, textStyle = TextStyle(fontSize = 17.sp)
                        )
                    } else
                        if (notes.isEmpty()) {
                            Text(text = "Заметки")
                        } else Text(text = "Количество заметок (${notes.count()})")
                },
                actions = {
                    if (searchstate) {
                        IconButton(onClick = {
                            searchstate = false
                            search = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close"
                            )
                        }
                    } else IconButton(onClick = {
                        searchstate = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search"
                        )
                    }
                }
            )
        }, drawerContent = {
            DrawerMenu(viewModelNote, dataStorManager, navHostController,
                onClick = {
                    coroutineScope.launch {
                        delay(50)
                        scaffold.drawerState.close()
                    }
                }
            )
        }
    ) {
        if (stateAlert.value) {
            ShowDialog2 {
                stateAlert.value = false
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            val filtred = sortNotes.filter {
                it.title.contains(search, ignoreCase = true)
                        || it.subtitle.contains(search, ignoreCase = true)
                        || it.createDate.contains(search, ignoreCase = true)
            }
            LazyColumn(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 0.dp)
            ) {
                items(filtred) { note ->
                    NoteItem(title = note.title, subtitlr = note.subtitle,
                        onClick = { navHostController.navigate(NavRoute.NoteScreen.route + "/${note.id}") },
                        onClickk = {
                            viewModelNote.delete(
                                Note(note.title, note.subtitle, id = note.id)
                            ) {
                            }
                        }, createDate = note.createDate, viewModelNote = viewModelNote
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    title: String,
    subtitlr: String,
    createDate: String,
    onClick: () -> Unit,
    onClickk: () -> Unit,
    viewModelNote: ViewModelNote
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 4.dp, start = 4.dp, top = 6.dp),
        shape = RoundedCornerShape(viewModelNote.shapes.value.toInt().dp),
        backgroundColor = Color(viewModelNote.stateColor.value),
        border = BorderStroke(1.dp, color = Color.Black)
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .clickable { onClick() }
                .padding(12.dp)
        ) {
            Text(
                text = title,
                fontSize = viewModelNote.sizeText.value.sp, fontWeight = FontWeight.W500
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = if (subtitlr.length > 60) subtitlr.take(60) + "..." else subtitlr,
                    fontSize = viewModelNote.sizeText.value.sp
                )

                Text("Создан: $createDate")
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = {
                onClickk()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete"
                )
            }
        }
    }
}

@Composable
fun ShowDialog2(
    onDismis: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color.Gray,
        onDismissRequest = { },
        title = {
            Text(
                text = "Закрыть приложение?",
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )
        },
        confirmButton = {

            Button(modifier = Modifier.width(147.dp),
                onClick = {
                    onDismis()
                }) {

                Text(text = "Нет")

            }

        },
        dismissButton = {

            Button(modifier = Modifier.width(149.dp),
                onClick = {
                    (context as? ComponentActivity)?.finish()
                }

            ) {
                Text(text = "Да")


            }

        }

    )
}