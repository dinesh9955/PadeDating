package com.padedatingapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.fxn.OnBubbleClickListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.padedatingapp.SavePref
import com.padedatingapp.base.DataBindingActivity
import com.padedatingapp.databinding.ActivityHomeBinding
import com.padedatingapp.extensions.onNavDestinationSelected
import com.padedatingapp.model.CallData
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
import com.padedatingapp.ui.call.AudioCallActivity
import com.padedatingapp.ui.call.VideoCallActivity2
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.padedatingapp.ui.main.fragments.MeetMeFragment
import com.padedatingapp.utils.*
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class HomeActivity : DataBindingActivity<ActivityHomeBinding>() {

    companion object{
        var TAG = "HomeActivity"
    }
    private val sharedPref by inject<SharedPref>()

    var locationRepeat = 0


    var primaryBaseActivity //THIS WILL KEEP ORIGINAL INSTANCE
            : Context? = null

    lateinit var locationStatus: Emitter.Listener

    override fun layoutId(): Int = R.layout.activity_home


    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    // private var geocoder: Geocoder? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mApplication: PadeDatingApp = applicationContext as PadeDatingApp
        mApplication.initializeSocket(applicationContext)

        initComponents()

        AppSocketListener.getInstance().restartSocket()
    }

    private fun initComponents() {
        val navController = findNavController(R.id.nav_host_fragment_home)
        viewBinding.bottomMenu.setSelected(1)
        viewBinding.bottomMenu.setSelected(0)
        viewBinding.bottomMenu.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                viewBinding.bottomMenu.onNavDestinationSelected(id, navController)
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == bottom_menu[0].id ||
                destination.id == bottom_menu[1].id ||
                destination.id == bottom_menu[2].id ||
                destination.id == bottom_menu[3].id){
                viewBinding.bottomMenu.isVisible = true
                viewBinding.bottomMenu.setSelectedWithId(destination.id, false)
            }
            else viewBinding.bottomMenu.isVisible = false
           // destination.displayName ==  "com.padedatingapp:id/editProfileFragment"
        }


      //  viewBinding.bottomMenu.setSelectedWithId(bottom_menu[2].id, false)



        val bundle = intent.extras
        if (bundle != null) {
            val res = bundle.getString("key")
            val jsonObject = JSONObject(res)
            Log.e(TAG, "jsonObjectAA " + jsonObject)
            val type: String = jsonObject.getString("type")
            Log.e(TAG, "typeAA " + type)

            if (type.equals("TEXT_CHAT", ignoreCase = true)) {
                val chatIDModel = ChatIDModel()

                //  val jsonObjectUser2: JSONObject = jsonObject.getJSONObject("sentTo")

                val jsonObjectUser2 = JSONObject(jsonObject.getString("sentBy"))

                Log.e(TAG, "jsonObjectUser2AA " + jsonObjectUser2)

                chatIDModel.type = type
                chatIDModel.receiverID = jsonObjectUser2.getString("_id")
                chatIDModel.receiverName = jsonObjectUser2.getString("firstName")+" "+jsonObjectUser2.getString(
                        "lastName"
                )
                chatIDModel.receiverImage = jsonObjectUser2.getString("image")

                val bundle2 = Bundle()
                bundle2.putSerializable("meetMeModelChat", chatIDModel)

                var chat = ChatFragment()
                chat.arguments = bundle2

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.constraintLayout_home, chat)
                ft.disallowAddToBackStack();
                ft.commit()
            }

            if (type.equals("VIDEO_CALL", ignoreCase = true)) {
                val callData2 = JSONObject(jsonObject.getString("callData"))
                val user1 = JSONObject(jsonObject.getString("user1"))
                val user2 = JSONObject(jsonObject.getString("user2"))
                val dataCallData = CallData()
                dataCallData.apikey = callData2.getString("apikey")
                dataCallData.sessionId = callData2.getString("sessionId")
                dataCallData.token = callData2.getString("token")
                dataCallData.user1FirstName = user1.getString("firstName")
                dataCallData.user1LastName = user1.getString("lastName")
                dataCallData.user1Image = user1.getString("image")
                dataCallData.senderID = user1.getString("_id")
                dataCallData.user2FirstName = user2.getString("firstName")
                dataCallData.user2LastName = user2.getString("lastName")
                dataCallData.user2Image = user2.getString("image")
                dataCallData.receiverID = user2.getString("_id")

//                Log.e(TAG, "_idAA "+user1.getString("_id"))
//                Log.e(TAG, "_idBB "+user2.getString("_id"))

//                val gson = Gson()
//                val topic = gson.fromJson(jsonObject.toString(), CallUser::class.java)
//                val topicData = topic.data
                dataCallData.callType = "video"
                dataCallData.callFrom = "notification"
                var intent = Intent(this@HomeActivity, VideoCallActivity2::class.java)
                var bundle = Bundle()
                bundle.putSerializable("key", dataCallData);
                intent.putExtras(bundle)
                startActivity(intent)


            }

            if (type.equals("AUDIO_CALL", ignoreCase = true)) {
                val callData2 = JSONObject(jsonObject.getString("callData"))
                val user1 = JSONObject(jsonObject.getString("user1"))
                val user2 = JSONObject(jsonObject.getString("user2"))
                val dataCallData = CallData()
                dataCallData.apikey = callData2.getString("apikey")
                dataCallData.sessionId = callData2.getString("sessionId")
                dataCallData.token = callData2.getString("token")
                dataCallData.user1FirstName = user1.getString("firstName")
                dataCallData.user1LastName = user1.getString("lastName")
                dataCallData.user1Image = user1.getString("image")
                dataCallData.senderID = user1.getString("_id")
                dataCallData.user2FirstName = user2.getString("firstName")
                dataCallData.user2LastName = user2.getString("lastName")
                dataCallData.user2Image = user2.getString("image")
                dataCallData.receiverID = user2.getString("_id")

//                val gson = Gson()
//                val topic = gson.fromJson(jsonObject.toString(), CallUser::class.java)
//                val topicData = topic.data
                dataCallData.callType = "audio"
                dataCallData.callFrom = "notification"
                var intent = Intent(this@HomeActivity, AudioCallActivity::class.java)
                var bundle = Bundle()
                bundle.putSerializable("key", dataCallData);
                intent.putExtras(bundle)
                startActivity(intent)
            }


           // viewBinding.bottomMenu.setSelectedWithId(bottom_menu[2].id, false)


            //viewBinding.bottomMenu.setSelectedWithId(bottom_menu[2].id, false)


            //navController.navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
        }

    }


    override fun onResume() {
        super.onResume()
        getLocation()
        registerReceiver(mHandleMessageReceiver, IntentFilter("OPEN_NEW_ACTIVITY1"))
    }

    override fun onStop() {
        super.onStop()
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback!!)

        unregisterReceiver(mHandleMessageReceiver);
    }

    private val mHandleMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundleObject = intent.extras
            val type = bundleObject!!.getString("type")
            Log.e(TAG, "typeAAA " + type)

            if (type.equals("VIDEO_CALL", ignoreCase = true)) {
                var res = intent.getStringExtra("key")
                val jsonObject = JSONObject(res)
                val callData2 = JSONObject(jsonObject.getString("callData"))
                val user1 = JSONObject(jsonObject.getString("user1"))
                val user2 = JSONObject(jsonObject.getString("user2"))
                val dataCallData = CallData()
                dataCallData.apikey = callData2.getString("apikey")
                dataCallData.sessionId = callData2.getString("sessionId")
                dataCallData.token = callData2.getString("token")
                dataCallData.user1FirstName = user1.getString("firstName")
                dataCallData.user1LastName = user1.getString("lastName")
                dataCallData.user1Image = user1.getString("image")
                dataCallData.senderID = user1.getString("_id")
                dataCallData.user2FirstName = user2.getString("firstName")
                dataCallData.user2LastName = user2.getString("lastName")
                dataCallData.user2Image = user2.getString("image")
                dataCallData.receiverID = user2.getString("_id")
//                val gson = Gson()
//                val topic = gson.fromJson(jsonObject.toString(), CallUser::class.java)
//                val topicData = topic.data
                dataCallData.callType = "video"
                dataCallData.callFrom = "notification"
                var intent = Intent(this@HomeActivity, VideoCallActivity2::class.java)
                var bundle = Bundle()
                bundle.putSerializable("key", dataCallData);
                intent.putExtras(bundle)
                startActivity(intent)

            }

            if (type.equals("AUDIO_CALL", ignoreCase = true)) {
                var res = intent.getStringExtra("key")
                val jsonObject = JSONObject(res)
                val callData2 = JSONObject(jsonObject.getString("callData"))
                val user1 = JSONObject(jsonObject.getString("user1"))
                val user2 = JSONObject(jsonObject.getString("user2"))
                val dataCallData = CallData()
                dataCallData.apikey = callData2.getString("apikey")
                dataCallData.sessionId = callData2.getString("sessionId")
                dataCallData.token = callData2.getString("token")
                dataCallData.user1FirstName = user1.getString("firstName")
                dataCallData.user1LastName = user1.getString("lastName")
                dataCallData.user1Image = user1.getString("image")
                dataCallData.senderID = user1.getString("_id")
                dataCallData.user2FirstName = user2.getString("firstName")
                dataCallData.user2LastName = user2.getString("lastName")
                dataCallData.user2Image = user2.getString("image")
                dataCallData.receiverID = user2.getString("_id")
//                val gson = Gson()
//                val topic = gson.fromJson(jsonObject.toString(), CallUser::class.java)
//                val topicData = topic.data
                dataCallData.callType = "audio"
                dataCallData.callFrom = "notification"
                var intent = Intent(this@HomeActivity, AudioCallActivity::class.java)
                var bundle = Bundle()
                bundle.putSerializable("key", dataCallData);
                intent.putExtras(bundle)
                startActivity(intent)
            }


        }
    }




    @SuppressLint("ServiceCast")
    fun getLocation() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if(!isGPSEnabled()){
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        //getLastKnownLocation()



        locationStatus = Emitter.Listener { args ->
            runOnUiThread {
                val data: JSONObject = args[0] as JSONObject
                Log.e("locationStatus ", "message $data")
            }
        }
        AppSocketListener.getInstance().addOnHandler(SocketUrls.LOCATION, locationStatus)



        var pref = SavePref()
        pref.SavePref(this@HomeActivity)

        val json = JSONObject()
        json.put("lat", "" + pref.latitude)
        json.put("long", "" + pref.longitude)

        AppSocketListener.getInstance().emit(SocketUrls.LOCATION, json)



        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener{


            override fun onLocationChanged(location: Location) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")

                pref.latitude = ""+latitute
                pref.longitude = ""+longitute

                val json = JSONObject()
                json.put("lat", "" + latitute)
                json.put("long", "" + longitute)

                AppSocketListener.getInstance().emit(SocketUrls.LOCATION, json)

                val list = getAddressList(latitute,longitute)
                val code = list!![0].countryCode
                val address = list!![0].getAddressLine(0)

                if(locationRepeat == 0){
                    var intent = Intent()
                    intent!!.putExtra("address", address)

                    onActivityResult(100, 200 , intent)
                    sharedPref.setString("address", address)
                    locationRepeat = 1
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }



        }

        try {
            // locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            if(locationManager!!.getAllProviders().contains("network")) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            }else{
                if(locationManager!!.getAllProviders().contains("gps")) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                }
            }

            // Log.e(TAG, "locationManager.getAllProviders() "+ locationManager!!.getAllProviders())


        } catch (ex: SecurityException) {
            //Toast.makeText(applicationContext, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        primaryBaseActivity = newBase;
        super.attachBaseContext(LocaleHelper.onAttach(primaryBaseActivity));
    }


    private fun getLastKnownLocation() {
        //requireActivity().showDialog()
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 100
        mLocationRequest?.fastestInterval = 100
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //locationRequest!!.setInterval(2000)
        locationRequest?.fastestInterval = 150
        initLocationCallback()
    }

    @SuppressLint("MissingPermission")
    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                val mylocation = locationResult?.lastLocation
                val mCurrentLatLng = LatLng(mylocation?.latitude?:0.0, mylocation?.longitude?:0.0)
                val latitute = locationResult?.lastLocation?.latitude?:0.0
                val longitute = locationResult?.lastLocation?.longitude?:0.0
                Log.v("test", "Latitute: $latitute ; Longitute: $longitute")
                val list = getAddressList(latitute,longitute)
                val code = list!![0].countryCode
                val address = list!![0].getAddressLine(0)
                sharedPref.setString(AppConstants.COUNTRY_CODE, code)

                MeetMeFragment.getLocation?.getLocation(address)



                val pref = SavePref()
                pref.SavePref(this@HomeActivity)
                pref.latitude = ""+latitute
                pref.longitude = ""+longitute
                val json = JSONObject()
                json.put("lat", "" + latitute)
                json.put("long", "" + longitute)
                AppSocketListener.getInstance().emit(SocketUrls.LOCATION, json)

                var intent = Intent()
                intent!!.putExtra("address", address)

                if(locationRepeat == 0){
                    onActivityResult(100, 200 , intent)
                    sharedPref.setString("address", address)
                    locationRepeat = 1
                }



                if (fusedLocationProviderClient != null)
                    fusedLocationProviderClient?.removeLocationUpdates(locationCallback!!)
            }
        }

        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }





}