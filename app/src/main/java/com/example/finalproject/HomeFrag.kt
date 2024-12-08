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
    private lateinit var storyGroupAdapter: StoryGroupAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val storyGroups = mutableListOf<StoryGroup>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewStories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        storyGroupAdapter = StoryGroupAdapter(storyGroups)
        recyclerView.adapter = storyGroupAdapter

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
                val stories = mutableListOf<Story>()
                for (document in querySnapshot.documents) {
                    val story = document.toObject(Story::class.java)
                    if (story != null && story.timestamp is com.google.firebase.Timestamp) {
                        val timestamp = story.timestamp
                        val date = timestamp.toDate()

                        // Use SimpleDateFormat to format the date and time
                        val dateFormat = java.text.SimpleDateFormat(
                            "EEE, MMM dd, yyyy",
                            java.util.Locale.getDefault()
                        )

                        val timeFormat = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US)

                        story.localDate = dateFormat.format(date)
                        story.localTime = timeFormat.format(date)

                        stories.add(story)
                    }
                }

                groupStoriesByDate(stories)
            }
            .addOnFailureListener { e ->
                // Handle the error (e.g., log it)
                println("Error: " + e.message)
            }
    }

    // Group the stories by date and notify the adapter
    private fun groupStoriesByDate(stories: List<Story>) {
        val grouped = mutableListOf<StoryGroup>()

        // Group stories by their localDate
        stories.groupBy { it.localDate }.forEach { (date, storiesForDate) ->
            val storyGroup = StoryGroup(date, storiesForDate)
            grouped.add(storyGroup)
        }

        storyGroups.clear()
        storyGroups.addAll(grouped)
        storyGroupAdapter.notifyDataSetChanged()
    }
}
