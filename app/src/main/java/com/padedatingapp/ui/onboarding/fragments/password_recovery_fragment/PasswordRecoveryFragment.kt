package com.padedatingapp.ui.onboarding.fragments.password_recovery_fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentPasswordRecoveryBinding
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.togglePasswordVisibility
import kotlinx.android.synthetic.main.header_layout.view.*
import org.koin.android.ext.android.inject

class PasswordRecoveryFragment : DataBindingFragment<FragmentPasswordRecoveryBinding>() {
    private val passwordRecoveryVM by inject<PasswordRecoveryVM>()
    private val sharedPref by inject<SharedPref>()
    private var progressDialog: CustomProgressDialog? = null
    override fun layoutId(): Int = R.layout.fragment_password_recovery
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = passwordRecoveryVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        initComponents()
        initObservables()
    }

    private fun initObservables() {
        passwordRecoveryVM.resetPasswordResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "resetPasswordResponse")
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
                    "resetPasswordResponse" -> {
                        var data = response.data as? ResultModel<UserModel>
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

    private fun onResetPasswordResponse(data: ResultModel<UserModel>?) {
        data?.let {
            if (it.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && it.success) {
                showCongratsPopup()
            } else {
                toast(it.message)
            }
        }
    }


    private fun initComponents() {
        passwordRecoveryVM.token =sharedPref.getString(AppConstants.USER_TOKEN)
            passwordRecoveryVM._errorMessage.observe(viewLifecycleOwner) {
                if (it != "") toast(it)
            }
        viewBinding.btnResetPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            showCongratsPopup()
        }

        viewBinding.header.tvTitle.text = getString(R.string.password_recovery)
        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
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

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

    private fun showCongratsPopup() = try {
        val dialog = Dialog(requireActivity(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false)
        val dialogView = layoutInflater.inflate(R.layout.dialog_congrats, null)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show()

        Handler().postDelayed(Runnable {
            dialog.dismiss()
            findNavController().popBackStack(R.id.forgotPasswordFragment, true)
        }, 1500)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}