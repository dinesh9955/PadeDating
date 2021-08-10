package com.padedatingapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.propertyonthespot.utils.Utility

class MainActivity : AppCompatActivity() {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1999


    companion object{
        var TAG   = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        Handler().postDelayed({
            if (Utility.checkAndRequestPermissions(this@MainActivity, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                //getLogin()
            } else {
            }
        }, 1000)


    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (Utility.checkAdditionPermissionsCheck(this@MainActivity,
                        permissions as Array<String>, grantResults, REQUEST_ID_MULTIPLE_PERMISSIONS)) {
                    //getLogin()
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(TAG , "onActivityResult "+requestCode+ " "+resultCode)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}