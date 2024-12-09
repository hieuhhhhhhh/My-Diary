package com.example.finalproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class StoryGroupAdapter(private val storyGroups: List<StoryGroup>) :
    RecyclerView.Adapter<StoryGroupAdapter.StoryGroupViewHolder>() {

    // ViewHolder class to hold views for each group (date and its list of stories)
    inner class StoryGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val storiesRecyclerView: RecyclerView = view.findViewById(R.id.storiesRecyclerView)
        val expandIcon: ImageView =
            view.findViewById(R.id.expandIcon)  // ImageView for clickable icon
    }

    // Called to create a new view (for each StoryGroup)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryGroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story_group, parent, false)
        return StoryGroupViewHolder(view)
    }

    // Called to bind the data to the ViewHolder
    override fun onBindViewHolder(holder: StoryGroupViewHolder, position: Int) {
        val storyGroup = storyGroups[position]
        holder.dateTextView.text = storyGroup.localDate

        // Set the inner RecyclerView adapter for stories
        val storyAdapter = StoryAdapter(storyGroup.stories)
        holder.storiesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.storiesRecyclerView.adapter = storyAdapter

        // Set OnClickListener for the expandIcon (ImageView)
        holder.expandIcon.setOnClickListener {
            // Move to WeatherActi when icon is clicked
            val context = holder.itemView.context
            val intent = Intent(context, WeatherActi::class.java)

            // Pass the date to the WeatherActi
            intent.putExtra("date", storyGroup.date) // Convert Date to long (timestamp)
            context.startActivity(intent)
        }
    }

    // Return the total number of StoryGroup items
    override fun getItemCount(): Int = storyGroups.size
}
