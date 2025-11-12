package com.example.weave_android_app.data.repository // Ensure your actual root package name is here

import com.example.weave_android_app.Note // Ensure the path to your Note.kt is correct
import kotlinx.coroutines.flow.Flow
import com.example.weave_android_app.util.Result

/**
 * Defines the contract for all data operations related to Note objects.
 * This abstracts the data source (Firebase Firestore) from the rest of the app.
 */
interface NoteRepository {

    // 1. Function to save or update a Note
    // 'suspend' makes this a non-blocking asynchronous operation.
    suspend fun saveNote(note: Note): Result<Unit> // Result<Unit> indicates success or failure

    // 2. Function to fetch all Notes
    // 'Flow' provides a stream of data for real-time updates (ideal for Firestore).
    fun getAllNotes(): Flow<List<Note>>

    // 3. Function to retrieve a single Note by its ID
    suspend fun getNoteById(noteId: String): Result<Note?>

    // 4. Function to delete a Note
    suspend fun deleteNote(noteId: String): Result<Unit>
}