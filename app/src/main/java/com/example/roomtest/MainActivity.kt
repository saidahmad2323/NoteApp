package com.example.roomtest

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.colorSpace
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomtest.model.DataStorManager
import com.example.roomtest.model.ViewModelNote
import com.example.roomtest.model.ViewModelNoteFactory
import com.example.roomtest.navigatoin.NavHostControler
import com.example.roomtest.ui.theme.RoomTestTheme
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = DataStorManager(this)
        setContent {

            val context = LocalContext.current
            val viewmodel: ViewModelNote =
                viewModel(factory = ViewModelNoteFactory(context.applicationContext as Application))

            LaunchedEffect(key1 = true) {
                dataStore.getPassword().collect { pass ->
                    viewmodel.password.value = pass.password
                }
            }   

            LaunchedEffect(key1 = true) {
                dataStore.getAccount().collect { account ->
                    viewmodel.name.value = account.name
                    viewmodel.surname.value = account.surname
                }
            }
            LaunchedEffect(key1 = true) {
                dataStore.getSetting().collect { sett ->
                    viewmodel.sizeText.value = sett.textSize
                }
            }
            LaunchedEffect(key1 = true) {
                dataStore.getColor().collect { sett ->
                    viewmodel.stateColor.value = sett.bgColor.toULong()
                }
            }
            LaunchedEffect(key1 = true) {
                dataStore.getShapes().collect { sett ->
                    viewmodel.shapes.value = sett.shapes.toFloat()
                }
            }
            Scaffold {
                NavHostControler(viewmodel, dataStore)
            }
//            MyApp(this)
        }
    }
}


