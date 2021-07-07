package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.adapter.PremiumPacksAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.AboutMeRepo
import com.padedatingapp.api.repository.GiftCardRepo
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentBuyGiftCardsListBinding
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.DummyModel
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.onboarding.fragments.about_me.AboutMeSignUpFragment
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class BuyGiftCardsListFragment :  DataBindingFragment<FragmentBuyGiftCardsListBinding>(),
    PremiumPacksAdapter.OnItemClickListener {

//    private val coroutinesManager: CoroutinesManager = TODO()
//    var allGiftCardResponse = SingleLiveEvent<Resource<Result<AllGiftCard>>>()
    var token = ""
//    private val giftCardRepo: GiftCardRepo

    private val sharedPref by inject<SharedPref>()

    override fun layoutId(): Int = R.layout.fragment_buy_gift_cards_list
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()

      //  initObservables()

        val jsonObj = JsonObject()
        jsonObj.addProperty("page", "")
        jsonObj.addProperty("count","")
        jsonObj.addProperty("isActive", "")
        jsonObj.addProperty("search", "")



//        callUpdateProfileApi(
//            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
//        )

    }


//    fun callUpdateProfileApi(body: RequestBody) {
//        coroutinesManager.ioScope.launch {
//            allGiftCardResponse.postValue(Resource.loading(null))
//            allGiftCardResponse.postValue(giftCardRepo.giftCards("Bearer $token", body))
//        }
//    }
//
//
//    private fun initObservables() {
//
//        allGiftCardResponse.observe(viewLifecycleOwner, Observer {
//           // movieAdapter.updateData(it)
//            Log.e(AboutMeSignUpFragment.TAG, "onViewCreated1111")
//        })
//
////        aboutMeVM.errorMessage.observe(viewLifecycleOwner) {
////            if (it != "") {
////                toast(it)
////            }
////        }
////
////        aboutMeVM.optionChoosen.observe(viewLifecycleOwner) {
////            showDropDownDialog(it)
////        }
////
////        aboutMeVM.loginResponse.observe(viewLifecycleOwner) {
////            getLiveData(it, "setupProfileResponse")
////        }
//
//        Log.e(AboutMeSignUpFragment.TAG, "onViewCreated11")
//    }



    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.gift_cards)
        token = sharedPref.getString(AppConstants.USER_TOKEN)

        var list = ArrayList<DummyModel>()
        repeat(5) {
            list.add(DummyModel())
        }
        var adapter = PremiumPacksAdapter(this)
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
        viewBinding.rvGiftCardList.adapter = adapter
        viewBinding.rvGiftCardList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onItemClick(model: DummyModel) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )
          findNavController().navigate(BuyGiftCardsListFragmentDirections.actionToBuy())

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}