package com.example.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddFrag : Fragment(R.layout.fragment_add) {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        // Initialize Firebase Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        editText = view.findViewById(R.id.editTextTextMultiLine)
        button = view.findViewById(R.id.button2)

        // Set button click listener
        button.setOnClickListener {
            val storyText = editText.text.toString().trim()

            if (storyText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please write something before submitting.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(
                    requireContext(),
                    "You need to be logged in to submit a story.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val userId = currentUser.uid

            // Create a map to hold the data, including userId
            val storyData = hashMapOf(
                "story" to storyText,
                "userId" to userId
            )

            // Add the data to Firestore in the collection "test1"
            db.collection("test1")
                .add(storyData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Story saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        editText.setText("") // Clear the EditText
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to save story: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        println("Failed to save story: ${task.exception?.message}")
                    }
                }
        }

        return view
    }
}
