package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.flexhelp.model.MessageItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.padedatingapp.model.*
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.chat.ChatUsersData
import com.padedatingapp.model.reasons.ReasonModel
import com.padedatingapp.model.reasons.Value
import com.padedatingapp.model.reportUser.ReportUserModel
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
import com.padedatingapp.ui.call.AudioCallActivity
import com.padedatingapp.ui.call.VideoCallActivity2
import com.padedatingapp.ui.chats.ConnectivityReceiver
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ChatVM
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.listeners.*
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.bottomsheet_gift_card_purchased.*
import kotlinx.android.synthetic.main.bottomsheet_report.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_profile_other_user.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : DataBindingFragment<FragmentChatBinding>(),
    ChatListAdapter.OnItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener {


    companion object{
        var TAG = "ChatFragment"
    }

    var titleIdReport = ""
    var descriptionReport = ""


    lateinit var list : List<Value>

    private lateinit var dialog: Dialog

    private val sharedPref by inject<SharedPref>()

    private lateinit var userObject : UserModel
    private lateinit var person: ChatIDModel

    private val chatVM by inject<ChatVM>()
    private var progressDialog: CustomProgressDialog? = null



    private lateinit var adapter: ChatListAdapter

    var emojiPopup: EmojiPopup? = null
    var heightDiff: Int = 0

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
    lateinit var onlineStatus: Emitter.Listener
    lateinit var offlineStatus: Emitter.Listener
//    lateinit var callDiscount: Emitter.Listener

    var reasonText: TextView?=null

    override fun layoutId(): Int = R.layout.fragment_chat
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this

        val mRootWindow: Window = activity?.window!!
        val mRootView: View = mRootWindow.getDecorView().findViewById(android.R.id.content)
        val r: Resources = resources
        val px = Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                36f,
                r.getDisplayMetrics()
            )
        )
        emojiPopup = EmojiPopup.Builder.fromRootView(mainView).build(etTypingMessage)
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            heightDiff = mRootView.getRootView().getHeight() - mRootView.getHeight()

            if (emojiPopup != null) {
                emojiPopup?.dismiss();
            }
            emojiPopup?.setPopupWindowHeight(heightDiff - (px * 2))
        })

        AppSocketListener.getInstance().restartSocket()

        initComponents()
        initObservables()
        initObservablesOnline()
        initObservablesBlockUser()
        initObservablesReason()
            initializeSockets()
            joinRoom()


    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun initComponents() {

        userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )
        chatVM.token = sharedPref.getString(AppConstants.USER_TOKEN)

        if (userObject != null) {
            val json = JSONObject()
            try {
                json.put("partner", userObject._id)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            AppSocketListener.getInstance().emit(SocketUrls.ONLINE, json)
        }



        val bundle = arguments
        Log.e(TAG, "bundleAA " + bundle.toString())
        person  = bundle?.getSerializable("meetMeModelChat") as ChatIDModel




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


        Log.e(TAG, "userObject.image " + userObject.image)
        Log.e(TAG, "userObject.image2 " + person.receiverImage)

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

//            val jsonObj = JsonObject()
//            jsonObj.addProperty("callType", "audio")
//            jsonObj.addProperty("partner", receiverId)
            chatVM.blockApi(receiverId!!)


        }

        tvReport.setOnClickListener {
            viewBinding.llChatOptions.visibility = View.GONE
            showCongratsBottomSheet()
        }

        ivEmoji.setOnClickListener { ignore ->
            val keyboard: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (keyboard.isAcceptingText()) {
                emojiPopup!!.toggle()
            }
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


//        val jsonObj = JsonObject()
//        jsonObj.addProperty("callType", "video")
//        jsonObj.addProperty("partner", receiverId)



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


     //   AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect, json)

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
                        dataCallData.senderID = data.data.user1._id
                        dataCallData.user2FirstName = data.data.user2.firstName
                        dataCallData.user2LastName = data.data.user2.lastName
                        dataCallData.user2Image = data.data.user2.image
                        dataCallData.user2Image = data.data.user2.image
                        dataCallData.receiverID = data.data.user2._id

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

        Log.e(TAG, "response?.status " + response?.status)



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


//        callDiscount = Emitter.Listener { args ->
//            activity?.runOnUiThread {
//
//                val data: JSONObject = args[0] as JSONObject
//
//                Log.e("callDiscountAA ", "message $data")
//
//            }
//        }


        onlineStatus = Emitter.Listener { args ->
            activity?.runOnUiThread {
                val data: JSONObject = args[0] as JSONObject

                Log.e("onlineStatus ", "message $data")
            }
        }

        offlineStatus = Emitter.Listener { args ->
            activity?.runOnUiThread {
                val data: JSONObject = args[0] as JSONObject

                Log.e("offlineStatus ", "message $data")
            }
        }

        callListerners()

    }


    private fun callListerners() {
        AppSocketListener.getInstance().addOnHandler(SocketUrls.NEW_MESSAGES, onNewMessage)
        AppSocketListener.getInstance().addOnHandler(SocketUrls.ONLINE, onlineStatus)
        AppSocketListener.getInstance().addOnHandler(SocketUrls.OFFLINE, offlineStatus)

        val json = JSONObject()
        json.put("partner", person.receiverID)

            AppSocketListener.getInstance().emit(SocketUrls.OFFLINE, json)

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





    private fun initObservablesOnline() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

//        meetMeVM.optionChoosen.observe(viewLifecycleOwner) {
//            showDropDownDialog(it)
//        }

        chatVM.meetMeResponse.observe(viewLifecycleOwner) {
            getLiveDataOnline(it, "userResponse")
        }





        chatVM.callMeetMeApi("" + receiverId)





    }



    private fun getLiveDataOnline(response: Resource<ResultModel<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "userResponse" -> {
                        val data = response.data as ResultModel<MeetMeData>
                        Log.e(TAG, "dataAA " + response.data)
                        Log.e(TAG, "dataBB " + data.toString())
                        // onLoginResponse(data)

                        if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {

                            if (response.data != null) {

                                if (response.data.data?.isOnline == true) {
                                    viewBinding.isOnline.text = "Online"
                                    viewBinding.isOnline.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.ic_online,
                                        0,
                                        0,
                                        0
                                    );
                                } else {
                                    viewBinding.isOnline.text = "Offline"
                                    viewBinding.isOnline.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.ic_offline,
                                        0,
                                        0,
                                        0
                                    );
                                }


//                                Glide.with(requireActivity()).load(response.data.data?.image)
//                                    .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)
//
//                                tvOtherUserName.text = response.data.data?.firstName+" "+ (response.data.data?.lastName) +", "+response.data.data?.age
//                                tvAboutDesc.text = response.data.data?.description
//                                tvEmployementType.text = response.data.data?.work
//
//
//                                if(response.data.data?.isApproved == true){
//                                    imageViewThik.visibility = View.VISIBLE
//                                }else{
//                                    imageViewThik.visibility = View.INVISIBLE
//                                }
//
//
//                                adapter2.submitList(response.data.data?.docImage)
                            }


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




    private fun initObservablesBlockUser() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

        chatVM.blockUserResponse.observe(viewLifecycleOwner) {
            getLiveDataBlockUser(it, "userResponse")
        }


    }




//
//    private fun getLiveDataBlockUser(response: Resource<BlockUserModel>?, s: String) {
//
//    }


    private fun getLiveDataBlockUser(response: Resource<BlockUserModel>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "userResponse" -> {
                        val data = response.data as BlockUserModel
                        Log.e(TAG, "dataAA " + response.data)
                        Log.e(TAG, "dataBB " + data.toString())
                        // onLoginResponse(data)
//
                        if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                            if (response.data != null) {
                                toast(response.data.message)
                                if (person.type.equals("VIDEO_CALL") || person.type.equals("TEXT_CHAT")) {
                                    activity?.finish()
                                } else {
                                    findNavController().popBackStack()
                                }
                            }
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



    private fun initObservablesReason() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

        chatVM.reasonModel.observe(viewLifecycleOwner) {
            getLiveDataReason(it, "userResponse")
        }

        chatVM.reportUserResponse.observe(viewLifecycleOwner) {
            getLiveDataReportUser(it, "userResponse")
        }

        chatVM.reasonApi()
    }



    private fun getLiveDataReason(response: Resource<ReasonModel>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "userResponse" -> {
                        val data = response.data as ReasonModel
                        Log.e(TAG, "dataAA " + response.data)
                        Log.e(TAG, "dataBB " + data.toString())
                        // onLoginResponse(data)
//
                        if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                            if (response.data != null) {
                                //toast(response.data.message)
                                Log.e(TAG, "dataAACC " + response.data.data)
                                list = response.data.data.value
                            }
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


    private fun getLiveDataReportUser(response: Resource<ReportUserModel>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "userResponse" -> {
                        val data = response.data as ReportUserModel
                        Log.e(TAG, "dataAA " + response.data)
                        Log.e(TAG, "dataBB " + data.toString())
                        // onLoginResponse(data)
//
                        if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                            if (response.data != null) {
                                Log.e(TAG, "dataAACC " + response.data.message)
                                toast(response.data.message)
                            }
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





    @RequiresApi(Build.VERSION_CODES.N)
    private fun showCongratsBottomSheet() = try {
       var bottomSheetDialog4 = Dialog(requireContext())
        val view: View = LayoutInflater.from(activity).inflate(R.layout.bottomsheet_report, null)
        bottomSheetDialog4.setContentView(view)
        bottomSheetDialog4.getWindow()?.getAttributes()!!.windowAnimations = R.style.DialogAnimation
        val window: Window = bottomSheetDialog4.getWindow()!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        reasonText = view.findViewById<TextView>(R.id.tvReason)
        var descriptionText = view.findViewById<EditText>(R.id.etDescription)
        reasonText?.setOnClickListener {
            requireActivity().hideKeyboard()
            showDropDownDialog()
            Log.e(TAG, "tvReason")
        }

        var btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        var btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnSubmit?.setOnClickListener {
            bottomSheetDialog4.dismiss()
            requireActivity().hideKeyboard()
//            showDropDownDialog()
//            titleIdReport = ""
//            descriptionReport = ""
            val jsonObj = JsonObject()
            jsonObj.addProperty("target", receiverId)
            jsonObj.addProperty("reportedIssue", titleIdReport)
            jsonObj.addProperty("description", descriptionText.text.toString())

            chatVM.reportUserApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
            )

        }

        btnCancel?.setOnClickListener {
            bottomSheetDialog4.dismiss()
        }


        bottomSheetDialog4.show()

    } catch (e: Exception) {
        e.printStackTrace()
    }




    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDropDownDialog() {
        var index = 0
        val array = arrayOfNulls<String>(list.size)
        for (value in list) {
            array[index] = value.text
            index++
        }

        //val list = resources.getStringArray(R.array.gender_array)
       // val array: Array<String> = list.stream().toArray { arrayOfNulls<String>(it) }
       MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Reason")
            .setItems(array) { _, which ->
                reasonText!!.text = list[which].text
                titleIdReport = list[which].id
            }.show()
    }

}