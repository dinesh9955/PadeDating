package com.padedatingapp.ui.main

import android.content.Intent
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
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.ui.main.fragments.ChatFragment
import com.padedatingapp.ui.main.fragments.MessagesFragment
import com.padedatingapp.ui.main.fragments.MessagesFragmentDirections
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
    }

    private fun initComponents() {
        val navController = findNavController(R.id.nav_host_fragment_home)
        viewBinding.bottomMenu.setSelected(1)
        viewBinding.bottomMenu.setSelected(0)
        viewBinding.bottomMenu.addBubbleListener(object :OnBubbleClickListener{
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
            val type: String = jsonObject.getString("type")
            Log.e(TAG, "typeAA "+type)

            if (type.equals("TEXT_CHAT", ignoreCase = true)) {
            }

            if (type.equals("VIDEO_CALL", ignoreCase = true)) {
            }

            if (type.equals("AUDIO_CALL", ignoreCase = true)) {
            }


           // viewBinding.bottomMenu.setSelectedWithId(bottom_menu[2].id, false)


            var chatIDModel = ChatIDModel()

          //  val jsonObjectUser2: JSONObject = jsonObject.getJSONObject("sentTo")

            val jsonObjectUser2 = JSONObject(jsonObject.getString("sentBy"))

            Log.e(TAG, "jsonObjectUser2AA "+jsonObjectUser2)

            chatIDModel.type = type
            chatIDModel.receiverID = jsonObjectUser2.getString("_id")
            chatIDModel.receiverName = jsonObjectUser2.getString("firstName")+" "+jsonObjectUser2.getString("lastName")
            chatIDModel.receiverImage = jsonObjectUser2.getString("image")

            val bundle2 = Bundle()
            bundle2.putSerializable("meetMeModelChat" , chatIDModel)

            var chat = ChatFragment()
            chat.arguments = bundle2

            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.constraintLayout_home, chat)
            ft.disallowAddToBackStack();
            ft.commit()


            //navController.navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach { fragment ->
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}