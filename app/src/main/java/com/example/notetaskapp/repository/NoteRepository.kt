package com.example.notetaskapp.repository

import androidx.annotation.VisibleForTesting
import com.example.notetaskapp.dp.Note
import com.example.notetaskapp.dp.NoteDao
import javax.inject.Inject

class NoteRepository @Inject constructor(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val noteDao: NoteDao
) {

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNote(noteId: Int) = noteDao.deleteNote(noteId)
    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    fun getAllNotes() = noteDao.getAllNotes()
    fun getNote() = noteDao.getNote()
    fun searchNote(query: String?) = noteDao.searchNote(query)

}