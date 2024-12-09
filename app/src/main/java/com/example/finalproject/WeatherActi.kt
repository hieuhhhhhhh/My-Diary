package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WeatherActi : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button6: Button
    private lateinit var returnButton: ImageButton // Reference for the return button
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherInfoTextView: TextView

    // Your Visual Crossing API key
    private val apiKey = "A3WMALQVCSAEPZESUFHWVT34Y&contentType=json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // Initialize UI elements
        textView = findViewById(R.id.textView)
        button6 = findViewById(R.id.button6)  // Reference to the Refresh button
        returnButton = findViewById(R.id.returnButton) // Reference to the return button
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView)  // Weather info text view
        locationHelper = LocationHelper(this)

        locationHelper.getLocation { result, success ->
            if (success) {
                // On success, display the location and fetch the weather
                textView.text = "Location: $result"
                button6.visibility = Button.GONE  // Hide button on success

                // Extract latitude and longitude from the result (assuming result is in "lat, long" format)
                val coordinates = result.split(",")
                if (coordinates.size == 2) {
                    val latitude = coordinates[0].toDouble()
                    val longitude = coordinates[1].toDouble()

                    // Fetch weather data
                    val currentDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    fetchWeatherData(latitude, longitude, currentDate)
                }
            } else {
                // Failure case
                textView.text = "Error: $result"
                button6.visibility = Button.VISIBLE  // Show button on failure

                // Set up click listener to reload the activity on failure
                button6.setOnClickListener {
                    recreate()  // This will recreate the activity and try the location request again
                }
            }
        }

        // Set up return button to finish the activity when clicked
        returnButton.setOnClickListener {
            finish() // Close the activity and return to the previous one
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double, date: String) {
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
                        runOnUiThread {
                            weatherInfoTextView.text = displayWeatherData(responseString)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@WeatherActi,
                            "Failed to fetch weather data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@WeatherActi,
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
        val description = jsonResponse.getString("description")

        // Initialize a string builder to construct the weather information string
        val weatherInfo = StringBuilder()

        // Add basic information
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.onRequestPermissionsResult(requestCode, grantResults) { result, success ->
            if (success) {
                // On success, display the location and fetch the weather
                textView.text = "Location: $result"
                button6.visibility = Button.GONE  // Hide button on success

                // Extract latitude and longitude from the result (assuming result is in "lat, long" format)
                val coordinates = result.split(",")
                if (coordinates.size == 2) {
                    val latitude = coordinates[0].toDouble()
                    val longitude = coordinates[1].toDouble()

                    // Fetch weather data
                    val currentDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    fetchWeatherData(latitude, longitude, currentDate)
                }
            } else {
                // Failure case
                textView.text = "Error: $result"
                button6.visibility = Button.VISIBLE  // Show button on failure

                // Set up click listener to reload the activity on failure
                button6.setOnClickListener {
                    recreate()  // This will recreate the activity and try the location request again
                }
            }
        }
    }
}
