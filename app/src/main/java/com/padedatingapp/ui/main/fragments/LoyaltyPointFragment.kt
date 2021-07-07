package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentLoginBinding
import com.padedatingapp.databinding.FragmentLoyaltyPointBinding
import com.padedatingapp.utils.hideKeyboard


class LoyaltyPointFragment : DataBindingFragment<FragmentLoyaltyPointBinding>() {

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


    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }

}