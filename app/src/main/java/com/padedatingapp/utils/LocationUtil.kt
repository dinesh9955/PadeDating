package com.padedatingapp.utils

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.padedatingapp.BuildConfig


fun Context.isGPSEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    Log.e("Location_manager", " $locationManager ")
    locationManager?.let {
        return locationManager.isProviderEnabled(LocationManager. GPS_PROVIDER)
    }
    return false
}

fun Context.isLocationNetworkProvider(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    locationManager?.let {
        return locationManager.isProviderEnabled(LocationManager. NETWORK_PROVIDER)
    }
    return false
}

fun openAppSettings(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun Context.getCountryCode(latitude: Double, longitude: Double) : String {
    var code = ""
    try {
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        //code = addresses!![0].getAddressLine(0)
        code = addresses!![0].countryCode
    } catch (e: Exception) {
        Log.e("exception", "location: $e")
    }
    return code
}

fun Context.getAddressList(latitude: Double, longitude: Double) : List<Address> {
    var address : List<Address> = ArrayList()
    try {
        val geocoder = Geocoder(this)
        address = geocoder.getFromLocation(latitude, longitude, 1)
       // address = addresses!![0].getAddressLine(0)
    } catch (e: Exception) {
        Log.e("exception", "location: $e")
    }
    return address
}
