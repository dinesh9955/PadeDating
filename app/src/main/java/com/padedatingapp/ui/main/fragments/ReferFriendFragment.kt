package com.padedatingapp.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentReferFriendBinding
import com.padedatingapp.utils.hideKeyboard

class ReferFriendFragment : DataBindingFragment<FragmentReferFriendBinding>() {
    override fun layoutId(): Int = R.layout.fragment_refer_friend

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
       viewBinding.header.tvTitle.text = getString(R.string.refer_a_friend)
        viewBinding.btnReferNow.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}