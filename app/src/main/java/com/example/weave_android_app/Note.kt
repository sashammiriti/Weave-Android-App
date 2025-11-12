package com.example.weave_android_app

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Note(
    // 1. Unique Firestore Document ID
    @DocumentId
    val id: String = "",

    // 2. Core Content
    val title: String = "",
    val content: String = "",

    // 3. Metadata for Graph/Context
    @ServerTimestamp
    val timestamp: Timestamp? = null,

    // 4. The Core Feature: List of linked Note IDs
    val connections: List<String> = emptyList(),

    // 5. Future: For the "Spaces" feature (can be expanded later)
    val spaces: List<String> = emptyList()
)