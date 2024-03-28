package com.example.roomtest.model

import android.graphics.Bitmap
import android.graphics.fonts.FontFamily

data class SettingsData(
    val textSize: Int
)

data class Profile(
    val name: String,
    val surname: String
)

data class Colors(
    val bgColor: Long
)

data class Password(
    val password: String
)

data class Shapes(
    val shapes: Int
)
