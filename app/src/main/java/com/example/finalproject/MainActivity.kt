package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : BaseActivity() {
    private lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the button
        button2 = findViewById(R.id.button2)

        // Set a click listener for the button (this can be overridden in subclasses if needed)
        button2.setOnClickListener {
            onButton2Clicked()
        }

        initBtn()
    }

    // Method that will be called when the button is clicked
    private fun onButton2Clicked() {
        // Default implementation - you can override this in the child classes
        println("Btn 1 clicked!")
    }
}