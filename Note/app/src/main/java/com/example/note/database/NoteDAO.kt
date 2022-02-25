package com.example.note.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.note.data.Note


@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun  insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("Select * from notes_table order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("Select * from notes_table where noteTitle like :searchQuery or noteDes like :searchQuery")
    fun getSearchResults(searchQuery: String): LiveData<List<Note>>
}