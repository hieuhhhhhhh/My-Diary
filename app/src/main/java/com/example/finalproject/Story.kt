package com.example.finalproject

import com.google.firebase.firestore.FieldValue

data class Story(
    val title: String,
    val story: String,
    val userId: String,
    val timestamp: Any = FieldValue.serverTimestamp()
)
