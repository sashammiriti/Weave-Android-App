package com.example.weave_android_app.data.repository

import com.example.weave_android_app.Note
import com.example.weave_android_app.util.Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NoteRepositoryImpl : NoteRepository {

    // Get reference to the "notes" node in the JSON tree
    private val database = FirebaseDatabase.getInstance().reference.child("notes")

    override suspend fun saveNote(note: Note): Result<Unit> {
        return try {
            // Generate a unique key if the note doesn't have an ID
            val noteId = note.id.ifEmpty { database.push().key ?: "" }
            val noteToSave = note.copy(id = noteId)

            // Save to Realtime Database
            database.child(noteId).setValue(noteToSave).await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getAllNotes(): Flow<List<Note>> = callbackFlow {
        // Listen for real-time updates
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = mutableListOf<Note>()
                for (childSnapshot in snapshot.children) {
                    val note = childSnapshot.getValue(Note::class.java)
                    note?.let { notes.add(it) }
                }
                trySend(notes)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        database.addValueEventListener(listener)

        awaitClose { database.removeEventListener(listener) }
    }

    override suspend fun getNoteById(noteId: String): Result<Note?> {
        return try {
            val snapshot = database.child(noteId).get().await()
            val note = snapshot.getValue(Note::class.java)
            Result.Success(note)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            database.child(noteId).removeValue().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}