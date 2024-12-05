package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpAct : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var goBackButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Bind UI elements
        emailEditText = findViewById(R.id.editTextText)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        continueButton = findViewById(R.id.button3)
        goBackButton = findViewById(R.id.button)

        // Handle Continue Button
        continueButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validate inputs
            if (email.isEmpty()) {
                emailEditText.error = "Email is required!"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Enter a valid email!"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required!"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters!"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Create user in Firebase
            signUpWithEmail(email, password)
        }

        // Handle Go Back Button
        goBackButton.setOnClickListener {
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
