package com.example.weave_android_app.data.repository

import com.example.weave_android_app.Note
import com.example.weave_android_app.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class NoteRepositoryImpl(
    private val firestore: FirebaseFirestore
) : NoteRepository {

    private val notesCollection = firestore.collection("notes")

    override suspend fun saveNote(note: Note): Result<Unit> {
        return try {
            notesCollection.document(note.id.ifEmpty { notesCollection.document().id })
                .set(note)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getAllNotes(): Flow<List<Note>> = callbackFlow {
        val subscription = firestore.collection("notes")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e) // Close the flow on error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    // Map the DocumentSnapshots to Note objects
                    val notes = snapshot.documents.mapNotNull {
                        it.toObject(Note::class.java)
                    }
                    trySend(notes) // Send the new list of notes to the collector
                } else {
                    trySend(emptyList())
                }
            }

        // This block runs when the collector stops (e.g., Fragment is destroyed)
        awaitClose { subscription.remove() }
    }
        .catch { e ->
            emit(emptyList()) // Handle errors gracefully
        }
    override suspend fun getNoteById(noteId: String): Result<Note?> {
        return try {
            val documentSnapshot = notesCollection.document(noteId).get().await()
            Result.Success(documentSnapshot.toObject(Note::class.java))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            notesCollection.document(noteId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}