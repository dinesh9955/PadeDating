package com.padedatingapp.ui.onboarding.fragments.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.AppConstants
import org.koin.android.ext.android.inject


class Splash : Fragment() {

    private val sharedPref by inject<SharedPref>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({

            if (sharedPref.getString(AppConstants.USER_OBJECT, "en") != "") {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else{

                findNavController().navigate(R.id.splash_to_welcome)
            }

        },1000)
    }


}