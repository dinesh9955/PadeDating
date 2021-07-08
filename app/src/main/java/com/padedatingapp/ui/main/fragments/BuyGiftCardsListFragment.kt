package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.adapter.GiftCardListAdapter
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentBuyGiftCardsListBinding
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.Doc
import com.padedatingapp.model.ResultModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BuyGiftCardsListVM
import kotlinx.android.synthetic.main.header_layout.view.*
import org.koin.android.ext.android.inject

class BuyGiftCardsListFragment :  DataBindingFragment<FragmentBuyGiftCardsListBinding>(),
        GiftCardListAdapter.OnItemClickListener {

    companion object{
        var TAG = "BuyGiftCardsListFragment"
    }


    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null

    private val buyGiftCardsListVM by inject<BuyGiftCardsListVM>()

    override fun layoutId(): Int = R.layout.fragment_buy_gift_cards_list

    internal lateinit var adapter: GiftCardListAdapter

    var list = ArrayList<Doc>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = buyGiftCardsListVM
        viewBinding.lifecycleOwner = this
        initComponents()

    }



    private fun initObservables() {

        buyGiftCardsListVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

        buyGiftCardsListVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "BuyGiftCardList")
        }

        Log.e(TAG, "onViewCreated11")
    }



    private fun getLiveData(response: Resource<ResultModel<AllGiftCard>>?, type: String) {

        Log.e(TAG, "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "BuyGiftCardList" -> {
                        val data = response.data as ResultModel<AllGiftCard>

                        Log.e(TAG, "userObjectAA "+data.toString())

                        onSetGiftCardListResponse(data)
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

    private fun onSetGiftCardListResponse(data: ResultModel<AllGiftCard>) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                list = data.data?.doc!!
                adapter.updateData(list)
                adapter.notifyDataSetChanged()

            } else {
                toast(data.message)
            }
        }
    }


    private fun initComponents() {
        viewBinding.header.tvTitle.text = getString(R.string.gift_cards)
        buyGiftCardsListVM.token = sharedPref.getString(AppConstants.USER_TOKEN)

       // var list = ArrayList<DummyModel>()
//        repeat(5) {
//            list.add(DummyModel())
//        }
       // var adapter = PremiumPacksAdapter(this)
      //  adapter.submitList(list)
        adapter = GiftCardListAdapter(requireContext(), list, this)

        viewBinding.rvGiftCardList.adapter = adapter
        viewBinding.rvGiftCardList.layoutManager = LinearLayoutManager(requireContext())

        viewBinding.header.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        initObservables()

//        val jsonObj = JsonObject()
////        jsonObj.addProperty("page", "")
////        jsonObj.addProperty("count","")
//        jsonObj.addProperty("isActive", "")
//        jsonObj.addProperty("search", "")

        buyGiftCardsListVM.callUpdateProfileApi(
                //jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        )
    }

    override fun onItemClick(model: Doc) {
        Log.e("BuyPremiumFragment", "onItemClick: ", )
          findNavController().navigate(BuyGiftCardsListFragmentDirections.actionToBuy())

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}