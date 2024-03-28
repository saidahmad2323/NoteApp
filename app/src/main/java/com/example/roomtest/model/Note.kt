package com.example.roomtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "table_name")
data class Note(
    @ColumnInfo val title: String,
    @ColumnInfo val subtitle: String,
    @ColumnInfo val createDate: String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)