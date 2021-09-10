package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.flexhelp.model.MessageItem
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.padedatingapp.adapter.ChatListAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentChatBinding
import com.padedatingapp.model.CallData
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.chat.ChatUsersData
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
import com.padedatingapp.ui.call.AudioCallActivity
import com.padedatingapp.ui.call.VideoCallActivity
import com.padedatingapp.ui.call.VideoCallActivity2
import com.padedatingapp.ui.chats.ConnectivityReceiver
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ChatVM
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_chat.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : DataBindingFragment<FragmentChatBinding>(),
    ChatListAdapter.OnItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener {


    companion object{
        var TAG = "ChatFragment"
    }




    private lateinit var dialog: Dialog

    private val sharedPref by inject<SharedPref>()

    private lateinit var userObject : UserModel
    private lateinit var person: ChatIDModel

    private val chatVM by inject<ChatVM>()
    private var progressDialog: CustomProgressDialog? = null

    private lateinit var adapter: ChatListAdapter



    var block : Boolean = false

//    private var bookingId: String? = null


    private var senderId: String?=null
    private var receiverId: String?=null
    private var blockUser: String?=null

    var main_list = ArrayList<ChatUsersData>()

    var unsendMessageList: MutableList<MessageItem>? = ArrayList()

    var roomJoined = false
    lateinit var socket: Socket
    lateinit var onConnect: Emitter.Listener
    lateinit var onNewMessage: Emitter.Listener
    lateinit var onDisconnect: Emitter.Listener
    lateinit var error: Emitter.Listener


    override fun layoutId(): Int = R.layout.fragment_chat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this




        AppSocketListener.getInstance().restartSocket()

        initComponents()
        initObservables()


            initializeSockets()
            joinRoom()


    }



    private fun initComponents() {



        userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )
        chatVM.token = sharedPref.getString(AppConstants.USER_TOKEN)





        val bundle = arguments
        Log.e(TAG, "bundleAA " + bundle.toString())
        person  = bundle?.getSerializable("meetMeModelChat") as ChatIDModel


//        if(person.type.equals("VIDEO_CALL")){
//
//            var dataCall : CallUser? = null
//
//            var data = dataCall?.data
//
//            data?.apikey = ""
//            data?.apikey = ""
//            data?.apikey = ""
//            data?.apikey = ""
//            data?.apikey = ""
//
//            Log.e(TAG, "dataAAC " + data.toString())
//
//
//            var intent = Intent(requireContext(), VideoCallActivity::class.java)
//            var bundle = Bundle()
//            bundle.putSerializable("key", data);
//            intent.putExtras(bundle)
//            startActivity(intent)
//
////            if(data.data.callType.equals("audio")){
////                var intent = Intent(requireContext(), VideoCallActivity::class.java)
////                var bundle = Bundle()
////                bundle.putSerializable("key", data);
////                intent.putExtras(bundle)
////                startActivity(intent)
////            }else if(data.data.callType.equals("video")){
////                var intent = Intent(requireContext(), VideoCallActivity::class.java)
////                var bundle = Bundle()
////                bundle.putSerializable("key", data);
////                intent.putExtras(bundle)
////                startActivity(intent)
////            }
//
////            var data : CallUser
////            data.data.apikey = person.
//
////            var intent = Intent(requireContext(), VideoCallActivity::class.java)
////            var bundle = Bundle()
////            bundle.putSerializable("key", data);
////            intent.putExtras(bundle)
////            startActivity(intent)
//        }


        senderId = ""+userObject._id
        Log.e(TAG, "senderId " + senderId)

        receiverId = ""+person.receiverID
        Log.e(TAG, "receiverId " + receiverId)




        tvName.setOnClickListener {
            findNavController().navigate(ChatFragmentDirections.actionToOtherProfile(person.receiverID))
        }
//        ivUserImageHeader.setOnClickListener {
//            findNavController().navigate(ChatFragmentDirections.actionToOtherProfile(person.receiverID))
//        }


        val options = RequestOptions()
        options.centerCrop()
        options.placeholder(R.drawable.user_circle_1179465)
        Glide.with(requireActivity()).load(userObject.image)
                .apply(options).into(viewBinding.ivUserOne)
        viewBinding.tvUserOneName.text = userObject.firstName + " "+userObject.lastName


        Log.e(TAG, "userObject.image "+userObject.image)
        Log.e(TAG, "userObject.image2 "+person.receiverImage)

        Glide.with(requireActivity()).load(person.receiverImage)
                .apply(options).into(viewBinding.ivUserTwo)
        viewBinding.tvUserTwoName.text = person.receiverName


        Glide.with(requireActivity()).load(person.receiverImage)
                .apply(options).into(viewBinding.ivUserImageHeader)
        viewBinding.tvName.text = person.receiverName


        if(block == true){
            tvBlock.text = "Blocked user"
        }else{
            tvBlock.text = "Block user"
        }

        tvBlock.setOnClickListener {
            viewBinding.llChatOptions.visibility = View.GONE
        }

        tvReport.setOnClickListener {
            viewBinding.llChatOptions.visibility = View.GONE
        }



        //var list = ArrayList<DummyModel>()
//        repeat(32) {
//            list.add(DummyModel())
//        }
//        var adapter = ChatListAdapter(this)
        adapter = ChatListAdapter(this)

        adapter.updateList(userObject._id)
        adapter.submitList(main_list)
        adapter.notifyDataSetChanged()
        viewBinding.rvMessageList.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        viewBinding.rvMessageList.layoutManager = layoutManager




        viewBinding.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()

            Log.e(TAG, "personZZ " + person.type)

            if(person.type.equals("VIDEO_CALL") || person.type.equals("TEXT_CHAT")){
                activity?.finish()
            }else{
                findNavController().popBackStack()
            }

        }

        viewBinding.ivChatOptions.setOnClickListener {
            viewBinding.llChatOptions.isVisible = !viewBinding.llChatOptions.isVisible
        }

        viewBinding.ivChatCall.setOnClickListener {
            //showCallPopup()
        val jsonObj = JsonObject()
        jsonObj.addProperty("callType", "audio")
            jsonObj.addProperty("partner", receiverId)
            chatVM.callApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )
        }

        viewBinding.ivChatVideoCall.setOnClickListener {
//            showCallPopup()
            val jsonObj = JsonObject()
            jsonObj.addProperty("callType", "video")
            jsonObj.addProperty("partner", receiverId)
            chatVM.callApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )
        }


        viewBinding.ivChatSend.setOnClickListener {
            if(viewBinding.etTypingMessage.text.toString().trimStart().isNotEmpty()){
                sendMessage("text")
                viewBinding.etTypingMessage.setText("")
            }else{
                Toast.makeText(context, "Please input String ", Toast.LENGTH_LONG).show()
            }
        }





    }




    private fun sendMessage(type: String) {

        Log.e(TAG, "userObject._id " + userObject._id)
        Log.e(TAG, "person.receiverID " + person.receiverID)

        val json = JSONObject()
        json.put("type", type)
        json.put("id", userObject._id)
        json.put("partner", person.receiverID)

        if(type == "text")
            json.put("message", viewBinding.etTypingMessage.text.toString())
       // else
            //json.put("media", sendImage)

        Log.e(TAG, "AppSocketListener " + AppSocketListener.getInstance().isSocketConnected)

        if(AppSocketListener.getInstance().isSocketConnected){
            AppSocketListener.getInstance().emit(SocketUrls.SEND_MESSAGE, json)
        }else{
//            val mApplication: PadeDatingApp = requireActivity() as PadeDatingApp
//            mApplication.initializeSocket(requireActivity())
        }

    }



    private fun initObservables() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }




        chatVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "ChatHistory")
        }



//        val jsonObj = JsonObject()
//        jsonObj.addProperty("gender", "interest")


//        val jsonObj = JsonObject()
//        jsonObj.addProperty("action", "like")


//        chatVM.userProfile(
//                list[position]._id,
//                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
//        )



        chatVM.chatHistoryApi(
            receiverId
            //  jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )





        chatVM.callUserResponse.observe(viewLifecycleOwner) {
            getCallLiveData(it, "Call")
        }

    }





    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    override fun onItemClick(model: ChatUsersData) {
        Log.e("BuyPremiumFragment", "onItemClick: ")
        chatVM.deleteChatApi(
            model._id
            //  jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )

        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

        chatVM.deleteMessageResponse.observe(viewLifecycleOwner) {
            getDeleteMessage(it, "DeleteMessage")
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    private fun showCallPopup() = try {
        if (::dialog.isInitialized && dialog.isShowing)
            dialog.cancel()
        dialog = Dialog(requireActivity(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.dialog_call, null)
        val tvEndCall = dialogView.findViewById<TextView>(R.id.tvEndCall)
        val ivChat = dialogView.findViewById<ImageView>(R.id.ivChat)
        tvEndCall.setOnClickListener {
            dialog.dismiss()
        }
        ivChat.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        dialog.show()


    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            if (!socket.connected()) {
                socket.connect()

                Log.e("Socket", " socket ${socket.connected()}")
            }
        } else {
            val messageToUser = getString(R.string.check_internet)
            Toast.makeText(context, "" + messageToUser, Toast.LENGTH_LONG).show()
        }
    }








    private fun getLiveData(response: Resource<ChatUsers>?, type: String) {

        //Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "ChatHistory" -> {
                        val data = response.data as ChatUsers
                        Log.e(TAG, "dataAAB " + data.toString())
                        onMeetMeResponse(data)
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }




    private fun onMeetMeResponse(data: ChatUsers) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                main_list = data.data as ArrayList<ChatUsersData>
                Log.e(MeetMeFragment.TAG, "listAA " + main_list.size)

                Collections.reverse(main_list)

                adapter.updateList(userObject._id)
                adapter.submitList(main_list)
                adapter.notifyDataSetChanged()

//                if(list.size == 0){
//                    viewBinding.likeFloating.visibility = View.GONE
//                    viewBinding.unlikeFloating.visibility = View.GONE
//                }

            } else {
                toast(data.message)
            }
        }
    }





    private fun getCallLiveData(response: Resource<CallUser>?, type: String) {

        //Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "Call" -> {
                        val data = response.data as CallUser
                        Log.e(TAG, "dataAAC " + data.toString())

                        val dataCallData = CallData()
                        dataCallData.apikey = data.data.apikey
                        dataCallData.sessionId = data.data.sessionId
                        dataCallData.token = data.data.token
                        dataCallData.user1FirstName = data.data.user1.firstName
                        dataCallData.user1LastName = data.data.user1.lastName
                        dataCallData.user1Image = data.data.user1.image
                        dataCallData.user2FirstName = data.data.user2.firstName
                        dataCallData.user2LastName = data.data.user2.lastName
                        dataCallData.user2Image = data.data.user2.image


                        if (data.data.callType.equals("audio")) {
                            dataCallData.callType = "audio"
                            var intent = Intent(requireContext(), AudioCallActivity::class.java)
                            var bundle = Bundle()
                            bundle.putSerializable("key", dataCallData);
                            intent.putExtras(bundle)
                            startActivity(intent)
                        } else if (data.data.callType.equals("video")) {
                            dataCallData.callType = "video"
                            var intent = Intent(requireContext(), VideoCallActivity2::class.java)
                            var bundle = Bundle()
                            bundle.putSerializable("key", dataCallData);
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }

                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }



    private fun getDeleteMessage(response: Resource<ChatDelete>?, type: String) {

        Log.e(TAG, "response?.status "+response?.status)



        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "DeleteMessage" -> {
                        val data = response.data as ChatDelete
                        Log.e(TAG, "dataAACChatDelete " + data.toString())
                        chatVM.chatHistoryApi(
                            receiverId
                        )
                    }
                }
            }
            Resource.Status.ERROR -> {
                progressDialog?.dismiss()
                toast(response.getErrorMessage().toString())
            }
            Resource.Status.CANCEL -> {
                progressDialog?.dismiss()
            }
        }
    }





    //  var count = 0

    private fun initializeSockets() {
        Log.e(TAG, "initializeSockets")

        onConnect = Emitter.Listener {
            activity?.runOnUiThread {
//                val json = JSONObject()
//                json.put("token", userObject.accessToken?:"")
                socket.emit("room")
            //    socket.emit("online", "true")
                roomJoined = true
                Log.e(TAG, "")
            }
        }


//        onDisconnect = Emitter.Listener {
//            activity?.runOnUiThread {
//                Log.e("onDisconnect", "onDisconnect")
//                //showMessage(data.toString())
//                //addUser = false
//                roomJoined = false
//                socket.connect()
//                Log.e("Socket", " socket ${socket.connected()}")
//            }
//        }

        onNewMessage = Emitter.Listener { args ->
            activity?.runOnUiThread {

                val data: JSONObject = args[0] as JSONObject

                Log.e("onNewMe ", "message $data")

                val chat_message = PadeDatingApp.gson.fromJson(
                    data.toString(),
                    ChatUsersData::class.java
                )




                main_list.add(chat_message)

                viewBinding.rvMessageList.scrollToPosition(main_list.size - 1)

                adapter.updateList(userObject._id)
                adapter.submitList(main_list)


                adapter?.notifyDataSetChanged()

                Log.e("onNewMeXX ", "message $data")

            }
        }

        callListerners()

    }


    private fun callListerners() {
        AppSocketListener.getInstance().addOnHandler(SocketUrls.NEW_MESSAGES, onNewMessage)
    }

    private fun removeListerners() {
        AppSocketListener.getInstance().restartSocket()
    }

    private fun joinRoom() {
//        val json = JSONObject()
//        json.put("token", userObject.accessToken?:"")
        AppSocketListener.getInstance().emit(SocketUrls.JOIN_ROOM)
    }




//    private fun clicks() {
//        viewBinding.ivChatSend.setOnClickListener {
//            if(binding.etTypingMessage.text.toString().trimStart().isNotEmpty()){
//                sendMessage("text")
//                binding.etTypingMessage.setText("")
//            }else{
//                Toast.makeText(context,"Please input String ",Toast.LENGTH_LONG).show()
//            }
//
//        }
//
//        binding.ivChatShareFile.setOnClickListener {
//
//            CropImage.activity()
//                    .setOutputCompressQuality(50)
//                    .start(requireActivity(), this)
//        }
//    }



}