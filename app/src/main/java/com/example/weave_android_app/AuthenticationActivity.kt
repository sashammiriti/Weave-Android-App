package com.example.weave_android_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weave_android_app.databinding.ActivityAuthenticationBinding
import com.google.firebase.FirebaseApp // Add this import

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CRITICAL FIX: Ensure Firebase is initialized here,
        // regardless of the WeaveApplication class success
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavHostFragment will load LoginFragment automatically
    }
}