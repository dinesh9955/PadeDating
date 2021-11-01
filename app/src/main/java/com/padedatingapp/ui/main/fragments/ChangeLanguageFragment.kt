package com.padedatingapp.ui.main.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentChangeLanguageBinding
import com.padedatingapp.model.otp.OtpForgotMain
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.LocaleHelper
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.ChangePasswordVM
import kotlinx.android.synthetic.main.fragment_change_language.*
import org.koin.android.ext.android.inject
import java.util.*

class ChangeLanguageFragment : DataBindingFragment<FragmentChangeLanguageBinding>(){

    private val passwordRecoveryVM by inject<ChangePasswordVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null

    var myLang = "en"


    override fun layoutId(): Int = R.layout.fragment_change_language

    companion object {
        var TAG = "ChangeLanguageFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
//        viewBinding.vm = passwordRecoveryVM
//        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())


        initComponents()
//        initObservables()
//        setUserData()
        viewBinding.header.tvTitle.text = getString(R.string.change_language)

    }



    private fun initComponents() {
//        passwordRecoveryVM.token = sharedPref.getString(AppConstants.USER_TOKEN)
//        passwordRecoveryVM._errorMessage.observe(viewLifecycleOwner) {
//            if (it != "") toast(it)
//        }
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.header.tvTitle.text = getString(R.string.change_language)
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }



        if (sharedPref.getString("mylang", "en").equals("fr", true)) {
            radiobtneng.setImageResource(R.drawable.ellipse_506)
            radiobtnarab.setImageResource(R.drawable.group_22856)
            myLang = "en"
        }
        else {
            radiobtneng.setImageResource(R.drawable.group_22856)
            radiobtnarab.setImageResource(R.drawable.ellipse_506)
            myLang = "fr"
        }




        viewBinding.clEnglish.setOnClickListener {
            radiobtneng.setImageResource(R.drawable.group_22856)
            radiobtnarab.setImageResource(R.drawable.ellipse_506)
            myLang = "en"
        }

        viewBinding.tvArabic.setOnClickListener {
            radiobtneng.setImageResource(R.drawable.ellipse_506)
            radiobtnarab.setImageResource(R.drawable.group_22856)
            myLang = "fr"
        }



        viewBinding.radiobtneng.setOnClickListener {
            radiobtneng.setImageResource(R.drawable.group_22856)
            radiobtnarab.setImageResource(R.drawable.ellipse_506)
            myLang = "en"
        }

        viewBinding.radiobtnarab.setOnClickListener {
            radiobtneng.setImageResource(R.drawable.ellipse_506)
            radiobtnarab.setImageResource(R.drawable.group_22856)
            myLang = "fr"
        }


        viewBinding.btnResetPassword.setOnClickListener {
            sharedPref.setString("mylang", myLang)
            setLocale(myLang)
        }



    }


    fun setLocale(localeName: String?) {
        val context: Context = LocaleHelper.setLocale(requireActivity(), localeName)
        val myLocale = Locale(localeName)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()

    }

    private fun initObservables() {
        passwordRecoveryVM.resetPasswordResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "resetPasswordResponse")
        }
    }

    private fun getLiveData(response: Resource<OtpForgotMain>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()
                when (type) {
                    "resetPasswordResponse" -> {
                        var data = response.data as? OtpForgotMain
                        onResetPasswordResponse(data)
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

    private fun onResetPasswordResponse(data: OtpForgotMain?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                showCongratsPopup()
            } else {
                toast(it.message)
            }
        }
    }


    private fun showCongratsPopup() = try {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.dialog_congrats, null)
        dialog.setContentView(dialogView)

        var txt = dialog.findViewById(R.id.change_pwd_txt) as TextView
        txt.text = getString(R.string.your_password_has_been_changed)

        dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show()

        Handler().postDelayed(Runnable {
            dialog.dismiss()
            findNavController().popBackStack(R.id.changePasswordFragment, true)
        }, 1500)
    } catch (e: Exception) {
        e.printStackTrace()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }




}