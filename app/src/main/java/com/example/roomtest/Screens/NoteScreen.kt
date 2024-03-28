package com.example.roomtest.Screens

import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteScreen(
    viewModelNote: ViewModelNote,
    navHostController: NavHostController,
    noteId: String?
) {
    var title by remember {
        mutableStateOf("")
    }
    var subtitle by remember {
        mutableStateOf("")
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val coroutine = rememberCoroutineScope()
    val state = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val note = viewModelNote.getAllNotes().observeAsState().value
    val notes =
        note?.firstOrNull { it.id == noteId?.toInt() } ?: Note(title = "none", subtitle = "none")

    BottomSheetScaffold(
        scaffoldState = state,
        sheetGesturesEnabled = false,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ), textStyle = TextStyle(fontWeight = FontWeight.W500, fontSize = 19.sp)
                    )
                    IconButton(onClick = {
                        viewModelNote.update(
                            note = Note(
                                title = title,
                                subtitle = subtitle,
                                id = notes.id, createDate = notes.createDate
                            ), createDate = notes.createDate
                        ) {

                        }
                        coroutine.launch {
                            delay(50)
                            state.bottomSheetState.collapse()
                            keyboard?.hide()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "edit"
                        )
                    }
                }

                TextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ), textStyle = TextStyle(fontSize = 16.sp)
                )
            }

        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(viewModelNote.stateColor.value))
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = notes.title,
                    fontSize = viewModelNote.sizeText.value.sp, fontWeight = FontWeight.Bold
                )
                val textFilter = notes.title.count { !it.isWhitespace() }
                val texttFilter = notes.subtitle.count { !it.isWhitespace() }
                Text(text = "${textFilter + texttFilter} cимволов", Modifier.clickable {
                    coroutine.launch {
                        title = notes.title
                        subtitle = notes.subtitle
                        state.bottomSheetState.expand()
                    }
                })
            }

            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = notes.subtitle, fontSize = viewModelNote.sizeText.value.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrevewNote() {
    val context = LocalContext.current
    val dataStore = DataStorManager(context)
    val viewmodel: ViewModelNote =
        viewModel(factory = ViewModelNoteFactory(context.applicationContext as Application))
    val navController = rememberNavController()
    NoteScreen(
        navHostController = navController,
        viewModelNote = viewmodel, noteId = "none"
    )
}