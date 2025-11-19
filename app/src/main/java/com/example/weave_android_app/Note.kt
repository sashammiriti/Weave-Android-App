package com.example.weave_android_app

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0, // Use Long for timestamp in Realtime DB
    val connections: List<String> = emptyList()
)