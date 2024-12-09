package com.example.finalproject

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DraftActi : AppCompatActivity() {

    // Your Visual Crossing API key
    private val apiKey = "A3WMALQVCSAEPZESUFHWVT34Y&contentType=json"

    // Fixed latitude and longitude
    private val latitude = 40.7128
    private val longitude = -74.0060

    // Set up UI elements
    private lateinit var weatherInfoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        // Initialize the TextView
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView)

        // Fetch today's date
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Call the function to fetch weather data
        fetchWeatherData(currentDate)
    }

    private fun fetchWeatherData(date: String) {
        val url =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$latitude,$longitude/$date?key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        // Perform the request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val responseString = responseBody.string()
                        // Parse JSON response and update the UI
                        runOnUiThread {
                            displayWeatherData(responseString)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DraftActi, "Failed to fetch data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@DraftActi,
                        "Request failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun displayWeatherData(responseString: String) {
        // Parse the JSON response
        try {
            val jsonObject = JSONObject(responseString)
            val currentConditions =
                jsonObject.getJSONObject("currentConditions") // This is a JSONObject, not an array

            val temperature = currentConditions.getDouble("temp")
            val humidity =
                currentConditions.getDouble("humidity")  // Corrected to get the value directly
            val description = currentConditions.getString("conditions")

            // Display main fields
            weatherInfoTextView.text = """
            Temperature: $temperature°C
            Humidity: $humidity%
            Description: $description
        """.trimIndent()
        } catch (e: Exception) {
            Log.e("DraftActi", "Error parsing response: ${e.message}")
            Toast.makeText(this, "Error parsing weather data", Toast.LENGTH_SHORT).show()
        }
    }
}
