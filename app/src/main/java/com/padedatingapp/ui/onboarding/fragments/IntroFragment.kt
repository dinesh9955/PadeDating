package com.padedatingapp.ui.onboarding.fragments

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentIntroBinding
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : DataBindingFragment<FragmentIntroBinding>() {
    override fun layoutId(): Int = R.layout.fragment_intro
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        var pos = 0
        pos = arguments?.getInt("pos", 0) ?: 0
        Glide.with(this).load(arguments?.getString("image")).into(ivImage)

//        when (pos) {
//            0 -> Glide.with(this).load(R.drawable.couple1).into(ivImage)
//            1 -> Glide.with(this).load(R.drawable.couple2).into(ivImage)
//            2 -> Glide.with(this).load(R.drawable.couple3).into(ivImage)
//            3 -> Glide.with(this).load(R.drawable.couple4).into(ivImage)
//        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}