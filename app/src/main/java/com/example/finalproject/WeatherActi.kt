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
    private lateinit var returnButton: ImageButton
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherInfoTextView: TextView

    private val apiKey = "A3WMALQVCSAEPZESUFHWVT34Y&contentType=json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // Initialize UI elements
        textView = findViewById(R.id.textView)
        button6 = findViewById(R.id.button6)
        returnButton = findViewById(R.id.returnButton)
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView)
        locationHelper = LocationHelper(this)

        // Get the passed date (if any), default to current date
        val passedDate: Date? = intent.getSerializableExtra("date") as Date?
        val currentDate = passedDate ?: Date()  // If no date is passed, use the current date

        // Format the date to string
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        locationHelper.getLocation { result, success ->
            if (success) {
                textView.text = "Location: $result"
                button6.visibility = Button.GONE

                val coordinates = result.split(",")
                if (coordinates.size == 2) {
                    val latitude = coordinates[0].toDouble()
                    val longitude = coordinates[1].toDouble()

                    // Fetch weather data with the formatted date
                    fetchWeatherData(latitude, longitude, formattedDate)
                }
            } else {
                textView.text = "Error: $result"
                button6.visibility = Button.VISIBLE

                button6.setOnClickListener {
                    recreate()
                }
            }
        }

        returnButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double, date: String) {
        val url =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$latitude,$longitude/$date?key=$apiKey"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val responseString = responseBody.string()

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
                        ).show()
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
        val jsonResponse = JSONObject(responseString)
        val description = jsonResponse.optString(
            "description",
            "No description available"
        ) // Default value if key is missing

        val weatherInfo = StringBuilder()
        weatherInfo.append("Description: $description\n\n")

        val days = jsonResponse.optJSONArray("days")
            ?: return weatherInfo.append("No daily weather data available.").toString()

        if (days.length() > 0) {
            val dayData =
                days.optJSONObject(0) ?: return weatherInfo.append("No valid day data available.")
                    .toString()

            val date = dayData.optString("datetime", "Unknown date")
            val tempMax = dayData.optDouble("tempmax", Double.NaN)
            val tempMin = dayData.optDouble("tempmin", Double.NaN)
            val currentTemp = dayData.optDouble("temp", Double.NaN)
            val humidity = dayData.optDouble("humidity", Double.NaN)
            val windSpeed = dayData.optDouble("windspeed", Double.NaN)
            val pressure = dayData.optDouble("pressure", Double.NaN)
            val conditions = dayData.optString("conditions", "Unknown conditions")
            val sunrise = dayData.optString("sunrise", "Unknown sunrise time")
            val sunset = dayData.optString("sunset", "Unknown sunset time")

            weatherInfo.append("Weather for $date:\n")
            weatherInfo.append("Max Temp: ${tempMax.ifNaN("N/A")}°F\n")
            weatherInfo.append("Min Temp: ${tempMin.ifNaN("N/A")}°F\n")
            weatherInfo.append("Current Temp: ${currentTemp.ifNaN("N/A")}°F\n")
            weatherInfo.append("Humidity: ${humidity.ifNaN("N/A")}%\n")
            weatherInfo.append("Wind Speed: ${windSpeed.ifNaN("N/A")} mph\n")
            weatherInfo.append("Pressure: ${pressure.ifNaN("N/A")} hPa\n")
            weatherInfo.append("Conditions: $conditions\n")
            weatherInfo.append("Sunrise: $sunrise\n")
            weatherInfo.append("Sunset: $sunset")
        }

        return weatherInfo.toString()
    }

    // Extension function to handle Double.NaN more cleanly
    private fun Double.ifNaN(defaultValue: String): String {
        return if (this.isNaN()) defaultValue else this.toString()
    }
}
