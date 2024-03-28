package com.example.roomtest.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roomtest.R
import com.example.roomtest.model.*
import com.example.roomtest.model.Colors
import com.example.roomtest.model.Shapes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.R as R1


@Composable
fun SettingScreen(
    viewModelNote: ViewModelNote,
    dataStorManager: DataStorManager,
    navController: NavController
) {
    val coroutine = rememberCoroutineScope()
    var sliderPosition by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(70.dp)
                .background(
                    color = Color(viewModelNote.stateColor.value),
                    shape = RoundedCornerShape(viewModelNote.shapes.value.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Размер текста",
                fontSize = viewModelNote.sizeText.value.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val colors = listOf(
                Color.Blue, Color.Red, Color.Cyan, Color.LightGray, Color.Green, Color.Magenta
            )

            colors.forEach { color ->
                CardItem(color = color) {
                    viewModelNote.stateColor.value = color.value
                    coroutine.launch {
                        dataStorManager.saveColor(
                            Colors(
                                bgColor = color.value.toLong()
                            )
                        )
                    }
                }
            }
        }
        Text(text = viewModelNote.shapes.value.toInt().toString())

        Slider(
            value = viewModelNote.shapes.value,
            onValueChange = {
                viewModelNote.shapes.value = it
                coroutine.launch {
                    dataStorManager.saveShape(
                        Shapes(
                            viewModelNote.shapes.value.toInt()
                        )
                    )
                }
            },
            valueRange = 0f..30f,
            modifier = Modifier.padding(horizontal = 50.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Card(
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp), backgroundColor = Color.Gray
            ) {
                IconButton(onClick = {
                    coroutine.launch {
                        dataStorManager.saveSetting(
                            SettingsData(
                                viewModelNote.sizeText.value + 2
                            )
                        )
                    }

                }, enabled = viewModelNote.sizeText.value != 30) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(1.dp))
            Card(
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp), backgroundColor = Color.Gray
            ) {
                IconButton(onClick = {
                    coroutine.launch {
                        dataStorManager.saveSetting(
                            SettingsData(
                                viewModelNote.sizeText.value - 2
                            )
                        )
                    }
                }, enabled = viewModelNote.sizeText.value != 18) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                        contentDescription = "age"
                    )
                }
            }
        }
    }
}


@Composable
fun CardItem(
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        backgroundColor = color,
        border = BorderStroke(1.dp, color = Color.Black)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(shape = CircleShape, color = Color.Transparent)
                .clickable { onClick() }
        ) {

        }
    }
}
