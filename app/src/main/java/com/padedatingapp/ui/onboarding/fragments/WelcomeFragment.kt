package com.padedatingapp.ui.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.birimo.birimosports.utils.SharedPref
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentWelcomeBinding
import com.padedatingapp.model.slider.Data
import com.padedatingapp.model.slider.SliderModel
import com.padedatingapp.ui.WebActivity
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.vm.BlockUserVM
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject

class WelcomeFragment : DataBindingFragment<FragmentWelcomeBinding>() {

    var TAG = "WelcomeFragment"

    private val chatVM by inject<BlockUserVM>()
    private var progressDialog: CustomProgressDialog? = null

    companion object{
        var isLogout = false
    }

    private val sharedPref by inject<SharedPref>()

    override fun layoutId(): Int = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     //   initPager()

        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = chatVM
        viewBinding.lifecycleOwner = this


        Log.e(TAG, "sharedPref.getString(AppConstants.USER_OBJECT) "+sharedPref.getString(AppConstants.USER_OBJECT, "en"))

        if (sharedPref.getString(AppConstants.USER_OBJECT, "en") != "") {
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


        initObservables()


    }





    private fun initObservables() {
        chatVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }


        chatVM.sliderImageResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "sliderImageResponse")
        }



        chatVM.callsliderImageApi()



    }


    private fun getLiveData(response: Resource<SliderModel>?, type: String) {

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "sliderImageResponse" -> {
                        val data = response.data as SliderModel
                        Log.e(TAG, "dataAA " + data.toString())
                        data?.let {
                            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                                Log.e(TAG, "listAA " + data.data.size)
                                var list_data = data.data as ArrayList<Data>

                                if(list_data != null){
                                    if(list_data.size > 0){
                                        var data : Data = list_data[0]

                                        var image1 = data.image1
                                        var image2 = data.image2

                                        val adapter = IntroductionPagerAdapter(childFragmentManager)
                                        if(image1 != null){
                                            adapter.addFragment(IntroFragment().also {
                                                it.arguments = Bundle().let {bd->
                                                    bd.putInt("pos", 0)
                                                    bd.putString("image", ""+image1)
                                                    bd
                                                }
                                            }, "tab")
                                        }

                                        if(image2 != null){
                                            adapter.addFragment(IntroFragment().also {
                                                it.arguments = Bundle().let { bd ->
                                                    bd.putInt("pos", 1)
                                                    bd.putString("image", ""+image2)
                                                    bd
                                                }
                                            }  , "tab")
                                        }

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

                                                    }

                                                    1 -> {
                                                        viewBinding.btnNext.text = getString(R.string.get_started)
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
                                                1->{
                                                    if (sharedPref.getString(AppConstants.USER_OBJECT, "en") != "") {
                                                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                                                        requireActivity().finish()
                                                    } else
                                                        findNavController().navigate(R.id.action_welcome_to_login)
                                                }
                                            }
                                        }

                                    }
                                }


                            } else {
                                toast(data.message)
                            }
                        }
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
//                        viewBinding.vThree.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )
//
//                        viewBinding.vFour.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )
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
//                        viewBinding.vThree.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )
//                        viewBinding.vFour.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )

                    }
                    2 -> {
                        viewBinding.btnNext.text = getString(R.string.next)
//                        viewBinding.vThree.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.button_gradient_bg
//                            )
//                        )
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
//                        viewBinding.vFour.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )

                    }
                    3 -> {
                        viewBinding.btnNext.text = getString(R.string.get_started)
//                        viewBinding.vFour.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.button_gradient_bg
//                            )
//                        )
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
//                        viewBinding.vThree.setBackgroundDrawable(
//                            ContextCompat.getDrawable(
//                                requireContext(),
//                                R.drawable.bg_round_edge_white
//                            )
//                        )

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
                    if (sharedPref.getString(AppConstants.USER_OBJECT, "en") != "") {
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