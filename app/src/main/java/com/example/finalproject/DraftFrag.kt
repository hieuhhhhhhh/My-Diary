package com.example.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DraftFrag : Fragment(R.layout.fragment_draft) {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var button: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draft, container, false)

        // Initialize Firebase Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        button = view.findViewById(R.id.button4)
        recyclerView = view.findViewById(R.id.recycler_view)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        storyAdapter = StoryAdapter()
        recyclerView.adapter = storyAdapter

        // Set button click listener
        button.setOnClickListener {
            fetchDrafts()
        }

        return view
    }

    private fun fetchDrafts() {
        // Get current user
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(
                requireContext(),
                "You need to be logged in to view drafts.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Fetch drafts from Firestore where the userId matches the current user's ID
        db.collection("test1")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    val drafts = mutableListOf<Story>()
                    documents?.forEach { document ->
                        val story = document.toObject(Story::class.java)
                        drafts.add(story)
                    }
                    // Update the adapter with the fetched drafts
                    storyAdapter.submitList(drafts)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch drafts: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("Failed to fetch drafts: ${task.exception?.message}")
                }
            }
    }

    // Define a Story data model class
    data class Story(
        val story: String = "",
        val userId: String = "",
        val timestamp: Long = 0L
    )

    // RecyclerView Adapter
    class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

        private val drafts = mutableListOf<Story>()

        fun submitList(newList: List<Story>) {
            drafts.clear()
            drafts.addAll(newList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return StoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
            val story = drafts[position]
            holder.bind(story)
        }

        override fun getItemCount(): Int = drafts.size

        inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(story: Story) {
                // Display the story text in the item view
                (itemView as TextView).text = story.story
            }
        }
    }
}
