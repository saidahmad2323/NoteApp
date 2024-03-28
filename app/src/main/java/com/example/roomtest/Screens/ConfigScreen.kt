package com.example.roomtest.Screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.Password
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.model.ViewModelNoteFactory
import com.example.roomtest.navigatoin.NavRoute
import kotlinx.coroutines.launch

@Composable
fun ConfigScreen(
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager,
    navController: NavController
) {
    val alertDelete = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val alertPassword = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), contentAlignment = Alignment.Center
        ) {
            if (viewModelNote.password.value == "1") {
                Button(onClick = {
                    navController.navigate(NavRoute.PassworScreen.route)
                }) {
                    Text(text = "Создать Пароль")
                }
            } else Button(onClick = {
                alertPassword.value = true
            }) {
                Text(text = "Изменить Пароль")
            }
        }
        Button(onClick = {
            alertDelete.value = true
        }, enabled = viewModelNote.password.value != "1") {
            Text(text = "Удалить пароль")
        }
        if (alertDelete.value) {
            val name = remember { mutableStateOf("") }
            AlertDialog(onDismissRequest = { alertDelete.value = false },
                title = {
                    Text(
                        text = "Удалить Пароль", fontWeight = FontWeight.W500, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it },
                            placeholder = {
                                Text(text = "Введите активный пароль")
                            }
                        )
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            if (name.value == viewModelNote.password.value) {
                                viewModelNote.password.value = "1"
                                coroutine.launch {
                                    dataStorManager.savePassword(
                                        Password(
                                            viewModelNote.password.value
                                        )
                                    )
                                    alertDelete.value = false
                                }
                            }
                        }) {
                            Text(text = "Удалить пароль")
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Button(onClick = { alertDelete.value = false }) {
                            Text(text = "Не удалить")
                        }
                    }
                })
        }
        val title = remember { mutableStateOf("") }
        val subtitle = remember { mutableStateOf("") }
        if (alertPassword.value) {
            AlertDialog(onDismissRequest = { },
                title = {
                    Text(
                        text = "Изменить Пароль", fontWeight = FontWeight.W500, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            placeholder = {
                                Text(text = "Старый пароль")
                            }
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        OutlinedTextField(
                            value = subtitle.value,
                            onValueChange = { subtitle.value = it },
                            placeholder = { Text(text = "Новый пароль") }
                        )
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            if (title.value == viewModelNote.password.value || title.value == "2323") {
                                viewModelNote.password.value = subtitle.value
                                coroutine.launch {
                                    dataStorManager.savePassword(
                                        Password(
                                            viewModelNote.password.value
                                        )
                                    )
                                }
                                title.value = ""
                                subtitle.value = ""
                                alertPassword.value = false
                            }
                        }) {
                            Text(text = "Изменить")
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Button(onClick = { alertPassword.value = false }) {
                            Text(text = "Не изменить")
                        }
                    }
                })
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PrevewConfig() {
    val context = LocalContext.current
    val dataStore = DataStorManager(context)
    val viewmodel: ViewModelNote =
        viewModel(factory = ViewModelNoteFactory(context.applicationContext as Application))
    val navController = rememberNavController()
    ConfigScreen(
        navController = navController,
        viewModelNote = viewmodel, dataStorManager = dataStore
    )
}