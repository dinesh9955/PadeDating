package com.padedatingapp.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.fxn.OnBubbleClickListener
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingActivity
import com.padedatingapp.databinding.ActivityHomeBinding
import com.padedatingapp.extensions.onNavDestinationSelected
import com.padedatingapp.model.CallData
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.ui.call.AudioCallActivity
import com.padedatingapp.ui.call.VideoCallActivity
import com.padedatingapp.ui.call.VideoCallActivity2
import com.padedatingapp.ui.main.fragments.ChatFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject

class HomeActivity : DataBindingActivity<ActivityHomeBinding>() {

    companion object{
        var TAG = "HomeActivity"
    }

    override fun layoutId(): Int = R.layout.activity_home

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


        val bundle = intent.extras
        if (bundle != null) {
            var res = bundle.getString("key")
            val jsonObject = JSONObject(res)
            Log.e(TAG, "jsonObjectAA " + jsonObject)
            val type: String = jsonObject.getString("type")
            Log.e(TAG, "typeAA " + type)

            if (type.equals("TEXT_CHAT", ignoreCase = true)) {
                var chatIDModel = ChatIDModel()

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





            //navController.navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
        }

    }


    override fun onResume() {
        super.onResume()
        registerReceiver(mHandleMessageReceiver, IntentFilter("OPEN_NEW_ACTIVITY1"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mHandleMessageReceiver);
    }

    private val mHandleMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundleObject = intent.extras
            val type = bundleObject!!.getString("type")
            Log.e(TAG, "typeAAA "+type)

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}