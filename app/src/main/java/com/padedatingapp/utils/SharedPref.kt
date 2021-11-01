package com.birimo.birimosports.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.padedatingapp.utils.AppConstants

class SharedPref(context: Application) {
    private val sharedpreferences: SharedPreferences =
        context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE)
    fun setString(name: String, value: String) {
        sharedpreferences.edit().putString(name, value).apply()
    }
    fun getString(name: String, s: String): String {
        return sharedpreferences.getString(name,"")!!
    }
    fun getLong(name:String): Long {
        return sharedpreferences.getLong(name,0)
    }
    fun setLong(name:String,long:Long){
        sharedpreferences.edit().putLong(name, long).apply()
    }

    fun getInt(name:String): Int {
        return sharedpreferences.getInt(name,0)
    }
    fun setInt(name:String,long:Int){
        sharedpreferences.edit().putInt(name, long).apply()
    }

    fun getBoolean(name: String): Boolean {
        return sharedpreferences.getBoolean(name, false)
    }

    fun setBoolean(name: String, value: Boolean) {
        sharedpreferences.edit().putBoolean(name, value).apply()
    }
    fun clear() {
        sharedpreferences.edit().clear().apply()
    }
}