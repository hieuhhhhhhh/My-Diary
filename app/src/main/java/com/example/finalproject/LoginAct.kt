package com.example.finalproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityLoginBinding

class LoginAct : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Set click listener for login button
        binding.button3.setOnClickListener {
            loginUser()
        }

        // Set click listener for create account button
        binding.button.setOnClickListener {
            // Navigate to the Signup Activity (if needed)
            val intent = Intent(this@LoginAct, SignUpAct::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        // Get email and password from EditText
        val email = binding.editTextText.text.toString()
        val password = binding.editTextTextPassword.text.toString()

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
