package com.padedatingapp.ui.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentWelcomeBinding
import com.padedatingapp.ui.WebActivity
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject

class WelcomeFragment : DataBindingFragment<FragmentWelcomeBinding>() {

    var TAG = "WelcomeFragment"

    companion object{
        var isLogout = false
    }

    private val sharedPref by inject<SharedPref>()

    override fun layoutId(): Int = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager()

        Log.e(TAG, "sharedPref.getString(AppConstants.USER_OBJECT) "+sharedPref.getString(AppConstants.USER_OBJECT))

        if (sharedPref.getString(AppConstants.USER_OBJECT) != "") {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        } else{

        }

        tvTandC.setOnClickListener {
            val intent = Intent(context, WebActivity::class.java)
            context?.startActivity(intent)
        }


        if(isLogout == true){
            findNavController().navigate(R.id.action_welcome_to_login)
        }




    }

    private fun initPager() {
        val adapter = IntroductionPagerAdapter(childFragmentManager)
        adapter.addFragment(IntroFragment().also {
                                                 it.arguments = Bundle().let {bd->
                                                     bd.putInt("pos",0)
                                                     bd
                                                 }
        }, "tab")
        adapter.addFragment(IntroFragment().also {
            it.arguments = Bundle().let { bd ->
                bd.putInt("pos", 1)
                bd
            }
        }  , "tab")
        adapter.addFragment(IntroFragment().also {
            it.arguments = Bundle().let { bd ->
                bd.putInt("pos", 2)
                bd
            }
        }  , "tab")

        adapter.addFragment(IntroFragment().also {
            it.arguments = Bundle().let { bd ->
                bd.putInt("pos", 3)
                bd
            }
        }  , "tab")

        viewBinding.introViewPager.adapter = adapter
        viewBinding.tabsIntro.setupWithViewPager(viewBinding.introViewPager)
        viewBinding.vOne.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.button_gradient_bg
            )
        )

        viewBinding.introViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                Log.e("WelcomeFragment", "onPageSelected: ")
                when (position) {
                    0 -> {
                        viewBinding.btnNext.text = getString(R.string.next)
                        viewBinding.vOne.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.button_gradient_bg
                            )
                        )
                        viewBinding.vTwo.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vThree.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )

                        viewBinding.vFour.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                    }
                    1 -> {
                        viewBinding.btnNext.text = getString(R.string.next)
                        viewBinding.vTwo.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.button_gradient_bg
                            )
                        )
                        viewBinding.vOne.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vThree.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vFour.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )

                    }
                    2 -> {
                        viewBinding.btnNext.text = getString(R.string.next)
                        viewBinding.vThree.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.button_gradient_bg
                            )
                        )
                        viewBinding.vTwo.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vOne.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vFour.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )

                    }
                    3 -> {
                        viewBinding.btnNext.text = getString(R.string.get_started)
                        viewBinding.vFour.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.button_gradient_bg
                            )
                        )
                        viewBinding.vTwo.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vOne.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )
                        viewBinding.vThree.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_round_edge_white
                            )
                        )

                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.e("WelcomeFragment", "onPageScrollStateChanged: ")

            }

        })

        viewBinding.btnNext.setOnClickListener {
            when (viewBinding.introViewPager.currentItem) {
                0 -> {
                    viewBinding.introViewPager.setCurrentItem(1, true)
                }
                1 -> {
                    viewBinding.introViewPager.setCurrentItem(2, true)
                }
                2 -> {
                    viewBinding.introViewPager.setCurrentItem(3, true)
                    viewBinding.btnNext.text = getString(R.string.get_started)
                }
                3->{
                    if (sharedPref.getString(AppConstants.USER_OBJECT) != "") {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    } else
                        findNavController().navigate(R.id.action_welcome_to_login)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()

    }


    override fun onStart() {
        super.onStart()
        isLogout = false
    }



}