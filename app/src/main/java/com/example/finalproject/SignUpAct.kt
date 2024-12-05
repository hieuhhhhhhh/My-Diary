package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpAct : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Handle Continue Button
        binding.button3.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            // Validate inputs
            if (email.isEmpty()) {
                binding.editTextText.error = "Email is required!"
                binding.editTextText.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextText.error = "Enter a valid email!"
                binding.editTextText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.editTextTextPassword.error = "Password is required!"
                binding.editTextTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.editTextTextPassword.error = "Password must be at least 6 characters!"
                binding.editTextTextPassword.requestFocus()
                return@setOnClickListener
            }

            // Create user in Firebase
            signUpWithEmail(email, password)
        }

        // Handle Go Back Button
        binding.button.setOnClickListener {
            finish() // Close current activity and go back
        }
    }

    private fun signUpWithEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-up successful
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_LONG).show()
                    println("Account created successfully!")
                    finishAffinity()
                    startActivity(Intent(this, MainActivity::class.java))

                } else {
                    // Sign-up failed
                    val exception = task.exception
                    when {
                        exception is FirebaseAuthUserCollisionException -> {
                            Toast.makeText(
                                this,
                                "This email is already registered.",
                                Toast.LENGTH_LONG
                            ).show()
                            println("This email is already registered.")

                        }

                        else -> {
                            Toast.makeText(
                                this,
                                "Sign-up failed: ${exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            println("Sign-up failed: ${exception?.message}")
                        }
                    }
                }
            }
    }
}