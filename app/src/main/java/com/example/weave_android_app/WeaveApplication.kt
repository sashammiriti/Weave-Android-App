package com.example.weave_android_app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase // Import for Realtime Database

class WeaveApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 1. Initialize Firebase core
        FirebaseApp.initializeApp(this)

        // 2. Enable Offline Persistence
        // This tells Realtime Database to store data locally on the phone
        // so the app works even when offline.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}