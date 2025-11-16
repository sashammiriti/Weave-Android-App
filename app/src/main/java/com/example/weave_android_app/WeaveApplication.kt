package com.example.weave_android_app

import android.app.Application
import com.google.firebase.FirebaseApp

// This custom class is the first code executed when the app process starts
class WeaveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Ensure Firebase initializes immediately upon app startup
        FirebaseApp.initializeApp(this)
    }
}