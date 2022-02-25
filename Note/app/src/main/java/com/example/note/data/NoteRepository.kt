package com.example.note.data

import androidx.lifecycle.LiveData
import com.example.note.database.NoteDAO


class  NoteRepository(private val noteDAO: NoteDAO) {


    val allNotes: LiveData<List<Note>> = noteDAO.getAllNotes()


    suspend fun insert(note: Note) {
        noteDAO.insert(note)
    }


    suspend fun delete(note: Note) {
        noteDAO.delete(note)
    }


    suspend fun update(note: Note) {
        noteDAO.update(note)
    }


    fun searchNote(searchQuery: String): LiveData<List<Note>>{
        return noteDAO.getSearchResults(searchQuery)
    }
}