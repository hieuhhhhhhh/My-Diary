// BaseActivity.kt
package com.example.finalproject

import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Handle "Home" action
                    var intent = Intent(this, HomeActi::class.java)
                    startActivity(intent)
                    true
                }

                R.id.search -> {
                    // Handle "Search" action
                    var intent = Intent(this, SearchActi::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    // Handle "Profile" action
                    var intent = Intent(this, ProfileActi::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }


    // Method that will be called when the button is clicked
    open fun onButtonClicked() {
        // Default implementation - you can override this in the child classes
        println("Btn 2 clicked!")
    }


}
