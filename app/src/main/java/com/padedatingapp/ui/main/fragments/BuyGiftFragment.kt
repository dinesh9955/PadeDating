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
import android.widget.ImageView
import android.widget.TextView
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
import com.padedatingapp.model.plans.Doc
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BuyGiftVM
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.exception.AuthenticationException
import com.stripe.model.Charge
import com.stripe.model.Customer
import kotlinx.android.synthetic.main.fragment_buy_gift.*
import kotlinx.android.synthetic.main.item_premium_packst.*
import kotlinx.android.synthetic.main.layout_setup_credit_card.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.util.*


class BuyGiftFragment : DataBindingFragment<FragmentBuyGiftBinding>(){

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
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = buyGiftVM
        viewBinding.lifecycleOwner = this

        initComponents()
        initObservables()
    }

    private fun initComponents() {


        var userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT),
                UserModel::class.java
            )

      //  val bundle = arguments

        var title = arguments?.getString("title", "")

//        var title = arguments?.getString("title","")

        planData  = arguments?.getParcelable<Doc>("planData") as Doc

        tvPackName.text = planData.name
        tvPackValidity.text = ""+planData.type + " months"
        tvPackValidity.visibility = View.VISIBLE

        tvPackPrice.text = planData.price.units +" "+ planData.price.amount

        textGoldPackage.text = planData.name
        tvTaxAmount.text = planData.price.units +" "+ planData.price.amount


        tvLoyalytPoints.text = userObject.totalPoints


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
                toast("Enter card holder name")
            }else if(etCardNumber.text.toString().length == 0){
                toast("Enter card number")
            }else if(etExpiryDate.text.toString().length != 7){
                toast("Enter expiry month and year")
            }else if(etCVV.text.toString().length == 0){
                toast("Enter cvv")
            }else{
                makePayment(etCardHolderName.text.toString(),
                    etCardNumber.text.toString(),
                    etExpiryDate.text.toString().split("/")[0],
                    etExpiryDate.text.toString().split("/")[1],
                    etCVV.text.toString(),
                    userObject.totalPoints
                )
            }


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




    fun makePayment(
        cardHolderName: String,
        cardNumber: String,
        expMonth: String,
        expYear: String,
        cvv: String,
        points: String
    ) {
        val publishableApiKey = "pk_test_inM99ehBADdrzRTf3wa3ggu2"


//        val card = Card(etCardHolderName.text.toString(),
//            etCardNumber.text.toString().toInt(),
//            etExpiryDate.text.toString().toInt(),
//            etCVV.text.toString())

         val card = Card(
             cardNumber,
             expMonth.toInt(),
             expYear.toInt(),
             cvv
         )

//        val card = Card("4242424242424242", 12, 2022, "123")

        var stripe: Stripe? = null
        try {
            stripe = Stripe(publishableApiKey)
        } catch (e: AuthenticationException) {
            e.printStackTrace()
        }
        stripe!!.createToken(card, publishableApiKey, object : TokenCallback() {
            override fun onError(error: java.lang.Exception) {
                Log.e(TAG, "onError " + error.message)
            }

            override fun onSuccess(token: Token) {
                Log.e(TAG, "onSuccess " + token.id)
                com.stripe.Stripe.apiKey = "sk_test_xitA2poC7TfjnP1IGD0FT6rp"
                try {
                    Thread {
                        try {
                            val customerParams: MutableMap<String, Any> =
                                HashMap()
                            customerParams["email"] = "dnkumar.chauhan@gmail.com"
                            customerParams["source"] = token.id
                            val customer = Customer.create(customerParams)
                            val chargeParams: MutableMap<String, Any> =
                                HashMap()
                            chargeParams["amount"] = 500 // amount in cents, again
                            chargeParams["currency"] = "usd"
                            // chargeParams.put("source", token.getId());
                            chargeParams["description"] = "Example charge"
                            chargeParams["customer"] = customer.id
                            //                                        chargeParams.put("email", "paying.user@example.com");
                            val charge = Charge.create(chargeParams)
                            Log.e(TAG, "onSuccessCharge " + charge.source.id)
                            val xx = Gson().toJson(charge)
                            Log.e(TAG, "onSuccessChargeGson $xx")


                            var packageName = planData._id
                            // var packageName = planData._id
//                            planData


                            val jsonObj = JsonObject()
                            jsonObj.addProperty("package", packageName)
                            jsonObj.addProperty("stripeToken", token.id)
                            jsonObj.addProperty("card", charge.source.id)

                            if (cbLoyaltyPoints.isChecked == true){
                                jsonObj.addProperty("points", ""+points)
                            }else{
                                jsonObj.addProperty("points", "")
                            }



                            buyGiftVM.callUpdateProfileApi(
                                jsonObj.toString()
                                    .toRequestBody("application/json".toMediaTypeOrNull())
                            )

                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "onSuccessERRoR " + e.message)
                        }
                        //System.out.println("Charge Log :" + charge);
                    }.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })
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
//                        val data = response.data as ResultModel<AllGiftCard>
//
//                        Log.e(BuyGiftCardsListFragment.TAG, "userObjectAA "+data.toString())
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