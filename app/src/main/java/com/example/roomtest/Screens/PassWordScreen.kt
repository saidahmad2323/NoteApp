package com.example.roomtest.Screens

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.roomtest.R
import com.example.roomtest.model.*
import com.example.roomtest.navigatoin.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PasswordScreen(
    navController: NavController,
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager
) {
    val context = LocalContext.current
    BackHandler(
        onBack = {
            (context as? ComponentActivity)?.finish()
        }
    )
    val stateSwitch = remember { mutableStateOf(false) }
    val stateAlertName = remember { mutableStateOf(false) }
    val stateAlert = remember { mutableStateOf(false) }
    val icon = remember { mutableStateOf(R.drawable.ic_baseline_visibility_off_24) }
    val statee = remember { mutableStateOf(false) }
    val stateText = remember {
        mutableStateOf(PasswordVisualTransformation())
    }
    val coroutine = rememberCoroutineScope()
    var state by remember { mutableStateOf("") }
    var state2 by remember { mutableStateOf("") }
    if (viewModelNote.password.value == "1") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Пароль") }, colors =
                TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = if (state == state2 && state.isNotEmpty() && state2.isNotEmpty())
                        Color.Blue else Color.Red, backgroundColor = Color.White
                )
            )
            Spacer(modifier = Modifier.padding(2.dp))
            OutlinedTextField(
                value = state2,
                onValueChange = { state2 = it },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Повторите пароль") },
                colors =
                TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = if (state == state2 && state.isNotEmpty() && state2.isNotEmpty())
                        Color.Blue else Color.Red, backgroundColor = Color.White
                )
            )

            Button(onClick = {
                if (state == state2) {
                    viewModelNote.password.value = state
                    coroutine.launch {
                        dataStorManager.savePassword(
                            Password(
                                viewModelNote.password.value
                            )
                        )
                    }
                }
            }, enabled = state == state2 && state.isNotEmpty() && state2.isNotEmpty()) {
                Text(text = "Регистрация")
            }
        }
    } else {
        if (statee.value) {
            icon.value = R.drawable.ic_baseline_remove_red_eye_24
        } else {
            icon.value = R.drawable.ic_baseline_visibility_off_24
        }
        val popit = remember {
            mutableStateOf("")
        }
        val stateIndicator = remember {
            mutableStateOf(Color.Blue)
        }
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val name = remember { mutableStateOf("") }
            val surname = remember { mutableStateOf("") }
            if (stateAlertName.value) {
                AlertDialog(onDismissRequest = {
                    stateAlertName.value = false
                }, title = { Text(text = "Введите имя и фамилию чтобы сбросить пароль") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                placeholder = { Text(text = "Имя") })
                            Spacer(modifier = Modifier.padding(3.dp))
                            OutlinedTextField(
                                value = surname.value,
                                onValueChange = { surname.value = it }, placeholder = {
                                    Text(text = "Фамилия")
                                })
                        }

                    }, buttons = {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = {
                                if (name.value == viewModelNote.name.value && surname.value == viewModelNote.surname.value) {
                                    viewModelNote.deleteAllNotes {
                                        navController.navigate(NavRoute.StartScreen.route)
                                        viewModelNote.password.value = "1"
                                        coroutine.launch {
                                            dataStorManager.savePassword(
                                                Password(
                                                    viewModelNote.password.value
                                                )
                                            )
                                        }
                                    }
                                }
                            }) {
                                Text(text = "Подверждаю Сброс")
                            }
                        }

                    })
            }
            if (stateAlert.value) {
                AlertDialog(onDismissRequest = {
                    stateAlert.value = false
                }, title = { Text(text = "Предупреждения!!!") },
                    text = {
                        Text(text = "Если вы сбросите пароль то все заметки которые вы имеете будет удалены!")
                    }, buttons = {
                        Button(onClick = {
                            stateAlert.value = false
                            stateAlertName.value = true

                        }) {
                            Text(text = "Сбросить Пароль")
                        }
                    })
            }



            OutlinedTextField(
                value = popit.value,
                onValueChange = { popit.value = it },
                trailingIcon = {
                    IconButton(onClick = {
                        statee.value = !statee.value
                    }) {
                        Icon(
                            painter = painterResource(id = icon.value),
                            contentDescription = ""
                        )
                    }

                },
                visualTransformation = if (statee.value) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = stateIndicator.value
                )
            )

            coroutine.launch(Dispatchers.Main) {
                if (stateSwitch.value) {
                    if (popit.value == viewModelNote.password.value || popit.value == "Открыть пароль") {
                        coroutine.launch {
                            navController.navigate(NavRoute.StartScreen.route)
                        }
                    }
                }
            }

            Button(onClick = {
                if (popit.value == viewModelNote.password.value || popit.value == "Открыть пароль") {
                    navController.navigate(NavRoute.StartScreen.route)
                } else {
                    stateIndicator.value = Color.Red
                    Toast.makeText(
                        context, "Неверный пароль", Toast.LENGTH_SHORT
                    ).show()
                }
            }, enabled = !stateSwitch.value) {
                Text(text = "Вход")
            }
            Button(onClick = {
                stateAlert.value = true
            }) {
                Text(text = "Забыли пароль")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (stateSwitch.value) Color.Blue else Color.Gray,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .width(50.dp)
                    .height(30.dp)
            ) {
                Switch(
                    checked = stateSwitch.value,
                    onCheckedChange = { stateSwitch.value = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackAlpha = 0f,
                        uncheckedTrackAlpha = 0f,
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.LightGray,
                    ), modifier = Modifier.width(100.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PrevewPassword() {
    val context = LocalContext.current
    val dataStore = DataStorManager(context)
    val viewmodel: ViewModelNote =
        viewModel(factory = ViewModelNoteFactory(context.applicationContext as Application))
    val navController = rememberNavController()
    PasswordScreen(
        navController = navController,
        viewModelNote = viewmodel, dataStorManager = dataStore
    )
}