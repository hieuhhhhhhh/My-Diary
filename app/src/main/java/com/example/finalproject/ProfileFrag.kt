package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFrag : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get reference to the logout button
        val logoutButton = view.findViewById<Button>(R.id.button5)

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
