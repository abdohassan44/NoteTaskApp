package com.example.notetaskapp.dp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("select * from NOTES ORDER BY date DESC LIMIT 1")
    fun getNote(): Note


    @Query("delete from NOTES where id = :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM NOTES")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE title LIKE :query OR text LIKE:query")
    fun searchNote(query: String?): LiveData<List<Note>>


}