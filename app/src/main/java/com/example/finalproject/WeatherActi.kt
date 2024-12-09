package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WeatherActi : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button6: Button
    private lateinit var returnButton: ImageButton // Reference for the return button
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        textView = findViewById(R.id.textView)
        button6 = findViewById(R.id.button6)  // Reference to the Refresh button
        returnButton = findViewById(R.id.returnButton) // Reference to the return button
        locationHelper = LocationHelper(this)

        locationHelper.getLocation { result, success ->
            if (success) {
                // Success case
                textView.text = "Location: $result"
                button6.visibility = Button.GONE  // Hide button on success
            } else {
                // Failure case
                textView.text = "Error: $result"
                button6.visibility = Button.VISIBLE  // Show button on failure

                // Set up click listener to reload the activity on failure
                button6.setOnClickListener {
                    // Restart the activity (reload)
                    recreate()  // This will recreate the activity and try the location request again
                }
            }
        }

        // Set up return button to finish the activity when clicked
        returnButton.setOnClickListener {
            finish() // Close the activity and return to the previous one
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(requestCode, grantResults) { result, success ->
            if (success) {
                // Success case
                textView.text = "Location: $result"
                button6.visibility = Button.GONE  // Hide button on success
            } else {
                // Failure case
                textView.text = "Error: $result"
                button6.visibility = Button.VISIBLE  // Show button on failure

                // Set up click listener to reload the activity on failure
                button6.setOnClickListener {
                    // Restart the activity (reload)
                    recreate()  // This will recreate the activity and try the location request again
                }
            }
        }
    }
}
