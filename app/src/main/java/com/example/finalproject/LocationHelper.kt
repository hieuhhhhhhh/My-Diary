package com.example.finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHelper(private val activity: AppCompatActivity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    fun getLocation(callback: (String) -> Unit) {
        if (checkLocationPermission()) {
            if (isLocationServiceEnabled()) {
                getLastLocation(callback)
            } else {
                callback("Location services are disabled. Please turn them on then try again")
                promptEnableLocationService()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun isLocationServiceEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLastLocation(callback: (String) -> Unit) {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    callback("$lat, $long")
                } else {
                    callback("Unable to get location")
                }
            }
        } else {
            callback("Location permission not granted")
        }
    }

    private fun promptEnableLocationService() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        callback: (String) -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isLocationServiceEnabled()) {
                    getLastLocation(callback)
                } else {
                    callback("Location services are disabled. Please turn them on then try again.")
                    promptEnableLocationService()
                }
            } else {
                callback("Location permission not granted")
            }
        }
    }
}
