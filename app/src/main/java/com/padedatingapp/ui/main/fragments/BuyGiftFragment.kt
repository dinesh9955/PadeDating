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
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentBuyGiftBinding
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.plans.Doc
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
        cardInputWidget = CardInputWidget(requireContext())
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = buyGiftVM
        viewBinding.lifecycleOwner = this
        cbLoyaltyPoints.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textTax.visibility=View.VISIBLE
                tvTaxAmount.visibility=View.VISIBLE
            } else {
                textTax.visibility=View.GONE
                tvTaxAmount.visibility=View.GONE
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
                fetchCardDetail(it)
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

                                    if (cbLoyaltyPoints.isChecked == true) {
                                        jsonObj.addProperty("points", arguments?.getString("point"))
                                    } else {
                                        jsonObj.addProperty("points", "")
                                    }

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



//    fun makePayment(
//        cardHolderName: String,
//        cardNumber: String,
//        expMonth: String,
//        expYear: String,
//        cvv: String,
//        points: String,
//        planName: String,
//        planAmount: Int
//    ) {
//        progressDialog?.show()
//       val publishableApiKey = getString(R.string.stripe_key)
//      //val publishableApiKey = "pk_test_inM99ehBADdrzRTf3wa3ggu2"
//
////        val card = Card(etCardHolderName.text.toString(),
////            etCardNumber.text.toString().toInt(),
////            etExpiryDate.text.toString().toInt(),
////            etCVV.text.toString())
//
//         val card = Card(
//             cardNumber,
//             expMonth.toInt(),
//             expYear.toInt(),
//             cvv
//         )
//
////        val card = Card("4242424242424242", 12, 2022, "123")
//
//        var stripe: Stripe? = null
//        try {
//            stripe = Stripe(publishableApiKey)
//        } catch (e: AuthenticationException) {
//            e.printStackTrace()
//        }
//        stripe!!.createToken(card, publishableApiKey, object : TokenCallback() {
//            override fun onError(error: java.lang.Exception) {
//                Log.e(TAG, "onError " + error.message)
//            }
//
//            override fun onSuccess(token: Token) {
//                Log.e(TAG, "onSuccess " + "Live mode " + token.livemode)
//                Log.e(TAG, "onSuccess " + token.id)
//                progressDialog?.dismiss()
//                var packageName = planData._id
//                val jsonObj = JsonObject()
//                jsonObj.addProperty("package", packageName)
//                jsonObj.addProperty("stripeToken", token.id)
//                jsonObj.addProperty("card", "")
//
//                if (cbLoyaltyPoints.isChecked == true) {
//                    jsonObj.addProperty("points", "" + points)
//                } else {
//                    jsonObj.addProperty("points", "")
//                }
//
//                buyGiftVM.paymentAPI(
//                    jsonObj.toString()
//                        .toRequestBody("application/json".toMediaTypeOrNull())
//                )
//
//
//                //com.stripe.Stripe.apiKey = "sk_test_51CiPu1Iuz09BIRfI5m3yq1y0mOq9wxctHpfRlYfT4hps7TaTfTfSjRKxd3zDBXi2j7KIPeEfgo8OfBXT6g2XrBl700FU6gNyLJ"
////                com.stripe.Stripe.apiKey = "sk_test_xitA2poC7TfjnP1IGD0FT6rp"
////                try {
////                    Thread {
////                        try {
////                            val customerParams: MutableMap<String, Any> =
////                                HashMap()
////                            customerParams["email"] = "dnkumar.chauhan@gmail.com"
////                            customerParams["source"] = token.id
////                            val customer = Customer.create(customerParams)
////                            val chargeParams: MutableMap<String, Any> =
////                                HashMap()
////                            chargeParams["amount"] = planAmount * 100 // amount in cents, again
////                            chargeParams["currency"] = "usd"
////                            // chargeParams.put("source", token.getId());
////                            chargeParams["description"] = ""+planName
////                            chargeParams["customer"] = customer.id
////                            //                                        chargeParams.put("email", "paying.user@example.com");
////                            val charge = Charge.create(chargeParams)
////                            Log.e(TAG, "onSuccessCharge " + charge.source.id)
////                            val xx = Gson().toJson(charge)
////                            Log.e(TAG, "onSuccessChargeGson $xx")
////
////
////                            var packageName = planData._id
////                            // var packageName = planData._id
//////                            planData
////
////
////                            val jsonObj = JsonObject()
////                            jsonObj.addProperty("package", packageName)
////                            jsonObj.addProperty("stripeToken", token.id)
////                            jsonObj.addProperty("card", charge.source.id)
////
////                            if (cbLoyaltyPoints.isChecked == true){
////                                jsonObj.addProperty("points", ""+points)
////                            }else{
////                                jsonObj.addProperty("points", "")
////                            }
////
////
////
////                            buyGiftVM.callUpdateProfileApi(
////                                jsonObj.toString()
////                                    .toRequestBody("application/json".toMediaTypeOrNull())
////                            )
////
////                        } catch (e: java.lang.Exception) {
////                            e.printStackTrace()
////                            Log.e(TAG, "onSuccessERRoR " + e.message)
////                        }
////                        //System.out.println("Charge Log :" + charge);
////                    }.start()
////                } catch (e: java.lang.Exception) {
////                    e.printStackTrace()
////                }
//            }
//        })
    //}





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



    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}