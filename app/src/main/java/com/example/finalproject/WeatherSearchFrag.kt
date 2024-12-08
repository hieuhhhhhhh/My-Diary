package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class WeatherSearchFrag : Fragment(R.layout.fragment_weather_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goToActiButton: Button = view.findViewById(R.id.button4)
        goToActiButton.setOnClickListener {
            val intent = Intent(requireContext(), WeatherActi::class.java)
            startActivity(intent)
        }
    }
}