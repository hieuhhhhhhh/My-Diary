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
    private val latitude = 40
    private val longitude = -74

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

                        // Log the full JSON response
                        Log.d("WeatherAPI", "Full JSON Response: $responseString")

                        // Parse JSON response and update the UI
                        runOnUiThread {
                            weatherInfoTextView.text = displayWeatherData(responseString)
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


    private fun displayWeatherData(responseString: String): String {
        // Parse the JSON response
        val jsonResponse = JSONObject(responseString)

        // Extract the main weather information
        val latitude = jsonResponse.getDouble("latitude")
        val longitude = jsonResponse.getDouble("longitude")
        val description = jsonResponse.getString("description")

        // Initialize a string builder to construct the weather information string
        val weatherInfo = StringBuilder()

        // Add basic information
        weatherInfo.append("Location: Lat $latitude, Long $longitude\n")
        weatherInfo.append("Description: $description\n\n")

        // Extract weather data for the day
        val days = jsonResponse.getJSONArray("days")
        if (days.length() > 0) {
            val dayData = days.getJSONObject(0)  // Assuming we only want the data for the first day

            val date = dayData.getString("datetime")
            val tempMax = dayData.getDouble("tempmax")
            val tempMin = dayData.getDouble("tempmin")
            val currentTemp = dayData.getDouble("temp")
            val humidity = dayData.getDouble("humidity")
            val windSpeed = dayData.getDouble("windspeed")
            val pressure = dayData.getDouble("pressure")
            val conditions = dayData.getString("conditions")
            val sunrise = dayData.getString("sunrise")
            val sunset = dayData.getString("sunset")

            // Add the day-specific weather information to the string
            weatherInfo.append("Weather for $date:\n")
            weatherInfo.append("Max Temp: $tempMax°F\n")
            weatherInfo.append("Min Temp: $tempMin°F\n")
            weatherInfo.append("Current Temp: $currentTemp°F\n")
            weatherInfo.append("Humidity: $humidity%\n")
            weatherInfo.append("Wind Speed: $windSpeed mph\n")
            weatherInfo.append("Pressure: $pressure hPa\n")
            weatherInfo.append("Conditions: $conditions\n")
            weatherInfo.append("Sunrise: $sunrise\n")
            weatherInfo.append("Sunset: $sunset")
        }

        // Return the complete weather information as a single string
        return weatherInfo.toString()
    }
}
