package com.example.notetaskapp.di

import android.content.Context
import com.example.notetaskapp.dp.NoteDao
import com.example.notetaskapp.dp.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): NoteDatabase {
        return NoteDatabase.createDatabase(context)
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.getNoteDao()
    }
}