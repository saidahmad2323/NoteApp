package com.example.roomtest.DrawerMenu

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roomtest.R
import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.Profile
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.navigatoin.NavRoute
import com.example.roomtest.ui.theme.Redbul
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager,
    navController: NavController, onClick: () -> Unit
) {
    Column {
        HeaderDrawer(viewModelNote, dataStorManager)
        ContentDrawer(navController, onClick)
    }
}

@Composable
fun HeaderDrawer(
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager
) {
    val context = LocalContext.current
    val stateColor = remember {
        mutableStateOf(Color.Red)
    }
    val name = remember {
        mutableStateOf("")
    }
    val surname = remember {
        mutableStateOf("")
    }
    val coroutine = rememberCoroutineScope()
    val state = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(15.dp), shape = RoundedCornerShape(20.dp), backgroundColor = Color.Gray
    ) {
        if (viewModelNote.name.value == "Пользователь") {
            stateColor.value = Redbul
        } else {
            stateColor.value = Color.Blue
        }
        if (state.value) {
            AlertDialog(
                onDismissRequest = { state.value = false },
                title = {
                    Text(
                        text = "Редактировать Профиль",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                }, text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = name.value,
                            onValueChange = { name.value = it })

                        TextField(
                            value = surname.value,
                            onValueChange = { surname.value = it })

                        Button(onClick = {
                            coroutine.launch {
                                viewModelNote.name.value = name.value
                                viewModelNote.surname.value = surname.value
                                state.value = false
                                dataStorManager.saveAccount(
                                    Profile(
                                        name = viewModelNote.name.value,
                                        surname = viewModelNote.surname.value
                                    )
                                )
                            }
                        }) {
                            Text(text = "Create")
                        }
                    }

                }, buttons = {

                }
            )
        }
        val stateIcon =
            remember { mutableStateOf(R.drawable.ic_baseline_sentiment_very_dissatisfied_24) }
        if (viewModelNote.name.value == "Пользователь") {
            stateIcon.value = R.drawable.ic_baseline_sentiment_very_dissatisfied_24
        } else {
            stateIcon.value = R.drawable.ic_baseline_sentiment_satisfied_alt_24
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(70.dp),
                backgroundColor = Color.Gray,
                elevation = 5.dp,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = stateIcon.value),
                    contentDescription = "state",
                    modifier = Modifier.size(90.dp)
                )
            }
            Text(text = viewModelNote.name.value, fontSize = 30.sp)
            Text(text = viewModelNote.surname.value, fontSize = 29.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = if (viewModelNote.name.value == "Пользователь") "Неидентифицирован"
                else "Идентифицирован", Modifier.padding(5.dp), color = stateColor.value
            )

            IconButton(onClick = {
                state.value = true
                name.value = viewModelNote.name.value
                surname.value = viewModelNote.surname.value
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit"
                )
            }
        }
    }
}

@Composable
fun ContentDrawer(
    navController: NavController,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val coroutine = rememberCoroutineScope()
        var name by remember {
            mutableStateOf("")
        }
        var surname by remember {
            mutableStateOf("")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(37.dp)
                .padding(horizontal = 15.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.Gray
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .clickable {
                        navController.navigate(NavRoute.SettingScreen.route)
                        onClick()
                    }, contentAlignment = Alignment.Center
            ) {
                Text(text = "Настройки")
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(37.dp)
                .padding(horizontal = 15.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.Gray
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .clickable {
                        navController.navigate(NavRoute.ConfigScreen.route)
                        onClick()
                    }, contentAlignment = Alignment.Center
            ) {
                Text(text = "Конфиденциальность")
            }
        }

    }
}