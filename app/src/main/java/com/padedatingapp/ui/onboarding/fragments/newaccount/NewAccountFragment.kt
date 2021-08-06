package com.padedatingapp.ui.onboarding.fragments.newaccount

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentNewAccountBinding
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_new_account.*
import org.koin.android.ext.android.inject

class NewAccountFragment : DataBindingFragment<FragmentNewAccountBinding>() {

    private val signUpVM by inject<SignUpVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null
    override fun layoutId(): Int = R.layout.fragment_new_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = signUpVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        initComponents()
    }

    private fun initComponents() {


        signUpVM.countryCode = ccp.selectedCountryCodeWithPlus

        viewBinding.btnGetStarted.setOnClickListener {
            showAccountVerificationChoice()
        }

        viewBinding.tvLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.llRoot.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        viewBinding.ccp.setOnCountryChangeListener {
            signUpVM.countryCode = ccp.selectedCountryCodeWithPlus
        }

        signUpVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }

        initObservables()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initObservables() {
        signUpVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(
                it,
                "registerUserResponse"
            )
        }
        signUpVM.sendOtpResponse.observe(viewLifecycleOwner) {
            getLiveData(
                it,
                "sentOtpResponse"
            )
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
                    "registerUserResponse" -> {
                        var data = response.data as ResultModel<UserModel>
                        onRegisterUserResponse(data)
                    }

                    "sentOtpResponse" -> {
                        var data = response.data as ResultModel<OtpData>
                        onSendOtpResponse(data)
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

    private fun onRegisterUserResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                signUpVM.phoneNo.value = data.data!!.phoneNo
                signUpVM.countryCode = data.data!!.countryCode
                signUpVM.email.value = data.data!!.email
                if (it.data!!.isEmailVerified || it.data!!.isPhoneVerified){
                   // findNavController().navigate(NewAccountFragmentDirections.actionToCreateNewAccount())
                    sharedPref.setString(AppConstants.USER_TOKEN,it.data?.accessToken)
                    findNavController().navigate(NewAccountFragmentDirections.actionToCreateNewAccount(email = it.data?.email?:"",phone = it.data?.phoneNo?:"",countryCode = it.data?.countryCode?:""))
                }
                else
                    showAccountVerificationChoice()
            } else {
                toast(data.message)
            }
        }
    }

    private fun onSendOtpResponse(data: ResultModel<OtpData>?) {

        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                when (signUpVM.verificationType) {
                    "email" -> {
                        findNavController().navigate(
                            NewAccountFragmentDirections.actionNewAccountToVerifyOtp(
                                "email",
                                email = signUpVM.email.value.toString()
                            )
                        )
                    }
                    "phone" -> {
                        findNavController().navigate(
                            NewAccountFragmentDirections.actionNewAccountToVerifyOtp(
                                "phone",
                                phone = signUpVM.phoneNo.value.toString()
                            )
                        )
                    }
                }

            } else {
                toast(data.message)
            }
        }


    }

    private fun showAccountVerificationChoice() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_account_verification_type, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
        val tvPhone = dialogView.findViewById<TextView>(R.id.tvPhone)
        val tvPhoneValue = dialogView.findViewById<TextView>(R.id.tvPhoneValue)
        val tvEmail = dialogView.findViewById<TextView>(R.id.tvEmail)
        val tvEmailValue = dialogView.findViewById<TextView>(R.id.tvEmailValue)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)

        tvPhoneValue.text = "+" + signUpVM.countryCode + signUpVM.phoneNo.value
        tvEmailValue.text = signUpVM.email.value

        tvPhone?.setOnClickListener {
            signUpVM.verificationType = "phone"
            dialog.dismiss()
              findNavController().navigate(NewAccountFragmentDirections.actionNewAccountToVerifyOtp("phone",phone = signUpVM.phoneNo.value.toString(),countryCode = signUpVM.countryCode))
          //  toast("Currently Unavailable")
        }
        tvPhoneValue?.setOnClickListener {
            signUpVM.verificationType = "phone"
            dialog.dismiss()
             findNavController().navigate(NewAccountFragmentDirections.actionNewAccountToVerifyOtp("phone",phone = signUpVM.phoneNo.value.toString(),countryCode = signUpVM.countryCode))
         //   toast("Currently Unavailable")
        }
        tvEmail?.setOnClickListener {
            dialog.dismiss()
            signUpVM.verificationType = "email"
            signUpVM.callSendOtpApi()

        }
        tvEmailValue?.setOnClickListener {
            dialog.dismiss()
            signUpVM.verificationType = "email"
            signUpVM.callSendOtpApi()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}