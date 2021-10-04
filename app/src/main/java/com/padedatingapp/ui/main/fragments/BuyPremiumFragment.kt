package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.PremiumPacksAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentBuyPremiumBinding
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.plans.Doc
import com.padedatingapp.model.plans.PlanModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BuyPremiumVM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class BuyPremiumFragment : DataBindingFragment<FragmentBuyPremiumBinding>(),
    PremiumPacksAdapter.OnItemClickListener {

    companion object{
        var TAG = "BuyPremiumFragment"
    }

    private val buyPremiumVM by inject<BuyPremiumVM>()

    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()

    override fun layoutId(): Int = R.layout.fragment_buy_premium
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = buyPremiumVM
        viewBinding.lifecycleOwner = this
        initComponents()
        initObservables()
    }

    private fun initComponents() {
        buyPremiumVM.token = sharedPref.getString(AppConstants.USER_TOKEN)

        var from = arguments?.getString("from","")

//        var list = ArrayList<DummyModel>()
//        repeat(5) {
//            list.add(DummyModel())
//        }
//        var adapter = PremiumPacksAdapter(this)
//        adapter.submitList(list)
//        adapter.notifyDataSetChanged()
//        viewBinding.rvPremiumPackList.adapter = adapter
//        viewBinding.rvPremiumPackList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.ivBack.setOnClickListener {
            if (from == "loyalty_points")
            {
                findNavController().popBackStack(R.id.loyaltyPointFragment,true)
            }
            else
            findNavController().popBackStack()
        }



    }

    override fun onItemClick(model: Doc) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )
        findNavController().navigate(BuyPremiumFragmentDirections.actionToBuy("Buy Premium", model))
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }









    private fun initObservables() {
        buyPremiumVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }



        buyPremiumVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "MyMatches")
        }

        Log.e(TAG, "onViewCreated11")


//        val jsonObj = JsonObject()
//        jsonObj.addProperty("limit", 10)
//        jsonObj.addProperty("page",1)
//        buyPremiumVM.callMyMatchesApi(
//            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
//        )

        val jsonObj = JsonObject()
        jsonObj.addProperty("limit", 10)
        jsonObj.addProperty("page",1)
        buyPremiumVM.callPlansApi(
            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )

    }




    private fun getLiveData(response: Resource<PlanModel>?, type: String) {

        //Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "MyMatches" -> {
                        val data = response.data as PlanModel
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






    private fun onMyMatchesResponse(data: PlanModel) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                var list = data.data.doc as ArrayList<Doc>
                Log.e(TAG, "listAA "+list.size)
//                adapter.updateData(list)
//                adapter.notifyDataSetChanged()

//                var adapter2 = PeopleWhoLikedAdapter(this)
//                adapter2.submitList(list)
//                adapter2.notifyDataSetChanged()
//                viewBinding.rvImagesList.adapter = adapter2

//                adapter.submitList(list)
//
//                adapter.notifyDataSetChanged()
//                viewBinding.rvWhoLiked.adapter = adapter


                var adapter = PremiumPacksAdapter(this)
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
                viewBinding.rvPremiumPackList.adapter = adapter
                viewBinding.rvPremiumPackList.layoutManager = LinearLayoutManager(requireContext())


            } else {
                toast(data.message)
            }
        }
    }


}