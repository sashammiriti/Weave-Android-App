package com.example.weave_android_app.data.repository

import com.example.weave_android_app.Note
import com.example.weave_android_app.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await // Important for converting Firebase tasks to Coroutines

// 1. Pass the Firestore instance into the constructor
class NoteRepositoryImpl(private val firestore: FirebaseFirestore) : NoteRepository {

    // Firestore collection path
    private val notesCollection = firestore.collection("notes")

    // --- Implementation of saveNote ---
    override suspend fun saveNote(note: Note): Result<Unit> {
        return try {
            // If ID is empty, Firestore will generate a new one (new note)
            // If ID exists, it updates the existing document
            notesCollection.document(note.id.ifEmpty { notesCollection.document().id })
                .set(note)
                .await() // Suspends execution until the save is complete
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // --- Implementation of getAllNotes ---
    // Uses snapshots to provide real-time data as a Flow
    override fun getAllNotes(): Flow<List<Note>> {
        return notesCollection.snapshots() // Get real-time updates
            .map { querySnapshot ->
                // Convert each Firestore DocumentSnapshot into a Note object
                querySnapshot.documents.mapNotNull { it.toObject(Note::class.java) }
            }
            .catch { e ->
                // If an error occurs (e.g., connection lost), emit the error
                emit(emptyList())
                // Log or handle the error appropriately
            }
    }

    // --- Implementation of getNoteById (Future Use) ---
    override suspend fun getNoteById(noteId: String): Result<Note?> {
        return try {
            val documentSnapshot = notesCollection.document(noteId).get().await()
            Result.Success(documentSnapshot.toObject(Note::class.java))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // --- Implementation of deleteNote (Future Use) ---
    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            notesCollection.document(noteId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}