package com.padedatingapp.ui.onboarding.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentLoginBinding
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.togglePasswordVisibility
import org.koin.android.ext.android.inject
import kotlin.math.log

class LoginFragment : DataBindingFragment<FragmentLoginBinding>() {
    private val loginVM by inject<LoginVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null
    override fun layoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = loginVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        initComponents()
    }

    private fun initComponents() {
        loginVM.isEmailLogin = false
        loginVM.password.value = ""

        if (sharedPref.getBoolean(AppConstants.REMEMBER_ME)) {
            loginVM.isRememberMe.value = true
            loginVM.email.value = (sharedPref.getString(AppConstants.USER_EMAIL))
            loginVM.phoneNo.value = sharedPref.getString(AppConstants.USER_PHONE)
            viewBinding.ccp.setCountryForPhoneCode(sharedPref.getInt(AppConstants.USER_COUNTRY_CODE))
            loginVM.password.value = (sharedPref.getString(AppConstants.USER_PASSWORD))
        }

        viewBinding.ivHideShowPass.setOnClickListener {
            togglePasswordVisibility(
                requireContext(),
                viewBinding.etPassword,
                viewBinding.ivHideShowPass
            )
        }

        loginVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus
        initObservables()
        setUpListeners()
    }

    private fun initObservables() {

        loginVM.isRememberMe.observe(viewLifecycleOwner) {
            sharedPref.setBoolean(AppConstants.REMEMBER_ME, it)
        }
        loginVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(
                it,
                "loginResponse"
            )
        }

        loginVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
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
                    "loginResponse" -> {
                        val data = response.data as Result<UserModel>
                        onLoginResponse(data)
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

    private fun onLoginResponse(data: Result<UserModel>) {

        data?.let {

            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)

                if (sharedPref.getBoolean(AppConstants.REMEMBER_ME)) {
                    sharedPref.setBoolean(AppConstants.REMEMBER_ME,true)
                    sharedPref.setString(AppConstants.USER_EMAIL, it.data.email)
                    sharedPref.setString(AppConstants.USER_PHONE, it.data.phoneNo)
                    sharedPref.setInt(
                        AppConstants.USER_COUNTRY_CODE,
                        it.data.countryCode.replace("+", "").toInt()
                    )
                    sharedPref.setString(
                        AppConstants.USER_PASSWORD,
                        viewBinding.etPassword.text.toString()
                    )
                }
                else{
                    sharedPref.setBoolean(AppConstants.REMEMBER_ME,false)
                    sharedPref.setString(AppConstants.USER_EMAIL, "")
                    sharedPref.setString(AppConstants.USER_PHONE, "")
                    sharedPref.setInt(
                        AppConstants.USER_COUNTRY_CODE,
                        viewBinding.ccp.selectedCountryCode.replace("+","").toInt()
                    )
                }
                when (it.data!!.profileStatus) {
                    1 -> {
                        findNavController().navigate(LoginFragmentDirections.actionToUploadPhoto("login"))
                    }
                    2 -> {
                        findNavController().navigate(
                            LoginFragmentDirections.actionToSignUpAboutFragment(
                                "Sign Up", "login"
                            )
                        )
                    }
                    3 -> {
                        toast(it.message)
                        sharedPref.setString(AppConstants.USER_ID, it.data._id)
                        sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    }
                }
            } else {
                toast(data.message)
            }
        }
    }

    private fun setUpListeners() {
        viewBinding.ccp.setOnCountryChangeListener {
            loginVM.countryCode = viewBinding.ccp.selectedCountryCodeWithPlus
        }

        viewBinding.cbRememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            loginVM.isRememberMe.value = isChecked
        }

        viewBinding.tvContinueWith.setOnClickListener {
            toggleEmailPhone()
        }
        viewBinding.llRoot.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        /*   viewBinding.btnLogin.setOnClickListener {
               startActivity(Intent(requireContext(), HomeActivity::class.java))
               requireActivity().finish()
           }*/

        viewBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionToSignUp())
        }

        viewBinding.tvForgotPassword.setOnClickListener {
            /*if(viewBinding.clPhone.isVisible)
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("phone"))
            else
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("email"))*/
            showAccountVerificationChoice()
        }
    }

    private fun toggleEmailPhone() {
        requireActivity().hideKeyboard()
        viewBinding.etEmail.isVisible = !viewBinding.etEmail.isVisible
        viewBinding.clPhone.isVisible = !viewBinding.clPhone.isVisible
        viewBinding.tvContinueWith.text = if (viewBinding.clPhone.isVisible) {
            loginVM.isEmailLogin = false
            getString(R.string.continue_with_email)
        } else {
            loginVM.isEmailLogin = true
            getString(R.string.continue_with_phone)
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
        tvPhone?.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("phone"))
        }
        tvPhoneValue?.isVisible = false
        tvEmail?.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(LoginFragmentDirections.actionToForgotPassword("email"))
        }
        tvEmailValue?.isVisible = false
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
