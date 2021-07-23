package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.MatchesAtChatAdapter
import com.padedatingapp.adapter.MessagesListAdapter
import com.padedatingapp.adapter.PeopleWhoLikedAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentMessagesBinding
import com.padedatingapp.model.ChatIDModel
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.chat.ChatUsersData
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ChatUserVM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class MessagesFragment : DataBindingFragment<FragmentMessagesBinding>(),
        MatchesAtChatAdapter.OnItemClickListener, MessagesListAdapter.OnItemClickListener {

    companion object {
        var TAG = "MessagesFragment"
    }

    private val chatVM by inject<ChatUserVM>()
    private var progressDialog: CustomProgressDialog? = null

    private val sharedPref by inject<SharedPref>()

    private lateinit var adapter1: MatchesAtChatAdapter

    private lateinit var adapter: MessagesListAdapter
    var list_data = ArrayList<ChatUsersData>()

    private lateinit var userObject : UserModel

    override fun layoutId(): Int = R.layout.fragment_messages
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }

    private fun initComponents() {
        chatVM.token = sharedPref.getString(AppConstants.USER_TOKEN)
        userObject =
                Gson().fromJson(
                        sharedPref.getString(AppConstants.USER_OBJECT),
                        UserModel::class.java
                )

//        var list = ArrayList<DummyModel>()
//        repeat(10) {
//            list.add(DummyModel())
//        }

        adapter1 = MatchesAtChatAdapter(this)
//        adapter2.submitList(list)
//        adapter2.notifyDataSetChanged()
        viewBinding.rvMatches.adapter = adapter1
        viewBinding.rvMatches.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = RecyclerView.HORIZONTAL
        }


        adapter = MessagesListAdapter(this)
       // adapter.submitList(list_data)
//        adapter.notifyDataSetChanged()
        viewBinding.rvMessageList.adapter = adapter
        viewBinding.rvMessageList.layoutManager = LinearLayoutManager(requireContext())



    }




    private fun initObservables() {

        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }


        chatVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "ChatUser")
        }

        Log.e(TAG, "onViewCreated11")


        val jsonObj = JsonObject()
        jsonObj.addProperty("limit", 10)
        jsonObj.addProperty("page",1)


        chatVM.chatUserListApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )


    }





    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
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
                    "ChatUser" -> {
                        val data = response.data as ChatUsers
                        Log.e(TAG, "dataAA "+data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                                Log.e(TAG, "listAA "+data.data.size)
                                list_data = data.data as ArrayList<ChatUsersData>

                                adapter.submitList(list_data)
                                adapter.updateList(userObject._id)
                                adapter.notifyDataSetChanged()

                                adapter1.submitList(list_data)
                                adapter1.notifyDataSetChanged()

                            } else {
                                toast(data.message)
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





//    override fun onItemClick(model: ChatUsersData) {
//        var chatIDModel = ChatIDModel()
////        chatIDModel.senderID = userObject._id
////        chatIDModel.senderName = userObject.firstName + " "+userObject.lastName
////        chatIDModel.senderImage = userObject.image
//
//        chatIDModel.receiverID = model.sentTo._id
//        chatIDModel.receiverName = model.sentTo.firstName + " "+model.sentTo.lastName
//        chatIDModel.receiverImage = model.sentTo.image
//        findNavController().navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
//    }

    override fun onItemClick(model: ChatUsersData) {
        var chatIDModel = ChatIDModel()
//        chatIDModel.senderID = userObject._id
//        chatIDModel.senderName = userObject.firstName + " "+userObject.lastName
//        chatIDModel.senderImage = userObject.image



        if(!model.sentBy._id.equals(userObject._id)){
//            Glide.with(binding.root).load(model.sentBy.image)
//                    .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)
//            tvName.text = model.sentBy.firstName + " " + model.sentBy.lastName

            chatIDModel.receiverID = model.sentBy._id
            chatIDModel.receiverName = model.sentBy.firstName + " "+model.sentBy.lastName
            chatIDModel.receiverImage = model.sentBy.image
        }
        if(!model.sentTo._id.equals(userObject._id)){
//            Glide.with(binding.root).load(model.sentTo.image)
//                    .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)
//            tvName.text = model.sentTo.firstName + " " + model.sentTo.lastName

            chatIDModel.receiverID = model.sentTo._id
            chatIDModel.receiverName = model.sentTo.firstName + " "+model.sentTo.lastName
            chatIDModel.receiverImage = model.sentTo.image
        }

        Log.e(TAG, "receiverIDVV "+chatIDModel.receiverID)

        findNavController().navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
    }


//    override fun onItemClick(model: ChatUsersData) {
////        findNavController().navigate(R.id.action_to_chat_fragment)
//
//        var chatIDModel = ChatIDModel()
////        chatIDModel.senderID = userObject._id
////        chatIDModel.senderName = userObject.firstName + " "+userObject.lastName
////        chatIDModel.senderImage = userObject.image
//
////        chatIDModel.receiverID = model._id
////        chatIDModel.receiverName = model.firstName + " "+model.lastName
////        chatIDModel.receiverImage = model.image
//
//        findNavController().navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
//
//    }
//
//
//
//
//    override fun onItemClick(model: ChatUsersData) {
//
//        var chatIDModel = ChatIDModel()
////        chatIDModel.senderID = userObject._id
////        chatIDModel.senderName = userObject.firstName + " "+userObject.lastName
////        chatIDModel.senderImage = userObject.image
//
//        chatIDModel.receiverID = model._id
//        chatIDModel.receiverName = model.firstName + " "+model.lastName
//        chatIDModel.receiverImage = model.image
//
//        findNavController().navigate(MessagesFragmentDirections.actionToChatFragment(chatIDModel))
//
////        findNavController().navigate(MatchesFragmentDirections.actionToChat(chatIDModel))
//
//    }
}