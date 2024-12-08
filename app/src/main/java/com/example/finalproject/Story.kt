package com.example.finalproject

import com.google.firebase.firestore.FieldValue


// Define the Story class with a no-argument constructor
data class Story(
    val title: String = "",
    val story: String = "",
    val userId: String = "",
    val timestamp: Any = FieldValue.serverTimestamp(),
    var localDate: String = "",
    var localTime: String = ""
) {
    // No-argument constructor needed by Firestore
    constructor() : this("", "", "")
}
