package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.abc
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentBuyGiftBinding
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.loyalityModel.DataX
import com.padedatingapp.model.loyalityModel.LoyalityPointsResponse
import com.padedatingapp.model.plans.Doc
import com.padedatingapp.model.waveModel.WaveCardResponse
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BuyGiftVM
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import kotlinx.android.synthetic.main.fragment_buy_gift.*
import kotlinx.android.synthetic.main.item_premium_packst.*
import kotlinx.android.synthetic.main.layout_setup_credit_card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject


class BuyGiftFragment : DataBindingFragment<FragmentBuyGiftBinding>(){

    lateinit var cardInputWidget: CardInputWidget
    var country=""
    var discount=""
    var month=""
    var year=""
    var flw_ref=""
    var points=""
    var packageId=""
    var totalAmmount=""
    var countryCode=""
    var UnCheckTotalAmmount=""
    var totalPoint=0
    var title=""
    private lateinit var userObject : UserModel



    val stripe: Stripe by lazy {
        Stripe(requireContext(), getString(R.string.stripe_key))
    }
    companion object{
        var TAG = "BuyGiftFragment"
    }

    private val buyGiftVM by inject<BuyGiftVM>()

    private lateinit var planData: Doc

    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null

    override fun layoutId(): Int = R.layout.fragment_buy_gift
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                UserModel::class.java
            )
        points=arguments?.getString("point")!!
         country= arguments?.getString("country")!!
         discount= arguments?.getString("discount")!!


         countryCode=sharedPref.getString(AppConstants.COUNTRY_CODE,"")

        if(countryCode=="ZA"){
            etPin.visibility=View.VISIBLE
        }
        else{

            etPin.visibility=View.GONE
        }
        if (arguments?.getString("titleProfile","") == "fromProfile"){
            totalPoint=userObject.totalPoints
            if (totalPoint<=100){
                cbLoyaltyPoints.visibility=View.GONE
                textYouHave.visibility=View.GONE
                tvLoyalytPoints.visibility=View.GONE
            }
            else{
                cbLoyaltyPoints.visibility=View.VISIBLE
                textYouHave.visibility=View.VISIBLE
                tvLoyalytPoints.visibility=View.VISIBLE
            }
        }
        else{
           // cbLoyaltyPoints.visibility=View.VISIBLE

        }

        cardInputWidget = CardInputWidget(requireContext())
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = buyGiftVM
        viewBinding.lifecycleOwner = this

//        totalPoint=userObject.totalPoints
//        if (totalPoint<=100){
//            cbLoyaltyPoints.visibility=View.GONE
//        }
//        else{
//
//            cbLoyaltyPoints.visibility=View.VISIBLE
//        }


        cbLoyaltyPoints.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textTax.visibility=View.VISIBLE
                tvTaxAmount.visibility=View.VISIBLE
                val price= planData.price.amount
                val discountprice=discount!!.toDouble()
                 totalAmmount= (price-discountprice!!).toString()
                tvTotalAmount.text= planData.price.units +" "+totalAmmount



            } else {
                textTax.visibility=View.GONE
                tvTaxAmount.visibility=View.GONE
                UnCheckTotalAmmount=planData.price.amount.toString()
                tvTotalAmount.text= planData.price.units +" "+ planData.price.amount
            }
        })
        initComponents()
        initObservables()
    }


    private fun initComponents() {

        var userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                UserModel::class.java
            )

        buyGiftVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")

      //  val bundle = arguments

        var title = arguments?.getString("title", "")

//        var title = arguments?.getString("title","")

        planData  = arguments?.getParcelable<Doc>("planData") as Doc

        tvLoyalytPoints.text = arguments?.getString("point")
        tvTaxAmount.text =  planData.price.units +" "+arguments?.getString("discount")

        tvPackName.text = planData.name
        tvPackValidity.text = ""+planData.type + " "+requireActivity().getString(R.string.bundle)
        tvPackValidity.visibility = View.VISIBLE

        tvPackPrice.text = planData.price.units +" "+ planData.price.amount
        textGoldPackage.text = planData.name
        tvPackageAmount.text = planData.price.units +" "+ planData.price.amount
        tvTotalAmount.text = planData.price.units +" "+ planData.price.amount

//        if (cbLoyaltyPoints.isChecked){
//            textTax.visibility=View.VISIBLE
//            tvTaxAmount.visibility=View.VISIBLE
//        }
//        else{
//            textTax.visibility=View.GONE
//            tvTaxAmount.visibility=View.GONE
//        }

//        tvLoyalytPoints.text = ""+userObject.totalPoints


        viewBinding.header.tvTitle.text = title
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.btnBuyNow.setOnClickListener {
//            if (title =="Buy Gift Card")
//            showCongratsBottomSheet()
//            else
//                findNavController().popBackStack()

            if(etCardHolderName.text.toString().length == 0){
                toast(requireActivity().getString(R.string.Enter_card_holder_name))
            }else if(etCardNumber.text.toString().length == 0){
                toast(requireActivity().getString(R.string.Enter_card_number))
            }else if(etExpiryDate.text.toString().length != 5){
                toast(requireActivity().getString(R.string.Enter_expiry_month_and_year))
            }else if(etCVV.text.toString().length == 0){
                toast(requireActivity().getString(R.string.Enter_cvv))
            }else{
                if (countryCode=="ZA"){
                    initObservables1()
                }
                else{

                    fetchCardDetail(it)
                }
//                makePayment(
//                    etCardHolderName.text.toString(),
//                    etCardNumber.text.toString(),
//                    etExpiryDate.text.toString().split("/")[0],
//                    etExpiryDate.text.toString().split("/")[1],
//                    etCVV.text.toString(),
//                    "" + userObject.totalPoints,
//                    planData.name,
//                    planData.price.amount
//                )
            }


//            var packageName = planData._id
//            val jsonObj = JsonObject()
//            jsonObj.addProperty("package", packageName)
//            jsonObj.addProperty("stripeToken", "")
//            jsonObj.addProperty("card", "card_1JhuEiIuz09BIRfIE5PC8gO5")
//            jsonObj.addProperty("points", "")



//            buyGiftVM.paymentAPI(
//                jsonObj.toString()
//                    .toRequestBody("application/json".toMediaTypeOrNull())
//            )


        }

        viewBinding.llTop.setOnClickListener{
            requireActivity().hideKeyboard()
        }



        etExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 2) {
                    if (start == 2 && before == 1 && !s.toString().contains("/")) {
                        etExpiryDate.setText("" + s.toString()[0])
                        etExpiryDate.setSelection(1)
                    } else {
                        etExpiryDate.setText("$s/")
                        etExpiryDate.setSelection(3)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }

    private fun showCongratsBottomSheet() = try {
        val dialog = Dialog(requireActivity(), R.style.dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0f)
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.bottomsheet_gift_card_purchased, null)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );

        val btnDone = dialogView.findViewById<TextView>(R.id.btnDone)
        val ivClose = dialogView.findViewById<ImageView>(R.id.ivClose)

        ivClose?.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.buyGiftCardsListFragment, true)
        }
        btnDone?.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.buyGiftCardsListFragment, true)
        }

        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun fetchCardDetail(view: View) {
        var userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                UserModel::class.java
            )
        try {

            cardInputWidget.setCardNumber(etCardNumber?.text?.trim()?.toString() ?: "")
            val month = etExpiryDate?.text.toString().substringBefore("/")
            val year = etExpiryDate?.text.toString().substringAfter("/")
            cardInputWidget.setExpiryDate(month.toInt(), year.toInt())
            cardInputWidget.setCvcCode(etCVV?.text?.trim().toString())
            if(cardInputWidget.cardParams!=null){
                CoroutineScope(Dispatchers.IO).launch {
                    stripe.createCardToken(
                        cardParams = cardInputWidget.cardParams!!,
                        callback = object : ApiResultCallback<Token> {
                            override fun onError(e: Exception) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Log.e("Card Message", e.message ?: "")
                                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                                    e.printStackTrace()
                                }

                            }

                            override fun onSuccess(result: Token) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    Log.e("Token", result.id)
                                    Log.e("Token", result.card!!.id.toString())
                                    //vm.addCardApi(result.id)
                                    progressDialog?.dismiss()
                                    var packageName = planData._id
                                    val jsonObj = JsonObject()
                                    jsonObj.addProperty("package", packageName)
                                    jsonObj.addProperty("stripeToken", result.id)
                                    jsonObj.addProperty("card", result.card!!.id)

                                    if (cbLoyaltyPoints.isChecked){
                                        jsonObj.addProperty("points", totalAmmount)
                                    }
                                    else{
                                        jsonObj.addProperty("points", UnCheckTotalAmmount)

                                    }
                                    Log.v("check",jsonObj.toString())

                                    buyGiftVM.paymentAPI(
                                        jsonObj.toString()
                                            .toRequestBody("application/json".toMediaTypeOrNull())
                                    )

                                }
                            }

                        })
                }
            }else{
                // CommonAlerts.showSnackBar("Please enter valid card details")
                Toast.makeText(context,"Please enter valid card details",Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    private fun initObservables() {
        buyGiftVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }
        buyGiftVM.loginResponsePayment.observe(viewLifecycleOwner) {
            getLiveData(it, "BuyGiftCardList")
        }
        Log.e(TAG, "onViewCreated11")
    }

    private fun initObservables1() {
         packageId = planData._id

        buyGiftVM.waveCardResponse.observe(viewLifecycleOwner) {
            getLiveData1(it, "waveCard")
        }

        if (!etExpiryDate.getText().toString().isEmpty()) {
            if (etExpiryDate.getText().toString().length >= 5) {
                 month = etExpiryDate.text.toString().split("/")[0]
                 year = etExpiryDate.text.toString().split("/")[1]
                Log.v("abc", month)
                Log.v("abc", year)
            }
        }
        val jsonObj = JsonObject()
        jsonObj.addProperty("card_number",etCardNumber.text.toString())
        jsonObj.addProperty("cvv", etCVV.text.toString())
        jsonObj.addProperty("expiry_month", month)
        jsonObj.addProperty("expiry_year", year)
        jsonObj.addProperty("amount",planData.price.amount )
        jsonObj.addProperty("package", packageId)
        if (cbLoyaltyPoints.isChecked){
            jsonObj.addProperty("points", totalAmmount)
        }
        else{
            jsonObj.addProperty("points", UnCheckTotalAmmount)
        }
        jsonObj.addProperty("pin", etPin.text.toString())
        Log.v("abccc",jsonObj.toString())


        buyGiftVM.waveCardApi(jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull()))



    }




    private fun getLiveData(response: Resource<BlockUserModel>?, type: String) {

        Log.e(TAG, "onViewCreated12")

//        val data = response?.data as BlockUserModel
//        Log.e(TAG, "userObjectAAXXx "+data.toString())

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "BuyGiftCardList" -> {
                        val data = response.data?.message
                        Log.e(TAG, "userObjectAA " + data.toString())


                        findNavController().navigate(BuyGiftFragmentDirections.actionToProfile("from_gift"))
//
//                        onSetGiftCardListResponse(data)
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
//                list = data.data?.doc!!
//                adapter.updateData(list)
//                adapter.notifyDataSetChanged()

            } else {
                toast(data.message)
            }
        }
    }

    private fun getLiveData1(response: Resource<WaveCardResponse>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "waveCard" -> {
                        val data = response.data as WaveCardResponse
                        Log.e(MessagesFragment.TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.status == "success") {
                                Log.e(MessagesFragment.TAG, "listAA " + data.data)
                                flw_ref=response.data.data.flw_ref
                                findNavController().navigate(BuyGiftFragmentDirections.actionToVerifyotp(
                                    type="from_wave",
                                    flwId = flw_ref,
                                    packageId = packageId,
                                    point = points))
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

}