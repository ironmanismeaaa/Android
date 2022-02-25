package com.example.note.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.note.data.Note
import com.example.note.data.NoteRepository
import com.example.note.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteViewModel(application: Application) : AndroidViewModel(application) {


    val allNotes: LiveData<List<Note>>
    private val repository: NoteRepository


    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()

        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }


    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }


    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }


    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }


    fun searchNote(searchQuery: String):LiveData<List<Note>> {
        return repository.searchNote(searchQuery)
    }
}