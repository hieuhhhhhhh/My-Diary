package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFrag : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get reference to the logout button
        val logoutButton = view.findViewById<Button>(R.id.button5)
        // Get reference to the TextView where the email will be displayed
        val emailTextView = view.findViewById<TextView>(R.id.textView4)

        // Get the current user
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Check if the user is logged in and display the email
        currentUser?.let {
            val email = it.email
            emailTextView.text = "Email Address: ${email ?: "No email found"}"
        } ?: run {
            emailTextView.text = "No user logged in"
        }

        // Set an OnClickListener on the logout button
        logoutButton.setOnClickListener {
            // Sign out from Firebase
            FirebaseAuth.getInstance().signOut()

            // Navigate to the login activity
            startActivity(Intent(requireContext(), LoginAct::class.java))

            // Optionally close the current activity (if ProfileFrag is part of one)
            requireActivity().finish()
        }
    }
}
