package com.padedatingapp.ui.main.fragments
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.padedatingapp.R
import com.padedatingapp.adapter.BlockUserAdapter
import com.padedatingapp.adapter.LoyalityPointAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentLoyaltyPointBinding
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.blockUser.BlockModel
import com.padedatingapp.model.blockUser.Data
import com.padedatingapp.model.loyalityModel.DataX
import com.padedatingapp.model.loyalityModel.LoyalityPointsResponse
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BlockUserVM
import com.padedatingapp.vm.LoyalityVM
import kotlinx.android.synthetic.main.fragment_messages.*
import org.koin.android.ext.android.inject


class LoyaltyPointFragment : DataBindingFragment<FragmentLoyaltyPointBinding>() {

    private val sharedPref by inject<SharedPref>()
    private val loyalityVM by inject<LoyalityVM>()
    private var progressDialog: CustomProgressDialog? = null
    private lateinit var adapter1: LoyalityPointAdapter
    var discount=""
    var list_data = ArrayList<com.padedatingapp.model.loyalityModel.DataX>()

    override fun layoutId(): Int = R.layout.fragment_loyalty_point

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = loyalityVM
//        viewBinding.lifecycleOwner = this
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.btnRedeemNow.setOnClickListener {
            //findNavController().popBackStack()
            findNavController().navigate(LoyaltyPointFragmentDirections.actionToBuyPremium(
                    from ="loyalty_points",
                    discount =discount,
                       point =viewBinding.tvLoyaltyPoints.text.toString() ))
        }
        adapter1 = LoyalityPointAdapter(requireContext())
        viewBinding.rvLoyalityPoint.adapter = adapter1
        viewBinding.rvLoyalityPoint.layoutManager = LinearLayoutManager(
                requireContext()
        ).also {
            it.orientation = RecyclerView.HORIZONTAL
        }
        initComponents()
        initObservables()

        //  setUserData()


    }


//    private fun setUserData() {
//        try {
//            var userObject =
//                    Gson().fromJson(
//                            sharedPref.getString(AppConstants.USER_OBJECT, "en"),
//                            UserModel::class.java
//                    )
//
//            viewBinding.tvLoyaltyPoints.text = ""+userObject.totalPoints
//
//        } catch (e: JsonParseException) {
//
//        }
//    }

    private fun initComponents() {
        loyalityVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")
    }


    private fun initObservables() {



        loyalityVM.blockUsersResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "ChatUser")
        }

        loyalityVM.callLoyalityPoints()



    }


    private fun getLiveData(response: Resource<LoyalityPointsResponse>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "ChatUser" -> {
                        val data = response.data as LoyalityPointsResponse
                        Log.e(MessagesFragment.TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                                Log.e(MessagesFragment.TAG, "listAA " + data.data)
                                viewBinding.tvLoyaltyPoints.text=response.data.data.UserPoints.totalPoints.toString()
                                discount=response.data.data.discount.toString()

                                list_data= response.data.data.data as ArrayList<DataX>
                                adapter1.updateList(list_data)
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



    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

//    override fun onItemClick(model: Data) {
//        TODO("Not yet implemented")
//    }

}