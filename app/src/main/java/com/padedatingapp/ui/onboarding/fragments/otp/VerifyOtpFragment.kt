package com.padedatingapp.ui.onboarding.fragments.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentVerifyOtpBinding
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.waveModel.WaveCardResponse
import com.padedatingapp.model.wavepayment.WavePaymentResponse
import com.padedatingapp.ui.main.fragments.BuyGiftFragmentDirections
import com.padedatingapp.ui.main.fragments.MessagesFragment
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.getFormattedCountDownTimer
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import kotlinx.android.synthetic.main.layout_setup_credit_card.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject

class VerifyOtpFragment : DataBindingFragment<FragmentVerifyOtpBinding>() {
    private val otpVm by inject<OtpVM>()
    private val sharedPref by inject<SharedPref>()
    private lateinit var timer: CountDownTimer
    private var progressDialog: CustomProgressDialog? = null
//    var type=""
//    var packageId=""
//    var flw_ref=""
//    var point=""

    override fun layoutId(): Int = R.layout.fragment_verify_otp

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = otpVm
        viewBinding.lifecycleOwner = this
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        progressDialog = CustomProgressDialog(requireContext())
        otpVm.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")

        otpVm.type= arguments?.getString("type","")!!

        if (otpVm.type=="from_wave"){
            otpVm.packageId=arguments?.getString("packageId")!!
            otpVm. flw_ref=arguments?.getString("flwId")!!
            otpVm.point=arguments?.getString("point")!!
            otpView.itemCount=5
            textVerifyPhoneNumber.visibility=View.GONE
            tvPleaseEnterVerification.text=getString(R.string.enter_code)
            tvResend.visibility=View.GONE
        }
        initComponents()

    }

    private fun initComponents() {
        initObservables1()
        getArgumentData()
        setUpListeners()
        initObservables()
        timer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = getFormattedCountDownTimer(millisUntilFinished)
                viewBinding.tvResend.text = hms
            }

            override fun onFinish() {
                if(isAdded) {
                    timer.cancel()
                    viewBinding.tvResend.isEnabled = true
                    viewBinding.tvResend.text = getString(R.string.resend)
                }
            }
        }

        viewBinding.tvResend.isEnabled = false
        timer.start()

    }

    private fun initObservables() {
        otpVm._errorMessage.observe(viewLifecycleOwner) {
            if(it!="")
            toast(it)
        }
        otpVm.sendOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "sendOtpResponse")
        }

        otpVm.verifyOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "verifyOtpResponse")
        }
    }

    private fun initObservables1() {
        otpVm.wavepaymentResponse.observe(viewLifecycleOwner) {
            getLiveData1(it, "waveCard")
        }
    }




    private fun getLiveData(response: Resource<ResultModel<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()
                when (type) {
                    "sendOtpResponse" -> {
                        var data = response.data as? ResultModel<OtpData>
                        onSendOtpResponse(data)
                    }
                    "verifyOtpResponse" -> {
                        var data = response.data as? ResultModel<UserModel>
                        onVerifyOtpResponse(data)
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

    private fun onVerifyOtpResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS) {
                timer.cancel()
                sharedPref.setString(AppConstants.USER_TOKEN,it.data?.accessToken!!)
                findNavController().navigate(VerifyOtpFragmentDirections.actionToCreateNewAccount(email = it.data?.email?:"",phone = it.data?.phoneNo?:"",countryCode = it.data?.countryCode?:""))
            } else {
                toast(it.message)
            }
        }
    }


    private fun onSendOtpResponse(data: ResultModel<OtpData>?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                viewBinding.tvResend.isEnabled = false
                timer.start()
            } else {
                toast(it.message)
            }
        }
    }

    private fun getArgumentData() {
        otpVm.verificationType = arguments?.getString("verificationType", "") ?: ""
        if (otpVm.verificationType == "email") {
            otpVm.email.value = arguments?.getString("email", "") ?: ""
            viewBinding.textVerifyPhoneNumber.text = getString(R.string.verifying_email)
            viewBinding.tvPhone.text = otpVm.email.value
        } else {//default phone
            otpVm.phoneNo.value = arguments?.getString("phone", "") ?: ""
            otpVm.countryCode = arguments?.getString("countryCode", "") ?: ""
            viewBinding.textVerifyPhoneNumber.text =
                getString(R.string.verifying_phone_number)
            viewBinding.tvPhone.text =otpVm.countryCode + otpVm.phoneNo.value
        }
    }



//        val jsonObj = JsonObject()
//        jsonObj.addProperty("flw_ref",flw_ref)
//        jsonObj.addProperty("package", packageId)
//        jsonObj.addProperty("points",point)
//        jsonObj.addProperty("otp", "12345")
//
//        Log.v("abccc",jsonObj.toString())
//
//
//        otpVm.wavePaymentApi(jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull()))
//



    private fun getLiveData1(response: Resource<WavePaymentResponse>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "waveCard" -> {
                        val data = response.data as WavePaymentResponse
                        Log.e(MessagesFragment.TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.status == "success") {
                                Log.e(MessagesFragment.TAG, "listAA " + data.data)
                                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                                findNavController().navigate(VerifyOtpFragmentDirections.actionToProfileFragment(
                                    typeOtp =  "otpProfile"))

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

    private fun setUpListeners() {
        viewBinding.ivBack.setOnClickListener {
            timer.cancel()
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }
    /*    viewBinding.btnVerifyOtp.setOnClickListener {
            timer.cancel()
            findNavController().navigate(VerifyOtpFragmentDirections.actionToCreateNewAccount())
        }*/

        /*viewBinding.tvResend.setOnClickListener {
            otpVm.callSendOtpApi()
        }*/

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}