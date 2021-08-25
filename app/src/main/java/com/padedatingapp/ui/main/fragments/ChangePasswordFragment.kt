package com.padedatingapp.ui.main.fragments

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentChangePasswordBinding
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.otp.OtpForgotMain
import com.padedatingapp.ui.onboarding.fragments.password_recovery_fragment.PasswordRecoveryVM
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.togglePasswordVisibility
import com.padedatingapp.vm.ChangePasswordVM
import kotlinx.android.synthetic.main.header_layout.view.*
import org.koin.android.ext.android.inject

class ChangePasswordFragment : DataBindingFragment<FragmentChangePasswordBinding>(){

    private val passwordRecoveryVM by inject<ChangePasswordVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null


    override fun layoutId(): Int = R.layout.fragment_change_password

    companion object {
        private val REQUIRED_GALLERY_PERMISSIONS =
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        private const val CAPTURE_IMAGE_REQUEST = 1001
        private const val GALLERY_IMAGE_REQUEST = 1002
        private const val GALLERY_PERMISSION_REQUEST = 1004
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003

        var TAG = "SettingsFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        viewBinding.vm = passwordRecoveryVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())


        initComponents()
        initObservables()
//        setUserData()
        viewBinding.header.tvTitle.text = "Change Password"

    }



    private fun initComponents() {
        passwordRecoveryVM.token =sharedPref.getString(AppConstants.USER_TOKEN)
        passwordRecoveryVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.header.tvTitle.text = getString(R.string.password_recovery)
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }

        viewBinding.ivOldHideShowPass.setOnClickListener {
            togglePasswordVisibility(
                requireContext(),
                viewBinding.etOldPass,
                viewBinding.ivOldHideShowPass
            )
        }


        viewBinding.ivHideShowPass.setOnClickListener {
            togglePasswordVisibility(
                requireContext(),
                viewBinding.etNewPass,
                viewBinding.ivHideShowPass
            )
        }

        viewBinding.ivHideShowConfPass.setOnClickListener {
            togglePasswordVisibility(
                requireContext(),
                viewBinding.etConfPassword,
                viewBinding.ivHideShowConfPass
            )
        }


        passwordRecoveryVM.password.observe(viewLifecycleOwner) {
            var count = 0
            if (it.trim().length > 7) {
                count += 1
            }
            if (passwordRecoveryVM.UpperCasePatten.matcher(it).find()) {
                count += 1
            }
            if (passwordRecoveryVM.specailCharPatten.matcher(it).find()) {
                count += 1
            }
            if (passwordRecoveryVM.digitCasePatten.matcher(it).find()) {
                count += 1
            }
            when (count) {
                0 -> {
                    viewBinding.vOne.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vTwo.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vThree.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vFour.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )

                }
                1 -> {
                    viewBinding.vOne.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vTwo.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vThree.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vFour.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )

                }
                2 -> {
                    viewBinding.vOne.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vTwo.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vThree.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )
                    viewBinding.vFour.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )

                }
                3 -> {
                    viewBinding.vOne.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vTwo.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vThree.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vFour.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_round_edge_light_purple
                    )

                }
                4 -> {
                    viewBinding.vOne.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vTwo.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vThree.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )
                    viewBinding.vFour.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_gradient_bg
                    )

                }
            }
        }
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