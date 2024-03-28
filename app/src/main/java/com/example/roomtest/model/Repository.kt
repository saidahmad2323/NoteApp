package com.example.roomtest.model

import androidx.lifecycle.LiveData

interface Repository {

    val readAllNote: LiveData<List<Note>>

    suspend fun creat(note: Note, createData: String, onSucces: () -> Unit)

    suspend fun update(note: Note, createData: String, onSucces: () -> Unit)

    suspend fun delete(note: Note, onSucces: () -> Unit)

    suspend fun deleteAllNotes(onSuccess: () -> Unit)
}