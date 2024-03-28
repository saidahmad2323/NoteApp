package com.example.roomtest.model


import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roomtest.ui.theme.Purple200
import com.example.roomtest.ui.theme.Purple500

import kotlinx.coroutines.launch

import java.util.*

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ViewModelNote(application: Application) : AndroidViewModel(application) {
    val shapes = mutableStateOf(10f)
    val password = mutableStateOf("1")
    val bitmap = mutableStateOf<Bitmap?>(null)
    var name = mutableStateOf("")
    var surname = mutableStateOf("")
    val sizeText = mutableStateOf(40)
    var stateColor = mutableStateOf(Red.value)
    private val REPOSITORY: RoomRepository

    init {
        val dao = DatabaseRoom.getInstance(application).dao()
        REPOSITORY = RoomRepository(dao)
    }

    val readNote = REPOSITORY.readAllNote

    fun addNote(title: String, subtitle: String, onCreat: () -> Unit) {
        val createDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        val note = Note(title = title, subtitle = subtitle, createDate = createDate)
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.creat(note, createDate) {
                onCreat()
            }
        }
    }

    fun delete(note: Note, onSucces: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.delete(note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSucces()
                }
            }
        }
    }

    fun update(note: Note, createDate: String, onSucces: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteWithDate = note.copy(createDate = createDate)
            REPOSITORY.update(noteWithDate, createDate) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSucces()
                }
            }
        }
    }

    fun deleteAllNotes(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteAllNotes() {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun getAllNotes() = REPOSITORY.readAllNote
}


class ViewModelNoteFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelNote::class.java)) {
            return ViewModelNote(application) as T
        }
        throw IllegalArgumentException("throw")
    }
}