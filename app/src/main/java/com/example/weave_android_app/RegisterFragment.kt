package com.example.weave_android_app

import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weave_android_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import androidx.navigation.fragment.findNavController
import com.example.weave_android_app.R


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Uses lazy delegate (safe initialization)
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            handleRegistration()
        }

        binding.btnBackToLogin.setOnClickListener {
            // Navigate back to the previous screen (LoginFragment)
            findNavController().popBackStack()
        }
    }

    private fun handleRegistration() {
        val username = binding.etUsername.text.toString() // Get the username
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank() || password.length < 6) {
            Toast.makeText(context, "Please enter a valid email and password (min 6 chars).", Toast.LENGTH_LONG).show()
            return
        }

        // 1. Create Authentication Entry
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Authentication successful! Now save details to Database.
                    val userId = auth.currentUser?.uid ?: ""

                    // 2. Prepare data to save
                    val userMap = mapOf(
                        "id" to userId,
                        "username" to username,
                        "email" to email
                    )

                    // 3. Save to Realtime Database under a "users" node
                    val databaseRef = FirebaseDatabase.getInstance().reference
                    databaseRef.child("users").child(userId).setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Account created & saved!", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Auth success but DB failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                } else {
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun navigateToHome() {
        // Attempt navigation using the verified action ID
        try {
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        } catch (e: Exception) {
            // If navigation fails (e.g., graph is unstable), show a critical message
            Toast.makeText(context, "Critical Navigation Error: " + e.message, Toast.LENGTH_LONG).show()
            // If you see this message, the navigation setup is the problem.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}