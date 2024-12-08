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

    private lateinit var titleInput: EditText
    private lateinit var storyInput: EditText
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
        titleInput = view.findViewById(R.id.tittleInput) // Title input field
        storyInput = view.findViewById(R.id.story_input) // Story input field
        button = view.findViewById(R.id.button2)

        // Set button click listener
        button.setOnClickListener {
            val titleText = titleInput.text.toString().trim()
            val storyText = storyInput.text.toString().trim()

            if (storyText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please write something before submitting.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // If title is empty, generate a title from the first words of the story
            val finalTitle = titleText.ifEmpty {
                getTitleFromStory(storyText)
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

            // Create a Story object
            val story = Story(
                title = finalTitle,
                story = storyText,
                userId = userId
            )

            // Add the Story object to Firestore
            db.collection("stories")
                .add(story)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Story saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        storyInput.setText("") // Clear the story input field
                        titleInput.setText("") // Clear the title input field
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

    // Function to get title from the first words of the story (max 20 characters)
    private fun getTitleFromStory(storyText: String): String {
        val words = storyText.split(" ") // Split the story into words
        var title = ""
        var totalLength = 0

        // Add words to title until the total length exceeds 20 characters
        for (word in words) {
            if (totalLength + word.length + 1 > 20) break // Stop if adding the word exceeds 20 characters
            if (title.isNotEmpty()) title += " " // Add space before the next word
            title += word
            totalLength += word.length + 1 // Add the length of the word plus space
        }

        // If the title is still longer than 20 characters, truncate with "..."
        return if (title.length > 20) {
            title.substring(0, 17) + "..." // Truncate and add "..."
        } else {
            title
        }
    }
}
