package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(private val stories: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.storyTitle)
        val storyTextView: TextView = view.findViewById(R.id.storyContent)
        val timeTextView: TextView = view.findViewById(R.id.timeView) // Added reference to timeView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.titleTextView.text = story.title
        holder.storyTextView.text = story.story
        holder.timeTextView.text = story.localTime // Set the localTime to timeView
    }

    override fun getItemCount(): Int = stories.size
}
