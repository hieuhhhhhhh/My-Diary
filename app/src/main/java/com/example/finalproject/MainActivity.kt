package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init the bottom nav and entry frag
        initBottomNavigation()
        selectFrag(AddFrag())
        binding.bottomNav.selectedItemId = R.id.add

        // Check if user is already logged in
        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            // User is not logged in, start LoginAct
            startActivity(Intent(this, LoginAct::class.java))
            finish() // Close MainActivity so user cannot navigate back without logging in
        }

    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.bottomNav.selectedItemId == R.id.home) {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                } else {
                    binding.bottomNav.selectedItemId = R.id.home
                    selectFrag(HomeFrag())
                }
            }
        })
    }


    private fun selectFrag(fragment: Fragment) {
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun initBottomNavigation() {
        // Access the BottomNavigationView through the binding object
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    selectFrag(HomeFrag())
                    true
                }

                R.id.add -> {
                    selectFrag(AddFrag())
                    true
                }


                R.id.profile -> {
                    selectFrag(ProfileFrag()) // Assuming you want ProfileFrag here
                    true
                }

                R.id.weather -> {
                    selectFrag(WeatherSearchFrag()) // Assuming you want ProfileFrag here
                    true
                }

                else -> false
            }
        }
    }


}
