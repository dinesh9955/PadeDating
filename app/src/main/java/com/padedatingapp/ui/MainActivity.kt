package com.padedatingapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.padedatingapp.FCM.ReferrerReceiver
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


//        updateData();

        Handler().postDelayed({
            if (Utility.checkAndRequestPermissions(
                    this@MainActivity,
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            ) {
                //getLogin()
            } else {
            }
        }, 1000)


    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (Utility.checkAdditionPermissionsCheck(
                        this@MainActivity,
                        permissions as Array<String>, grantResults, REQUEST_ID_MULTIPLE_PERMISSIONS
                    )
                ) {
                    //getLogin()
                }
            }
        }
    }


    override fun onResume() {
        Log.e(TAG, "onResume1")
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mUpdateReceiver, IntentFilter(ReferrerReceiver.ACTION_UPDATE_DATA))
        updateData();

        super.onResume()
    }

    override fun onPause() {
        Log.e(TAG, "onPause1")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver)
        super.onPause()
    }


    private val mUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e(TAG, "mUpdateReceiver11 ")
            updateData()
        }
    }


    private fun updateData() {
//        boolean isReferrerDetected = Controller.isReferrerDetected(getApplicationContext());
//        String firstLaunch = Controller.getFirstLaunch(getApplicationContext());
//        String referrerDate = Controller.getReferrerDate(getApplicationContext());
        val referrerDataRaw: String? = PadeDatingApp.referrerFriend.getReferrerDataRaw(applicationContext)
        val referrerDataDecoded: String? = PadeDatingApp.referrerFriend.getReferrerDataDecoded(applicationContext)

//        StringBuilder sb = new StringBuilder();
//        sb.append("<b>First launch:</b>")
//                .append("<br/>")
//                .append(firstLaunch)
//                .append("<br/><br/>")
//                .append("<b>Referrer detection:</b>")
//                .append("<br/>")
//                .append(referrerDate);
//        if (isReferrerDetected) {
//            sb.append("<br/><br/>")
//                    .append("<b>Raw referrer:</b>")
//                    .append("<br/>")
//                    .append(referrerDataRaw);

//            if (referrerDataDecoded != null) {
//                sb.append("<br/><br/>")
//                        .append("<b>Decoded referrer:</b>")
//                        .append("<br/>")
//                        .append(referrerDataDecoded);
//            }
//        }

//		content.setText(Html.fromHtml(sb.toString()));
//		content.setMovementMethod(new LinkMovementMethod());
        Log.e(
            TAG,
            "SSSSSSSSSSSSSSSSS $referrerDataDecoded"
        )

//        pref.setRefferCode(Utility.isEmptyNull(referrerDataDecoded));


//        pref.setRefferCode("QuaiqFNBYn");
//        pref.setRefferCode("");
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(TAG, "onActivityResult " + requestCode + " " + resultCode)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}