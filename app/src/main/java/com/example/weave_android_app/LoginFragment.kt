package com.example.weave_android_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weave_android_app.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.findNavController
import com.example.weave_android_app.R

// Ensure the R file is explicitly imported if needed, otherwise rely on IDE's automatic import
// import com.example.weave_android_app.R

class LoginFragment : Fragment() {

    // View Binding is the modern way to access UI elements safely
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // CRITICAL FIX: Use 'by lazy' to ensure FirebaseAuth is initialized ONLY
    // when first accessed, guaranteeing FirebaseApp is ready.
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Check if user is already logged in (This is the first time 'auth' is accessed)
        if (auth.currentUser != null) {
            // If authenticated, navigate straight to the Home screen
            navigateToHome()
            // We exit here, so the click listeners below are NOT registered for efficiency.
            return
        }

        // 2. Set up click listeners for UNATHENTICATED users
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // CORRECT LISTENER PLACEMENT: This handles navigation to the Register page.
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Removed: Duplicated btnRegister listener was here inside an 'if' block.
    }

    // --- Authentication Logic ---

    private fun handleLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // --- DELETED OBSOLETE FUNCTION ---
    // private fun handleRegistration() { ... } // DELETED: Navigation now handles this.
    // The handleRegistration logic is not needed here as we are navigating away.

    // --- Navigation (Placeholder) ---
    // --- Navigation (Placeholder) ---
    private fun navigateToHome() {
        // Navigates from Login to Home
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    // Always clear the binding reference
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}