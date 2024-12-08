package com.example.finalproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFrag : Fragment(R.layout.fragment_home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val stories = mutableListOf<Story>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewStories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        storyAdapter = StoryAdapter(stories)
        recyclerView.adapter = storyAdapter

        fetchStories()
    }

    private fun fetchStories() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        firestore.collection("stories")
            .whereEqualTo("userId", userId)  // Only fetch stories for this user
            .orderBy(
                "timestamp",
                Query.Direction.DESCENDING
            )  // Order by timestamp in descending order
            .get()
            .addOnSuccessListener { querySnapshot ->
                stories.clear()
                for (document in querySnapshot.documents) {
                    val story = document.toObject(Story::class.java)
                    if (story != null && story.timestamp is com.google.firebase.Timestamp) {
                        val timestamp = story.timestamp
                        val date = timestamp.toDate()

                        // Use SimpleDateFormat to format the date and time
                        val dateFormat =
                            java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

                        val timeFormat = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US)

                        story.localDate = dateFormat.format(date)
                        story.localTime = timeFormat.format(date)

                        stories.add(story)
                    }
                }
                storyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle the error (e.g., log it)
                println("Error: " + e.message)
            }
    }

}
