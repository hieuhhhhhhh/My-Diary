package com.example.finalproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginAct : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Make sure to use your correct layout name

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Initialize UI components
        emailEditText = findViewById(R.id.editTextText)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.button3)
        createAccountButton = findViewById(R.id.button)

        // Set click listener for login button
        loginButton.setOnClickListener {
            loginUser()
        }

        // Set click listener for create account button
        createAccountButton.setOnClickListener {
            // Navigate to the Signup Activity (if needed)
            val intent = Intent(this@LoginAct, SignUpAct::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        // Get email and password from EditText
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            // Show error if email or password is empty
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Call Firebase Auth to sign in the user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in successful, navigate to the home screen or main activity
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    finish() // Escape activity
                } else {
                    // Handle login failure
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}