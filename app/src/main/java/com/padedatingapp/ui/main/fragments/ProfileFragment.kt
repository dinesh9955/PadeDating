package com.padedatingapp.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.compose.navArgument
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.padedatingapp.PadeDatingApp
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentProfileBinding
import com.padedatingapp.model.UserModel
import com.padedatingapp.sockets.AppSocketListener
import com.padedatingapp.sockets.SocketUrls
import com.padedatingapp.ui.MainActivity
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.ui.onboarding.fragments.WelcomeFragment.Companion.isLogout
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.LocaleHelper
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.*

class ProfileFragment : DataBindingFragment<FragmentProfileBinding>() {
    private val sharedPref by inject<SharedPref>()

    companion object{
        var TAG = "ProfileFragment"
    }

    private lateinit var userObject : UserModel

    override fun layoutId(): Int = R.layout.fragment_profile
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userObject =
            Gson().fromJson(
                sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                UserModel::class.java
            )
        if (userObject.isSubscribed==true){
            tvBecomePremium.text="My Premium"
        }
        else{
            tvBecomePremium.text="Become Premium Member"

        }
        initComponents()
    }

    private fun initComponents() {


        var type=arguments?.getString("typeOtp")
        var from=arguments?.getString("type")?:""

//        val text = if (from=="from_gift") "My Premium"
//        else  "Become Premium Member"
//        tvBecomePremium.text = text
//
//        if (type=="otpProfile"){
//            tvBecomePremium.text="My Premium"
//        }
//        else
//        {
//            tvBecomePremium.text="Become Premium Member"
//        }

//        userObject = Gson().fromJson(
//            sharedPref.getString(AppConstants.USER_OBJECT, "en"),
//            UserModel::class.java
//        )

        tvLikesCount.text = ""+userObject.totalLikes
        tvFollowerCount.text = ""+userObject.totalMatched


        if(userObject.isSubscribed == true){
            tvPremium.visibility = View.VISIBLE
        }else{
            tvPremium.visibility = View.INVISIBLE
        }




          viewBinding.tvBecomePremium.setOnClickListener {
          ///  findNavController().navigate(R.id.action_to_buy_premium)
              findNavController().navigate(ProfileFragmentDirections.actionToBuyPremium(titleProfile = "fromProfile"))

              // findNavController().navigate(ProfileFragmentDirections.actionToSignUpAboutFragment(requireActivity().getString(R.string.Edit_Info)))


        }
        viewBinding.tvPref.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToSignUpAboutFragment(requireActivity().getString(R.string.Edit_Info)))
        }

        viewBinding.tvSetting.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToChangePasswordFragment())
        }

        viewBinding.tvLogout.setOnClickListener {

            Log.e(TAG, "tvLogout")
            val json = JSONObject()
            json.put("partner", userObject._id)
            AppSocketListener.getInstance().emit(SocketUrls.OFFLINE, json)

            var email = ""
            var phone = ""
            var pass = ""
            var countryCode = 91
            var isRemember = false

            if (sharedPref.getBoolean(AppConstants.REMEMBER_ME)) {
                isRemember = true
                email = sharedPref.getString(AppConstants.USER_EMAIL, "en")
                phone = sharedPref.getString(AppConstants.USER_PHONE, "en")
                countryCode = sharedPref.getInt(AppConstants.USER_COUNTRY_CODE)
                pass = sharedPref.getString(AppConstants.USER_PASSWORD, "en")
            }

            sharedPref.clear()
            sharedPref.setBoolean(AppConstants.REMEMBER_ME,isRemember)
            sharedPref.setString(AppConstants.USER_EMAIL, email)
            sharedPref.setString(AppConstants.USER_PHONE, phone)
            sharedPref.setInt(
                AppConstants.USER_COUNTRY_CODE,
                countryCode
            )
            sharedPref.setString(
                AppConstants.USER_PASSWORD,
                pass
            )

            sharedPref.setString("mylang", "en")
            setLocale("en")
            sharedPref.setString(AppConstants.USER_OBJECT , "")

            val mApplication: PadeDatingApp = requireActivity().applicationContext as PadeDatingApp
            mApplication.destroySocketListener()

            isLogout = true
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        viewBinding.tvOrigin.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToSignUpAboutFragment(requireActivity().getString(R.string.Edit_Info)))
        }

        viewBinding.ivEditProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToEditProfileFragment())
        }

        viewBinding.ivPorfilePic.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToEditProfileFragment())
        }

        viewBinding.tvLoyaltyPoints.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToLoyaltyFragment())
        }

        viewBinding.tvLanguage.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToChangeLanguageFragment())
        }

        viewBinding.tvBuyGift.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToGiftCardListFragment())
        }

        viewBinding.tvReferAfriend.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToReferFriendFragment())
        }

        viewBinding.tvRechargeCoins.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionToBuyCreditFragment())
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
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
        setUserData()
    }

    private fun setUserData() {
        try {
            var userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT, "en"),
                    UserModel::class.java
                )

            viewBinding.tvName.text = userObject.firstName + " " + userObject.lastName
            Glide.with(requireContext()).load(userObject.image)
                .placeholder(R.drawable.user_place_holder).into(viewBinding.ivPorfilePic)

            viewBinding.tvCredits.text = ""+userObject.totalPoints

        } catch (e: JsonParseException) {

        }
    }
}