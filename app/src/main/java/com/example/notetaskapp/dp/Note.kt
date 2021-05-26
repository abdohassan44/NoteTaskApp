package com.example.notetaskapp.dp

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val text: String,
    val date: Long?,
    val reminder: Long?
) : Parcelable