package com.example.finalproject

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StoryGroup(
    val date: Date, // Store raw date object here
    val localDate: String, // Keep the formatted date string
    val stories: List<Story> // List of stories for this date
) {
    // Constructor to simplify initialization
    constructor(localDate: String, stories: List<Story>) : this(
        // Convert localDate string back to Date object (you might use SimpleDateFormat here)
        SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).parse(localDate) ?: Date(),
        localDate,
        stories
    )
}
