package com.propertyonthespot.utils


import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

import android.text.format.Formatter
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson



import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AnaadIT on 3/9/2017.
 */

object Utility {
    val MY_PERMISSIONS_REQUEST = 123

    private val TAG = "Utility"


    val isDTTrue: Boolean
        get() {
            var b = false
            val date1 = date1
            val date2 = date2
            if (date1!!.after(date2)) {
                val `is` = Integer.parseInt("df")
                b = true
            }
            if (date1.before(date2)) {
                b = false
            }
            return b
        }

    private// TODO Auto-generated catch block
    val date1: Date?
        get() {
            val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.US)
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 0)
            val date = cal.time
            val date1 = dateFormatter.format(date)
            var inActiveDate: Date? = null
            try {
                inActiveDate = dateFormatter.parse(date1)
                return inActiveDate
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }

            return inActiveDate
        }

    private// TODO Auto-generated catch block
    val date2: Date?
        get() {
            val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.US)
            val date1 = "01/01/19"
            var inActiveDate: Date? = null
            try {
                inActiveDate = dateFormatter.parse(date1)
                return inActiveDate
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }

            return inActiveDate
        }


    val HOME_LOCATION_START = "HOME_LOCATION_START"
    val HOME_LOCATION_STOP = "HOME_LOCATION_STOP"


    val MAP_MARKER_LOCATION_START = "MAP_MARKER_LOCATION_START"
    val MAP_MARKER_LOCATION_STOP = "MAP_MARKER_LOCATION_STOP"



    val PROPERTY_DETAIL = "PROPERTY_DETAIL"
    val SUBSCRIBE_INFO = "SUBSCRIBE_INFO"

    val ADVERTISEMENT_2 = "ADVERTISEMENT_2"












    val USER_TYPE = "user_type"
    val ID = "id"

    val USERNAME = "user_login"


    val USER_NAME = "user_name"
    val ICON = "icon"
    val PRICE = "price"
    val RATING = "rating"
    val DATE = "date"


    val VO_PROFILE_DETAIL = "VO_PROFILE_DETAIL"
    val VO_PROFILE_DETAIL_TO_DETAIL = "VO_PROFILE_DETAIL_TO_DETAIL"
    val VO_MORE_SERVICES = "VO_MORE_SERVICES"
    val VO_HOME_SERVICE_DETAIL = "VO_HOME_SERVICE_DETAIL"
    val VO_HOME_SEASONAL_DETAIL = "VO_HOME_SEASONAL_DETAIL"
    val VO_HOME_SEASONAL_DETAIL_MAP = "VO_HOME_SEASONAL_DETAIL_MAP"
    val VO_HOME_FEATURED_DETAIL = "VO_HOME_FEATURED_DETAIL"
    val VO_HOME_FEATURED_DETAIL_MAP = "VO_HOME_FEATURED_DETAIL_MAP"
    val VO_HOME_MY_CAR_INFO = "VO_HOME_MY_CAR_INFO"
    val VO_HOME_MY_CAR = "VO_HOME_MY_CAR"
    val VO_HOME_MY_CAR_ADD_CAR = "VO_HOME_MY_CAR_ADD_CAR"
    val VO_HOME_MY_CAR_EDIT_CAR = "VO_HOME_MY_CAR_EDIT_CAR"
    val VO_ACTIVE_BIDS_REQUEST = "VO_ACTIVE_BIDS_REQUEST"
    val VO_ACTIVE_BIDS_SERVICE_DETAIL = "VO_ACTIVE_BIDS_SERVICE_DETAIL"
    val VO_ACTIVE_BIDS_REQUEST_DETAIL = "VO_ACTIVE_BIDS_REQUEST_DETAIL"
    val VO_ACTIVE_BIDS_SERVICE_DETAIL_RATE = "VO_ACTIVE_BIDS_SERVICE_DETAIL_RATE"
    val VO_EDIT_PROFILE = "VO_EDIT_PROFILE"
    val VO_CHANGE_PASSWORD = "VO_CHANGE_PASSWORD"
    val VO_ADD_INSURANCE_INFO = "VO_ADD_INSURANCE_INFO"
    val VO_HOME_SERVICE_MAP_DETAIL_SEASONAL_DETAIL = "VO_HOME_SERVICE_MAP_DETAIL_SEASONAL_DETAIL"
    val VO_HOME_SERVICE_MAP_DETAIL_SEASONAL_MORE = "VO_HOME_SERVICE_MAP_DETAIL_SEASONAL_MORE"
    val VO_HOME_ACCIDENT_INSURANCE_INFO = "VO_HOME_ACCIDENT_INSURANCE_INFO"
    val VO_HISTORY_DETAIL = "VO_HISTORY_DETAIL"
    val VO_CHOOSE_LOCATION = "VO_CHOOSE_LOCATION"
    val VO_EDIT_LOCATION = "VO_EDIT_LOCATION"
    val VO_HOME = "VO_HOME"
    val VO_PROFILE_LOCATION = "VO_PROFILE_LOCATION"
    val VO_PENDING_JOB_DETAIL = "VO_PENDING_JOB_DETAIL"
    val VO_ACTIVE_BID_DETAIL = "VO_ACTIVE_BID_DETAIL"
    val VO_HOME_MY_CAR_ADD_CAR_INSURANCE = "VO_HOME_MY_CAR_ADD_CAR_INSURANCE"

    val VO_HOME_MY_CAR_EDIT_INSURANCE = "VO_HOME_MY_CAR_EDIT_INSURANCE"


    val VO_PROFILE_MY_INSURANCE = "VO_PROFILE_MY_INSURANCE"

    val VO_MC_REQUEST_DETAIL = "VO_MC_REQUEST_DETAIL"


    val MC_HOME = "MC_HOME"
    val IC_HOME = "IC_HOME"


    val VO_IC_REGISTER_WITH_QUAIQ = "VO_IC_REGISTER_WITH_QUAIQ"
    val VO_DOES_QUAIQ_HAVE = "VO_DOES_QUAIQ_HAVE"
    val VO_IC_COMP_CALL = "VO_IC_COMP_CALL"
    val VO_IC_CALL_MC = "VO_IC_CALL_MC"
    val VO_IC_START_BID = "VO_IC_START_BID"
    val VO_SEND_ACCIDENT_INFO = "VO_SEND_ACCIDENT_INFO"

    val VO_MC_DETAIL = "VO_MC_DETAIL"
    val VO_MC_MAP = "VO_MC_MAP"


    val VO_CHOOSE_LOCATION_START = "VO_CHOOSE_LOCATION_START"
    val VO_CHOOSE_LOCATION_STOP = "VO_CHOOSE_LOCATION_STOP"


    val VO_MC_PROFILE_START = "VO_MC_PROFILE_START"
    val VO_MC_PROFILE_STOP = "VO_MC_PROFILE_STOP"


    val VO_REFRESH_ACTIVE_BID_START = "VO_REFRESH_ACTIVE_BID_START"
    val VO_REFRESH_ACTIVE_BID_STOP = "VO_REFRESH_ACTIVE_BID_STOP"


    val NOTIFICATION_START = "NOTIFICATION_START"
    val NOTIFICATION_STOP = "NOTIFICATION_STOP"


    val VO_LOCATION_START = "VO_LOCATION_START"
    val VO_LOCATION_STOP = "VO_LOCATION_STOP"


    val VO_VEHICLE_LOCATION_START = "VO_VEHICLE_LOCATION_START"
    val VO_VEHICLE_LOCATION_STOP = "VO_VEHICLE_LOCATION_STOP"

    val VO_MC_COMPANY_INFO_LOCATION_START = "VO_MC_COMPANY_INFO_LOCATION_START"
    val VO_MC_COMPANY_INFO_LOCATION_STOP = "VO_MC_COMPANY_INFO_LOCATION_STOP"


    val IC_COMPANY_INFO_LOCATION_START = "IC_COMPANY_INFO_LOCATION_START"
    val IC_COMPANY_INFO_LOCATION_STOP = "IC_COMPANY_INFO_LOCATION_STOP"


    val VO_MAIN_ACTIVITY_LOCATION_START = "VO_MAIN_ACTIVITY_LOCATION_START"
    val VO_MAIN_ACTIVITY_LOCATION_STOP = "VO_MAIN_ACTIVITY_LOCATION_STOP"


    val MC_SERVICE_INFO_LOCATION_START = "MC_SERVICE_INFO_LOCATION_START"
    val MC_SERVICE_INFO_LOCATION_STOP = "MC_SERVICE_INFO_LOCATION_STOP"

    val MC_MAIN_ACTIVITY_LOCATION_START = "MC_MAIN_ACTIVITY_LOCATION_START"
    val MC_MAIN_ACTIVITY_LOCATION_STOP = "MC_MAIN_ACTIVITY_LOCATION_STOP"


    val IC_SERVICE_INFO_LOCATION_START = "IC_SERVICE_INFO_LOCATION_START"
    val IC_SERVICE_INFO_LOCATION_STOP = "IC_SERVICE_INFO_LOCATION_STOP"


    val IC_MAIN_ACTIVITY_LOCATION_START = "IC_MAIN_ACTIVITY_LOCATION_START"
    val IC_MAIN_ACTIVITY_LOCATION_STOP = "IC_MAIN_ACTIVITY_LOCATION_STOP"


    val VO_MY_CARS = "VO_MY_CARS"
    val VO_CHAT = "VO_CHAT"


    val MC_HISTORY_DETAIL = "MC_HISTORY_DETAIL"
    val MC_EDIT_PROFILE = "MC_EDIT_PROFILE"
    val MC_EDIT_BUSSINESS = "MC_EDIT_BUSSINESS"
    val MC_CHANGE_PASSWORD = "MC_CHANGE_PASSWORD"
    val MC_BROWSE_DETAIL = "MC_BROWSE_DETAIL"
    val MC_ACTIVE_BID_DETAIL = "MC_ACTIVE_BID_DETAIL"
    val MC_PENDING_JOB_DETAIL = "MC_PENDING_JOB_DETAIL"
    val MC_SUBSCRIBE = "MC_SUBSCRIBE"
    val MC_CHANGE_SERVICES = "MC_CHANGE_SERVICES"
    val MC_PENDING_JOB_DETAIL_CHANGE_AMOUNT = "MC_PENDING_JOB_DETAIL_CHANGE_AMOUNT"

    val VO_ADVERTICEMENT_TYPE1 = "VO_ADVERTICEMENT_TYPE1"
    val MC_ADVERTICEMENT_TYPE1 = "MC_ADVERTICEMENT_TYPE1"

    val MC_ADVERTICEMENT_ADD = "MC_ADVERTICEMENT_ADD"
    val MC_ADVERTICEMENT_VIEW = "MC_ADVERTICEMENT_VIEW"

    val VO_ADVERTICEMENT_ADD = "VO_ADVERTICEMENT_ADD"
    val VO_ADVERTICEMENT_VIEW = "VO_ADVERTICEMENT_VIEW"


    val MC_BID_HIGH_LOW = "MC_BID_HIGH_LOW"

    val MC_SUBSCRIBE_DETAIL = "MC_SUBSCRIBE_DETAIL"
    val MC_SUBSCRIBE_CARD_DETAIL = "MC_SUBSCRIBE_CARD_DETAIL"


    val MC_ADVERTICEMENT_EDIT_1 = "MC_ADVERTICEMENT_EDIT_1"
    val MC_ADVERTICEMENT_EDIT_2 = "MC_ADVERTICEMENT_EDIT_2"
    val MC_ADVERTICEMENT_EDIT_3 = "MC_ADVERTICEMENT_EDIT_3"
    val MC_ADVERTICEMENT_EDIT_4 = "MC_ADVERTICEMENT_EDIT_4"


    val IC_HISTORY_DETAIL = "IC_HISTORY_DETAIL"
    val IC_EDIT_PROFILE = "IC_EDIT_PROFILE"
    val IC_EDIT_BUSSINESS = "IC_EDIT_BUSSINESS"
    val IC_CHANGE_PASSWORD = "IC_CHANGE_PASSWORD"
    val IC_CLAIM_ACTIONABLE_DETAIL = "IC_CLAIM_ACTIONABLE_DETAIL"
    val IC_CLAIM_NON_ACTIONABLE_DETAIL = "IC_CLAIM_NON_ACTIONABLE_DETAIL"
    val IC_ACTIVE_BIDS_DETAIL = "IC_ACTIVE_BIDS_DETAIL"
    val IC_WORK_IN_PROGRESS_DETAIL = "IC_WORK_IN_PROGRESS_DETAIL"
    val IC_TYPE_EDIT = "IC_TYPE_EDIT"

    val CHAT_LIST = "CHAT_LIST"


    val VO_ACTIVE_BIDS = "VO_ACTIVE_BIDS"
    val VO_PENDING_JOBS = "VO_PENDING_JOBS"


    val SEARCH_VO = "SEARCH_VO"
    val SEARCH_MC = "SEARCH_MC"
    val SEARCH_IC = "SEARCH_IC"


    val BUSSINESS_NAME = "business_name"
    val BUSSINESS_NO = "business_number"
    val BUSSINESS_ADDR = "business_address"
    val LATITUDE = "latitude"
    val LONGITUDE = "longitude"
    val MESSAGE = "msg"
    val FIRSTNAME = "first_name"
    val LASTNAME = "last_name"

    val USER_ID = "user_id"
    val MOBILE = "mobile"
    val PHONE = "phone"
    val MAP = "map"
    val TYPE = "user_type"
    val ADDRESS = "address"
    val EMAIL = "user_email"

    val PASSWORD = "user_password"
    val VEHICLE_VIN = "vinnumber"
    //    public static final String VEHICLENAME="v_name";
    val VEHICLE_YEAR = "v_year"
    val VEHICLEMAKE = "v_make"
    val VEHICLEMODEL = "v_model"
    val FEATURE = "feature"

    val TIRESIZE = "v_tyre_size"


    val DISTANCE = "distance"
    val NAME = "name"

    val BS_TITLE = "bs_title"
    val INSURANCE_NAME = "insurance_name"
    val INSURANCE_AMOUNT = "insurance_amount"
    val SESSION_KEY = "csrf_quaiq_insurance"


    // Image and Video file extensions
    val IMAGE_EXTENSION = "jpg"
    val VIDEO_EXTENSION = "mp4"
    // Gallery directory name to store the images or videos
    val GALLERY_DIRECTORY_NAME = "Hello Camera"

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2


    val REQUEST_LOCATION = 11
    val REQUEST_LOCATION_ONE_TIME = 1
    val REQUEST_LOCATION_MULTI_TIME = 2


    val FIXED_KEY = "csrf_quaiq_insurance"
    val FIXED_VALUE = "30d3b38c74732eb0a2e3046d01d3d4d1"

    val VO = "VO"
    val MC = "MC"
    val IC = "IC"


    val LOGIN_TYPE = "type"

    val VIN = "vin"

    val BS_TYPE = "bs_type"
    val BRAND = "brand"
    val SERVICE = "service"

    val IP_ADDRESS = "ipaddress"


    val VO_SERVICE_ID = "service_id"

    val VO_GET_VEHICLE_DETAIL = "VO_GET_VEHICLE_DETAIL"

    val VO_MY_CAR_TO_VO_HOME_SERVICES_DETAIL = "VO_MY_CAR_TO_VO_HOME_SERVICES_DETAIL"

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!, permission)) {
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.GET_ACCOUNTS
                            ),
                            MY_PERMISSIONS_REQUEST
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.GET_ACCOUNTS
                            ),
                            MY_PERMISSIONS_REQUEST
                        )
                    }
                    return false
                }
            }
        }
        return true
    }


    fun checkAndRequestPermissions(context: Activity, request: Int): Boolean {
        val camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val writeExternalStorage =
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        val coarseLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val callPhone = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        val readContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        val recordAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)


        val listPermissionsNeeded = ArrayList<String>()

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (coarseLocartion != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (fineLocartion != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (callPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
        }
        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded.toTypedArray
                    (), request
            )
            return false
        }
        return true
    }


    fun checkAdditionPermissionsCheck(
        splashScreen: Activity,
        permissions: Array<String>,
        grantResults: IntArray,
        REQUEST_ID_MULTIPLE_PERMISSIONS: Int
    ): Boolean {

        val perms = HashMap<String, Int>()

        perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.ACCESS_COARSE_LOCATION] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.CALL_PHONE] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.READ_CONTACTS] = PackageManager.PERMISSION_GRANTED
        perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED

        if (grantResults.size > 0) {
            for (i in permissions.indices)
                perms[permissions[i]] = grantResults[i]
            // Check for both permissions
            if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.CALL_PHONE] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.READ_CONTACTS] == PackageManager.PERMISSION_GRANTED &&
                perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED
            ) {
                //   Log.d(TAG, "Please allow access to all asked permissions. Due to the nature of the friendlywagon app, access to these areas on your mobile devices are necessary  for the app to function properly.");
                // process the normal flow
                //else any one or both the permissions are not granted
                //                Toast.makeText(splashScreen, "CCCCCCCCCc", Toast.LENGTH_LONG)
                //                        .show();
                return true
            } else {
                //  Log.d(TAG, "Please allow access to all asked permissions. Due to the nature of the friendlywagon app, access to these areas on your mobile devices are necessary  for the app to function properly.");
                if (ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.CALL_PHONE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen,
                        Manifest.permission.READ_CONTACTS
                    ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        splashScreen, Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    //                    showDialogOK(splashScreen, "Please allow access to all asked permissions, access to your mobile devices are necessary for the app to function properly.",
                    showDialogOK(splashScreen, "Please allow access to all asked permissions.",
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions(
                                    splashScreen,
                                    REQUEST_ID_MULTIPLE_PERMISSIONS
                                )
                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        })
                } else {
                    return true
                    //                    Toast.makeText(splashScreen, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                    //                            .show();
                }
            }
        } else {

        }


        return false
    }


    private fun showDialogOK(splashScreen: Activity, message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(splashScreen)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }


    fun checkPermissionsCheck(splashScreen: Activity): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                splashScreen,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else false
    }


    fun isEmptyNull(address: String?): String {
        return if (address != null && !address.isEmpty() && !address.equals("null", ignoreCase = true)) {
            address
        } else {
            ""
        }
    }







//    fun getImageLoaderNoImage(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.no_media)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        //        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderDefaultCar(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.iconfinder_car)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        //        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderUser(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        //        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderDriver(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        //        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderCornerRound(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        roundedBitmapDrawable.cornerRadius = 20.0f
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoader(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        roundedBitmapDrawable.cornerRadius = 20.0f
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                //                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(RoundedBitmapDisplayer(20))
//                //                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderRoundStrech(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
//            mResources,
//            mBitmap
//        )
//        //        roundedBitmapDrawable.setCornerRadius(20.0f);
//        roundedBitmapDrawable.isCircular = true
//        roundedBitmapDrawable.setAntiAlias(true)
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(roundedBitmapDrawable)
//                .showImageForEmptyUri(roundedBitmapDrawable)
//                .showImageOnFail(roundedBitmapDrawable)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                .displayer(CircleBitmapDisplayer(context.resources.getColor(R.color.colorPrimary), 1))
//
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }
//
//
//    fun getImageLoaderRoundCrop(context: Activity): HashMap<String, Any> {
//        val loaderHashMap = HashMap<String, Any>()
//
//        val mResources = context.resources
//        val mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.logo_splash)
//
//        val cd = CroppedDrawable(mBitmap)
//        // Bitmap ddd = drawableToBitmap(cd);
//
//
//        var imageLoader: ImageLoader? = null
//        var options: DisplayImageOptions? = null
//
//        try {
//            imageLoader = ImageLoader.getInstance()
//
//            imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
//            options = DisplayImageOptions.Builder()
//                .showImageOnLoading(cd)
//                .showImageForEmptyUri(cd)
//                .showImageOnFail(cd)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                //                    .displayer(new RoundedBitmapDisplayer(20))
//                .displayer(CircleBitmapDisplayer(context.resources.getColor(R.color.colorPrimary), 1))
//                .build()
//        } catch (e: Exception) {
//            Log.d(TAG, "myError11: " + e.message)
//        }
//
//        loaderHashMap["imageLoader"] = imageLoader
//        loaderHashMap["options"] = options
//
//        return loaderHashMap
//    }


    fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap =
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }



    @SuppressLint("LongLogTag")
    fun getCompleteAddressString(activity: Activity, locationAA: Location): String {
        var strAdd = ""
        val geocoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(locationAA.latitude, locationAA.longitude, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("My Current loction address", strReturnedAddress.toString())
            } else {
                Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("My Current loction address", "Canont get Address!")
        }

        return strAdd
    }


    fun setAddress(activity: Activity, locationAA: Location): String {
        val buffer = StringBuffer()
        try {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(activity, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                locationAA.latitude,
                locationAA.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address2 = addresses[0].subLocality

            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName


            if (address2 != null) {
                //editTextSAddress.setText(""+address2);
                buffer.append("$address2,")
            }
            if (city != null) {
                //                editTextAddress.setText(""+city);
                buffer.append("$city,")
            }
            if (state != null) {
                //                editTextState.setText(""+state);
                buffer.append("$state,")
            }
            if (postalCode != null) {
                //                editTextZipcode.setText(""+postalCode);
                buffer.append("$postalCode,")
            }
            if (country != null) {
                //                editTextZipcode.setText(""+postalCode);
                buffer.append(country + "")
            }
            return buffer.toString()
        } catch (e: Exception) {
        }

        return buffer.toString()
    }

    fun getStringFromGson(responseCode: String): String {
        var jsonObjectInStringSub = ""
        val gson = Gson()
        if (responseCode.length > 0) {
            if (responseCode.length > 1) {
                val jsonObjectInString = gson.toJson(responseCode).replace("\\", "")
                jsonObjectInStringSub = jsonObjectInString.substring(1, jsonObjectInString.length - 1)
                return jsonObjectInStringSub
            }
        }
        return jsonObjectInStringSub
    }


    private fun isValid(urlString: String): Boolean {
        try {
            // URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (e: Exception) {

        }

        return false
    }





    class CroppedDrawable(b: Bitmap) : BitmapDrawable(b) {
        private val p = Path()

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)

            p.rewind()
            p.addCircle(
                (bounds.width() / 2).toFloat(),
                (bounds.height() / 2).toFloat(),
                (Math.min(bounds.width(), bounds.height()) / 2).toFloat(),
                Path.Direction.CW
            )
        }

        override fun draw(canvas: Canvas) {
            canvas.clipPath(p)
            super.draw(canvas)
        }
    }





    object BitmapTool {
        fun toOvalBitmap(bitmap: Bitmap?): Bitmap? {
            if (bitmap != null) {
                val output = Bitmap.createBitmap(bitmap.height, bitmap.width, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(output)
                val paint = Paint()
                paint.isAntiAlias = true
                val rect = Rect(0, 0, bitmap.width, bitmap.height)
                val rectF = RectF(rect)

                canvas.drawOval(rectF, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(bitmap, rect, rectF, paint)

                return output
            }

            return null
        }
    }


    fun convertValueStringToDouble(valueFirst: String): String {
        try {
            val time = java.lang.Double.parseDouble(valueFirst)
            val df = DecimalFormat("0.00")
            //DecimalFormat df = new DecimalFormat("##.##");
            return "" + df.format(time)
        } catch (e: Exception) {

        }

        return "0.0"
    }


    //
    //
    //    public ArrayList<ItemLeftMenu> getItemLeftMenuWithoutLoginUser(Activity activity) {
    //        ArrayList<ItemLeftMenu> arrayList = new ArrayList<ItemLeftMenu>();
    //
    //        ItemLeftMenu vehicle1 = new ItemLeftMenu();
    //        vehicle1.setName(activity.getString(R.string.home));
    //        vehicle1.setIcon(R.drawable.home);
    //        arrayList.add(vehicle1);
    //
    //        ItemLeftMenu vehicle11 = new ItemLeftMenu();
    //        vehicle11.setName(activity.getString(R.string.booking));
    //        vehicle11.setIcon(R.drawable.open_book);
    //        arrayList.add(vehicle11);
    //
    //
    //        ItemLeftMenu vehicle2 = new ItemLeftMenu();
    //        vehicle2.setName(activity.getString(R.string.send_request));
    //        vehicle2.setIcon(R.drawable.history);
    //        arrayList.add(vehicle2);
    //
    //
    //        ItemLeftMenu vehicle22 = new ItemLeftMenu();
    //        vehicle22.setName(activity.getString(R.string.payment_history));
    //        vehicle22.setIcon(R.drawable.payment_history);
    //        arrayList.add(vehicle22);
    //
    //
    //
    //        ItemLeftMenu vehicle221 = new ItemLeftMenu();
    //        vehicle221.setName(activity.getString(R.string.settings));
    //        vehicle221.setIcon(R.drawable.settings);
    //        arrayList.add(vehicle221);
    //
    //
    //
    //        ItemLeftMenu vehicle22s = new ItemLeftMenu();
    //        vehicle22s.setName(activity.getString(R.string.logout));
    //        vehicle22s.setIcon(R.drawable.logout);
    //        arrayList.add(vehicle22s);
    //
    //        return arrayList;
    //    }
    //
    //
    //
    //    public ArrayList<ItemSettings> getItemSettings(Activity activity) {
    //        ArrayList<ItemSettings> arrayList = new ArrayList<ItemSettings>();
    //
    //        ItemSettings vehicle1 = new ItemSettings();
    //        vehicle1.setName(activity.getString(R.string.available));
    //        arrayList.add(vehicle1);
    //
    //        ItemSettings vehicle11 = new ItemSettings();
    //        vehicle11.setName(activity.getString(R.string.edit_profile));
    //        arrayList.add(vehicle11);
    //
    ////        ItemSettings vehicle2 = new ItemSettings();
    ////        vehicle2.setName(activity.getString(R.string.payment_history));
    ////        arrayList.add(vehicle2);
    //
    //
    ////        ItemSettings vehicle2 = new ItemSettings();
    ////        vehicle2.setName(activity.getString(R.string.change_password));
    ////        arrayList.add(vehicle2);
    //
    //        ItemSettings vehicle221 = new ItemSettings();
    //        vehicle221.setName(activity.getString(R.string.contact));
    //        arrayList.add(vehicle221);
    //
    //
    //        ItemSettings vehicle22 = new ItemSettings();
    //        vehicle22.setName(activity.getString(R.string.terms_conditions));
    //        arrayList.add(vehicle22);
    //
    //
    //        ItemSettings vehicle2234 = new ItemSettings();
    //        vehicle2234.setName(activity.getString(R.string.privacy_policy));
    //        arrayList.add(vehicle2234);
    //
    //
    //
    //        return arrayList;
    //    }
    //
    //
    //
    //
    //
    //
    //
    //    public static void setFontCrimsonRoman(Activity activity, TextView appBarTextView ) {
    //        appBarTextView.setTypeface(EasyFontsCustom.crimsonRoman(activity));
    //    }


    fun isRunning(ctx: Context): Boolean {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(Integer.MAX_VALUE)
        for (task in tasks) {
            if (ctx.packageName.equals(task.baseActivity?.packageName, ignoreCase = true))
                return true
        }
        return false
    }


    fun getPath(activity: Activity, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = activity.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }


    fun getImagePath(activity: Activity, selectedImage: Uri): String {
        // TODO Auto-generated method stub
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity.contentResolver.query(
            selectedImage,
            filePathColumn, null, null, null
        )
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        Log.d(TAG, "path is :: $picturePath")
        return picturePath
    }


    fun getImagePathCamera(activity: Activity, selectedImage: Intent): String {
        // TODO Auto-generated method stub
        val thumbnail = selectedImage.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())

            val imagePath1 = destination.toString()

            fo.close()

            return imagePath1

            //imageLoader.displayImage("file:///"+imagePath1, imageViewDriverProfile, options);

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }




    fun dialPhoneNumber(activity: Activity, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        }
    }



    fun dialPhoneNumber(activity: Activity) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + 35435345)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        }
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }


    @SuppressLint("LongLogTag")
    fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        var address: List<Address>? = ArrayList()
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            Log.e(TAG, "address111 $address")

            if (address.size > 0) {
                val location = address[0]
                location.latitude
                location.longitude
                p1 = LatLng(location.latitude, location.longitude)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            Log.e(TAG, "getMessage111 " + ex.message)
        }

        return p1
    }


    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val ip = Formatter.formatIpAddress(inetAddress.hashCode())
                        Log.i(TAG, "***** IP=$ip")
                        return ip
                    }
                }
            }
        } catch (ex: SocketException) {
            Log.e(TAG, ex.toString())
        }

        return null
    }


}


