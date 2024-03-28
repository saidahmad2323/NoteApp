package com.example.roomtest.model

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.roomtest.ui.theme.Purple500
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

class DataStorManager(private val context: Context) {


    suspend fun saveSetting(settingsData: SettingsData) {
        context.dataStore.edit { preferences ->
            preferences[textSizeKey] = settingsData.textSize
        }
    }

    fun getSetting(): Flow<SettingsData> = context.dataStore.data.map { preferences ->
        SettingsData(
            textSize = preferences[textSizeKey] ?: DEFAULT_TEXT_SIZE
        )
    }

    suspend fun saveAccount(profile: Profile) {
        context.dataStore.edit { preferences ->
            preferences[nameKey] = profile.name
            preferences[surnameKey] = profile.surname
        }
    }

    fun getAccount(): Flow<Profile> = context.dataStore.data.map { preferences ->
        Profile(
            name = preferences[nameKey] ?: DEFAULT_NAME,
            surname = preferences[surnameKey] ?: DEFAULT_SURNAME
        )
    }

    suspend fun saveColor(colors: Colors) {
        context.dataStore.edit { preferences ->
            preferences[bgColorKey] = colors.bgColor
        }
    }

    fun getColor(): Flow<Colors> = context.dataStore.data.map { preferences ->
        Colors(
            bgColor = preferences[bgColorKey] ?: Color.Transparent.value.toLong()
        )
    }

    suspend fun savePassword(password: Password) {
        context.dataStore.edit { preferences ->
            preferences[passKey] = password.password
        }
    }

    fun getPassword(): Flow<Password> = context.dataStore.data.map { preferences ->
        Password(
            password = preferences[passKey] ?: DEFAULT_PASSWORD
        )
    }

    suspend fun saveShape(shapes: Shapes) {
        context.dataStore.edit { preferences ->
            preferences[shapeKey] = shapes.shapes
        }
    }

    fun getShapes(): Flow<Shapes> = context.dataStore.data.map { preferences ->
        Shapes(
            shapes = preferences[shapeKey] ?: DEFAULT_SHAPES
        )
    }
}

private const val DEFAULT_TEXT_SIZE = 20
private const val DEFAULT_NAME = "Пользователь"
private const val DEFAULT_SURNAME = "Пользователь"
private const val DEFAULT_PASSWORD = "1"
private const val DEFAULT_SHAPES = 10
private val textSizeKey = intPreferencesKey("text_size")
private val nameKey = stringPreferencesKey("name_string")
private val surnameKey = stringPreferencesKey("surname_string")
private val bgColorKey = longPreferencesKey("bg_color")
private val passKey = stringPreferencesKey("pass_key")
private val shapeKey = intPreferencesKey("shapes")
