package com.example.finalproject

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class WeatherSearchFrag : Fragment(R.layout.fragment_weather_search) {

    private lateinit var selectedDateTextView: TextView
    private var selectedDate: Date? = null  // Store the selected date here

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener on btn 6:
        val goToActiButton: Button = view.findViewById(R.id.button4)
        goToActiButton.setOnClickListener {
            val intent = Intent(requireContext(), WeatherActi::class.java)

            // Pass the selected date to the next activity
            selectedDate?.let {
                intent.putExtra("selected_date", it)
            }

            startActivity(intent)
        }

        // Initialize the TextView to show selected date
        selectedDateTextView = view.findViewById(R.id.text_selected_date)

        // Set up the DatePicker button
        val datePickerButton: Button = view.findViewById(R.id.button_date_picker)
        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Create a Calendar instance with the selected date
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDate = calendar.time  // Save the selected date as a java.util.Date object

                // Update the TextView with the selected date
                val selectedDateString = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                selectedDateTextView.text = "Selected Date: $selectedDateString"
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
