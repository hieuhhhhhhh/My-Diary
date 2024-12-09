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
        val description = jsonResponse.getString("description")

        val weatherInfo = StringBuilder()
        weatherInfo.append("Description: $description\n\n")

        val days = jsonResponse.getJSONArray("days")
        if (days.length() > 0) {
            val dayData = days.getJSONObject(0)

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

        return weatherInfo.toString()
    }
}
