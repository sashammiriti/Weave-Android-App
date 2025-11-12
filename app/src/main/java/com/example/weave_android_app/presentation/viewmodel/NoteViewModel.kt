package com.example.weave_android_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weave_android_app.Note
import com.example.weave_android_app.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 1. ViewModel takes the Repository as a dependency
@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    // 2. StateFlow to hold the list of Notes for the UI to observe
    // StateFlow is a lifecycle-aware observable data holder.
    val notes: StateFlow<List<Note>> = repository.getAllNotes()
        .map { notesResult ->
            // If the repository returns a list, map it directly.
            // Since getAllNotes() returns Flow<List<Note>>, we map the list.
            notesResult
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Start collecting when UI is visible
            initialValue = emptyList()
        )

    // 3. Function to save a new note from the UI
    fun saveNewNote(title: String, content: String) {
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content
            )
            repository.saveNote(newNote)
        }
    }
}