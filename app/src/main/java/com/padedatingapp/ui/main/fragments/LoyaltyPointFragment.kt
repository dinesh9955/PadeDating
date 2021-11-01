package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentLoginBinding
import com.padedatingapp.databinding.FragmentLoyaltyPointBinding
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import org.koin.android.ext.android.inject


class LoyaltyPointFragment : DataBindingFragment<FragmentLoyaltyPointBinding>() {

    private val sharedPref by inject<SharedPref>()


    override fun layoutId(): Int = R.layout.fragment_loyalty_point

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.btnRedeemNow.setOnClickListener {
            //findNavController().popBackStack()
            findNavController().navigate(LoyaltyPointFragmentDirections.actionToBuyPremium("loyalty_points"))
        }

        setUserData()

    }


    private fun setUserData() {
        try {
            var userObject =
                    Gson().fromJson(
                            sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                            UserModel::class.java
                    )

            viewBinding.tvLoyaltyPoints.text = ""+userObject.totalPoints

        } catch (e: JsonParseException) {

        }
    }



    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}