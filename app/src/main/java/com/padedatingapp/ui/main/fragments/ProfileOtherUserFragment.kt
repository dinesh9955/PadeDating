package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.padedatingapp.R
import com.padedatingapp.adapter.OtherUserImagesAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentProfileOtherUserBinding
import com.padedatingapp.model.*
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ProfileOtherVM
import kotlinx.android.synthetic.main.fragment_profile_other_user.*
import org.koin.android.ext.android.inject

class ProfileOtherUserFragment : DataBindingFragment<FragmentProfileOtherUserBinding>(),
    OtherUserImagesAdapter.OnItemClickListener {

    companion object{
        var TAG = "ProfileOtherUserFragment"
    }



    private val profileOtherVM by inject<ProfileOtherVM>()
    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()

    var userID = ""


    private lateinit var adapter2 : OtherUserImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(requireContext())

        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }

    private fun initComponents() {
        profileOtherVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")


        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
      }



        adapter2 = OtherUserImagesAdapter(this)

        adapter2.notifyDataSetChanged()
        viewBinding.rvImagesList.adapter = adapter2
        viewBinding.rvImagesList.layoutManager = LinearLayoutManager(requireContext()).also {
            it.orientation = RecyclerView.HORIZONTAL
        }


        val bundle = arguments
        userID  = bundle?.getString("meetMeModel").toString()

//
//        var person  = bundle?.getParcelable<MeetMeData>("meetMeModel") as MeetMeData
//
//        Log.e(TAG, "myFoodHistory "+person?.firstName)




    }


    override fun layoutId(): Int {
        return R.layout.fragment_profile_other_user
    }

    override fun onItemClick(model: DocImage) {
        Log.e("Profile Other User", "onItemClick: ", )
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }


    private fun initObservables() {
        profileOtherVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

//        meetMeVM.optionChoosen.observe(viewLifecycleOwner) {
//            showDropDownDialog(it)
//        }

        profileOtherVM.meetMeResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "userResponse")
        }





        profileOtherVM.callProfileApi(""+userID)





    }



    private fun getLiveData(response: Resource<ResultModel<*>>?, type: String) {
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

                            if(response.data != null){
                                Glide.with(requireActivity()).load(response.data.data?.image)
                                        .apply(RequestOptions().placeholder(R.drawable.user_place_holder)).into(ivUserPic)

                                tvOtherUserName.text = response.data.data?.firstName+" "+ (response.data.data?.lastName) +", "+response.data.data?.age
                                tvAboutDesc.text = response.data.data?.description
                                tvEmployementType.text = response.data.data?.work


                                if(response.data.data?.isApproved == true){
                                    imageViewThik.visibility = View.VISIBLE
                                }else{
                                    imageViewThik.visibility = View.INVISIBLE
                                }


                                adapter2.submitList(response.data.data?.docImage)
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




}