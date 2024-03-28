package com.example.roomtest.model

import androidx.lifecycle.LiveData

class   RoomRepository(private val dao: Dao): Repository {
    override val readAllNote: LiveData<List<Note>>
        get() = dao.getAllNotes()

    override suspend fun creat(note: Note, createData: String, onSucces: () -> Unit) {
        val noteWithData = note.copy(createDate = createData)
        dao.insert(note)
        onSucces()
    }

    override suspend fun update(note: Note, createData: String, onSucces: () -> Unit) {
        val noteWithData = note.copy(createDate = createData)
        dao.update(note)
        onSucces()
    }

    override suspend fun delete(note: Note, onSucces: () -> Unit) {
        dao.delete(note)
        onSucces()
    }

    override suspend fun deleteAllNotes(onSuccess: () -> Unit) {
        dao.deleteAll()
        onSuccess()
    }
}

lateinit var REPOSITORY: RoomRepository