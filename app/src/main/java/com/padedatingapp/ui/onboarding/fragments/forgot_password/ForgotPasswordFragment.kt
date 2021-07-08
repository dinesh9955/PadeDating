package com.padedatingapp.ui.onboarding.fragments.forgot_password

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentForgotPasswordBinding
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.getFormattedCountDownTimer
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.header_layout.view.*
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : DataBindingFragment<FragmentForgotPasswordBinding>() {
    private lateinit var timer: CountDownTimer
    private var verificationType: String = "phone"
    private val forgotPasswordVM by inject<ForgotPasswordVM>()
    private var progressDialog: CustomProgressDialog? = null
    private val sharedPref by inject<SharedPref>()


    override fun layoutId(): Int = R.layout.fragment_forgot_password
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = forgotPasswordVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())

        initComponents()
    }

    private fun initComponents() {



        verificationType = requireArguments().getString("verificationType", "")
        forgotPasswordVM.verificationType = verificationType

        viewBinding.llRoot.setOnClickListener { requireActivity().hideKeyboard() }

        viewBinding.header.tvTitle.text = getString(R.string.password_recovery)

        /*  viewBinding.btnSubmit.setOnClickListener {
              toggleUI()
          }*/

        /* viewBinding.btnVerifyOtp.setOnClickListener {
             timer.cancel()
             requireActivity().hideKeyboard()
             findNavController().navigate(R.id.action_to_password_recovery)
         }*/

        viewBinding.header.ivBack.setOnClickListener {
            timer.cancel()
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        timer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = getFormattedCountDownTimer(millisUntilFinished)
                viewBinding.tvResend.text = hms
            }

            override fun onFinish() {
                timer.cancel()
                viewBinding.tvResend.isEnabled = true
                viewBinding.tvResend.text = getString(R.string.resend)
            }
        }
        viewBinding.tvResend.setOnClickListener {
            forgotPasswordVM.callSendOtpApi()
        }

        if (verificationType == "phone") {
            forgotPasswordVM.isEmail = false
            viewBinding.tvForgotPassDescText.text = getString(R.string.enter_mobile_number_desc)
            viewBinding.clPhone.isVisible = !viewBinding.clPhone.isVisible
            viewBinding.btnVerifyOtp.text = getString(R.string.verify_number)
        } else {
            forgotPasswordVM.isEmail = true
            viewBinding.tvForgotPassDescText.text = getString(R.string.enter_email_address_desc)
            viewBinding.etEmail.isVisible = !viewBinding.etEmail.isVisible
            viewBinding.btnVerifyOtp.text = getString(R.string.verify_email)
        }
        forgotPasswordVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus.toString()

        viewBinding.ccp.setOnCountryChangeListener {
            forgotPasswordVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus.toString()

        }


        initObservables()
    }

    private fun initObservables() {
        forgotPasswordVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "")
                toast(it)
        }
        forgotPasswordVM.sendOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "sendOtpResponse")
        }

        forgotPasswordVM.verifyOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "verifyOtpResponse")
        }

        forgotPasswordVM.forgotPasswordResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "forgotPasswordResponse")
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

                    "forgotPasswordResponse" -> {
                        var data = response.data as? ResultModel<UserModel>
                        onForgotPasswordResponse(data)
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

    private fun onForgotPasswordResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                viewBinding.otpView.setText("")
                toggleUI()
                timer.start()
            } else {
                toast(it.message)
            }
        }
    }

    private fun onVerifyOtpResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS) {
                timer.cancel()
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)
                findNavController().navigate(ForgotPasswordFragmentDirections.actionToPasswordRecovery())
                // findNavController().navigate(Forg.actionToCreateNewAccount(email = it.data?.email?:"",phone = it.data?.phoneNo?:"",countryCode = it.data?.countryCode?:""))
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


    private fun toggleUI() {
        if (verificationType == "phone") {
            viewBinding.tvForgotPassDescText.text =
                getString(R.string.enter_four_digit_code) + " +9198786535"
            viewBinding.btnSubmit.isVisible = !viewBinding.btnSubmit.isVisible
            viewBinding.btnVerifyOtp.isVisible = !viewBinding.btnVerifyOtp.isVisible
            viewBinding.otpView.isVisible = !viewBinding.otpView.isVisible
            viewBinding.clPhone.isVisible = !viewBinding.clPhone.isVisible
        } else {
            viewBinding.btnSubmit.isVisible = !viewBinding.btnSubmit.isVisible
            viewBinding.btnVerifyOtp.isVisible = !viewBinding.btnVerifyOtp.isVisible
            viewBinding.otpView.isVisible = !viewBinding.otpView.isVisible
            viewBinding.etEmail.isVisible = !viewBinding.etEmail.isVisible
            viewBinding.tvForgotPassDescText.text =
                getString(R.string.enter_four_digit_code) + " johndoe@example.com"
        }
        viewBinding.tvResend.isEnabled = false
        timer.start()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }


}