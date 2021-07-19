package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
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
import com.flexhelp.model.chat_history.DataItem
import com.google.gson.Gson
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.padedatingapp.adapter.ChatListAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentChatBinding
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.chat.ChatUsersData
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
import com.padedatingapp.ui.chats.ConnectivityReceiver
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ChatVM
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import org.koin.android.ext.android.inject


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


//    private var bookingId: String? = null
    private var receiverId: String?=null
    private var senderId: String?=null

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
        Log.e(TAG, "bundleAA "+bundle.toString())
        person  = bundle?.getSerializable("meetMeModelChat") as ChatIDModel


//        bookingId = "60ee8d5ccf1b8502aecd4117"
        senderId = ""+userObject._id
        receiverId = ""+person.receiverID
        Log.e(TAG, "receiverId "+receiverId)




        val options = RequestOptions()
        options.centerCrop()
        options.placeholder(R.drawable.user_place_holder)

        Glide.with(requireActivity()).load(userObject.image)
                .apply(options).into(viewBinding.ivUserOne)
        viewBinding.tvUserOneName.text = userObject.firstName + " "+userObject.lastName

        Glide.with(requireActivity()).load(person.receiverImage)
                .apply(options).into(viewBinding.ivUserTwo)
        viewBinding.tvUserTwoName.text = person.receiverName


        Glide.with(requireActivity()).load(person.receiverImage)
                .apply(options).into(viewBinding.ivUserImageHeader)
        viewBinding.tvName.text = person.receiverName




        //var list = ArrayList<DummyModel>()
//        repeat(32) {
//            list.add(DummyModel())
//        }
//        var adapter = ChatListAdapter(this)
        adapter = ChatListAdapter(this)
        adapter.submitList(main_list)
        adapter.notifyDataSetChanged()
        viewBinding.rvMessageList.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        viewBinding.rvMessageList.layoutManager = layoutManager



        viewBinding.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.ivChatOptions.setOnClickListener {
            viewBinding.llChatOptions.isVisible = !viewBinding.llChatOptions.isVisible
        }

        viewBinding.ivChatCall.setOnClickListener {
            showCallPopup()
        }

        viewBinding.ivChatVideoCall.setOnClickListener {
            showCallPopup()
        }


        viewBinding.ivChatSend.setOnClickListener {
            if(viewBinding.etTypingMessage.text.toString().trimStart().isNotEmpty()){
                sendMessage("text")
                viewBinding.etTypingMessage.setText("")
            }else{
                Toast.makeText(context,"Please input String ",Toast.LENGTH_LONG).show()
            }
        }





    }




    private fun sendMessage(type : String ) {
        val json = JSONObject()
        json.put("type", type)
        json.put("id",  senderId)
        json.put("partner", receiverId)
//        json.put("bookingId", bookingId?:"")

        if(type == "text")
            json.put("message", viewBinding.etTypingMessage.text.toString())
       // else
            //json.put("media", sendImage)

        Log.e(TAG , "AppSocketListener "+AppSocketListener.getInstance().isSocketConnected)

        if(AppSocketListener.getInstance().isSocketConnected){
            AppSocketListener.getInstance().emit(SocketUrls.SEND_MESSAGE, json)
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

        chatVM.chatHistoryApi(
                receiverId
              //  jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )



    }





    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    override fun onItemClick(model: ChatUsersData) {
        Log.e("BuyPremiumFragment", "onItemClick: ")
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
            Toast.makeText(context,"" +messageToUser,Toast.LENGTH_LONG).show()
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
                        Log.e(TAG, "dataAAB "+data.toString())
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
                Log.e(MeetMeFragment.TAG, "listAA "+main_list.size)
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



    private fun initializeSockets() {
        onConnect = Emitter.Listener {
            activity?.runOnUiThread {
//                val json = JSONObject()
//                json.put("token", userObject.accessToken?:"")
                socket.emit("room")

                Handler().postDelayed({

                    roomJoined = true
                    if (unsendMessageList?.size ?: 0 > 0) {
                        for (i in 0 until unsendMessageList?.size!!)
                            socket.emit("sendMessage",
                                    JSONObject(PadeDatingApp.gson.toJson(unsendMessageList?.get(i)))
                            )
                    }
                    unsendMessageList?.clear()
                }, 500)
            }
        }
        onDisconnect = Emitter.Listener {
            activity?.runOnUiThread {
                Log.e("onDisconnect", "onDisconnect")
                //showMessage(data.toString())
                //addUser = false
                roomJoined = false
                socket.connect()
                Log.e("Socket", " socket ${socket.connected()}")
            }
        }

        onNewMessage = Emitter.Listener { args ->
            activity?.runOnUiThread {
                val data: JSONObject = args[0] as JSONObject

                Log.e("onNewMe ", "message $data")
//                val chat_message = PadeDatingApp.gson.fromJson(data.toString(), DataItem::class.java)
//
//                if (chat_message?.type.equals("text")) {
//                    if (chat_message?.sender!!.equals(userObject._id)){//(context as BaseActivity).user_obj?.id)) {
//                        chat_message.typeText = 0
//                        main_list.add(chat_message)
//                    } else {
//                        chat_message.typeText = 1
//                        main_list.add(chat_message)
//                    }
//                }
//                else
//                {
//                    if (chat_message?.sender!!.equals( person.receiverID)){//(context as BaseActivity).user_obj?.id)) {
//                        chat_message.typeText = 0
//                        main_list.add(chat_message)
//                    } else {
//                        chat_message.typeText = 1
//                        main_list.add(chat_message)
//                    }
//                }
//                adapter?.notifyDataSetChanged()


//                if (main_list.size > 0)
//                    binding.rvRecycler.scrollToPosition(main_list.size - 1)
            }
        }

        callListerners()

    }


    private fun callListerners() {
        AppSocketListener.getInstance().addOnHandler(SocketUrls.NEW_MESSAGES  , onNewMessage )
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