package com.padedatingapp.ui.onboarding.fragments.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentVerifyOtpBinding
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.onboarding.fragments.newaccount.SignUpVM
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.getFormattedCountDownTimer
import com.padedatingapp.utils.hideKeyboard
import org.koin.android.ext.android.inject

class VerifyOtpFragment : DataBindingFragment<FragmentVerifyOtpBinding>() {
    private val otpVm by inject<OtpVM>()
    private val sharedPref by inject<SharedPref>()
    private lateinit var timer: CountDownTimer
    private var progressDialog: CustomProgressDialog? = null

    override fun layoutId(): Int = R.layout.fragment_verify_otp

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = otpVm
        viewBinding.lifecycleOwner = this
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        progressDialog = CustomProgressDialog(requireContext())
        initComponents()
    }

    private fun initComponents() {
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

    private fun getLiveData(response: Resource<Result<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()
                when (type) {
                    "sendOtpResponse" -> {
                        var data = response.data as? Result<OtpData>
                        onSendOtpResponse(data)
                    }
                    "verifyOtpResponse" -> {
                        var data = response.data as? Result<UserModel>
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

    private fun onVerifyOtpResponse(data: Result<UserModel>?) {
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


    private fun onSendOtpResponse(data: Result<OtpData>?) {
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
            viewBinding.tvPhone.text = "+" + otpVm.countryCode + otpVm.phoneNo.value
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