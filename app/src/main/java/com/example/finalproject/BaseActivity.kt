// BaseActivity.kt
package com.example.finalproject

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    // Define a variable for the button
    private lateinit var button: Button

    fun initBtn() {
        // Initialize the button
        button = findViewById(R.id.button)

        // Set a click listener for the button (this can be overridden in subclasses if needed)
        button.setOnClickListener {
            onButtonClicked()
        }
    }


    // Method that will be called when the button is clicked
    open fun onButtonClicked() {
        // Default implementation - you can override this in the child classes
        println("Btn 2 clicked!")
    }

}
