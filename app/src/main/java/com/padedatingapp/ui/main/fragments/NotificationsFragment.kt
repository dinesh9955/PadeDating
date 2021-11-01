package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.NotificationListAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentNotificationsBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMe
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.notification.Doc
import com.padedatingapp.model.notification.NotificationModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BlockUserVM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class NotificationsFragment : DataBindingFragment<FragmentNotificationsBinding>(),
    NotificationListAdapter.OnItemClickListener {


    companion object {
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
        var TAG = "NotificationsFragment"
    }

    private val chatVM by inject<BlockUserVM>()
    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()
    private lateinit var userObject : UserModel

    override fun layoutId(): Int = R.layout.fragment_notifications

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }

    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.notifications)

        chatVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")
        userObject =
                Gson().fromJson(
                        sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                        UserModel::class.java
                )


//        var list = ArrayList<DummyModel>()
//        repeat(7) {
//            list.add(DummyModel())
//        }
//        var adapter = NotificationListAdapter(this)
//        adapter.submitList(list)
//        adapter.notifyDataSetChanged()
//        viewBinding.rvNotificationList.adapter = adapter
//        viewBinding.rvNotificationList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }




    }




    private fun initObservables() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }


        chatVM.notificationModelResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "Notifications")
        }




        val jsonObj = JsonObject()
        jsonObj.addProperty("limit", 10)
        jsonObj.addProperty("page", 1)


        chatVM.callNotificationApi(jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull()))



    }



    private fun getLiveData(response: Resource<NotificationModel>?, type: String) {

        Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "Notifications" -> {
                        val data = response.data as NotificationModel
                        Log.e(TAG, "dataAACCCVVVSS "+data.toString())
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



    private fun onMeetMeResponse(data: NotificationModel) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                var notificationModelList = data.data.docs as ArrayList<Doc>
                Log.e(TAG, "listAA "+notificationModelList.size)

                var adapter = NotificationListAdapter(this)
                adapter.submitList(notificationModelList)
                adapter.notifyDataSetChanged()
                viewBinding.rvNotificationList.adapter = adapter
                viewBinding.rvNotificationList.layoutManager = LinearLayoutManager(requireContext())

            } else {
                toast(data.message)
            }
        }
    }



    override fun onItemClick(model: DummyModel) {
        Log.e("Noti_Fragment", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}