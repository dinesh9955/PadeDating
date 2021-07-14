package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.MeetMeAdapter
import com.padedatingapp.adapter.OtherUserImagesAdapter
import com.padedatingapp.adapter.PeopleWhoLikedAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentMatchesBinding
import com.padedatingapp.model.*
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.MeetMeVM
import com.padedatingapp.vm.MyMatchesVM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class MatchesFragment : DataBindingFragment<FragmentMatchesBinding>(),
    PeopleWhoLikedAdapter.OnItemClickListener {

    companion object {
        var TAG = "MatchesFragment"
    }


    private lateinit var adapter: PeopleWhoLikedAdapter
    var list = ArrayList<MeetMeData>()

    private val myMatchesVM by inject<MyMatchesVM>()
    private var progressDialog: CustomProgressDialog? = null

    private val sharedPref by inject<SharedPref>()

    override fun layoutId(): Int = R.layout.fragment_matches

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = myMatchesVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }

    private fun initComponents() {
        myMatchesVM.token = sharedPref.getString(AppConstants.USER_TOKEN)


//        var list = ArrayList<DummyModel>()
//        repeat(10) {
//            list.add(DummyModel())
//        }
      //  var adapter = PeopleWhoLikedAdapter(this)

        adapter = PeopleWhoLikedAdapter(this)

        //adapter.submitList(list)

        adapter.notifyDataSetChanged()
        viewBinding.rvWhoLiked.adapter = adapter
        viewBinding.rvWhoLiked.layoutManager = GridLayoutManager(requireContext(),2)

       viewBinding.ivBack.setOnClickListener {
           findNavController().popBackStack()
       }

        viewBinding.ivNoti.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToNotif())
        }
        viewBinding.tvPadeTab.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToMeetMe())
        }

        viewBinding.ivWhoLiked.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToBuyPremium())
        }

        viewBinding.ivFilters.setOnClickListener {
            findNavController().navigate(MatchesFragmentDirections.actionToFilters())
        }




    }



    private fun initObservables() {

        myMatchesVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

//        meetMeVM.optionChoosen.observe(viewLifecycleOwner) {
//            showDropDownDialog(it)
//        }

        myMatchesVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "MyMatches")
        }

        Log.e(MeetMeFragment.TAG, "onViewCreated11")


        val jsonObj = JsonObject()
        jsonObj.addProperty("limit", 10)
        jsonObj.addProperty("page",1)


        myMatchesVM.callMyMatchesApi(
                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )


    }




    private fun getLiveData(response: Resource<MyMatches>?, type: String) {

        //Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "MyMatches" -> {
                        val data = response.data as MyMatches
                        Log.e(TAG, "dataAA "+data.toString())
                          onMyMatchesResponse(data)
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






    private fun onMyMatchesResponse(data: MyMatches) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                list = data.data as ArrayList<MeetMeData>
                Log.e(TAG, "listAA "+list.size)
//                adapter.updateData(list)
//                adapter.notifyDataSetChanged()

//                var adapter2 = PeopleWhoLikedAdapter(this)
//                adapter2.submitList(list)
//                adapter2.notifyDataSetChanged()
//                viewBinding.rvImagesList.adapter = adapter2

                adapter.submitList(list)

                adapter.notifyDataSetChanged()
//                viewBinding.rvWhoLiked.adapter = adapter

            } else {
                toast(data.message)
            }
        }
    }






    override fun onItemClick(model: MeetMeData) {
        Log.e("Matches Fragment", "onItemClick: " )

        var userObject =
                Gson().fromJson(
                        sharedPref.getString(AppConstants.USER_OBJECT),
                        UserModel::class.java
                )

        var chatIDModel = ChatIDModel()
//        chatIDModel.senderID = userObject._id
//        chatIDModel.senderName = userObject.firstName + " "+userObject.lastName
//        chatIDModel.senderImage = userObject.image

        chatIDModel.receiverID = model._id
        chatIDModel.receiverName = model.firstName + " "+model.lastName
        chatIDModel.receiverImage = model.image

        findNavController().navigate(MatchesFragmentDirections.actionToChat(chatIDModel))
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}