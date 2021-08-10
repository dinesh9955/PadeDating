package com.padedatingapp.ui.onboarding.fragments.about_me

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentAboutMeSignUpBinding
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.ui.main.HomeActivity
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_about_me_sign_up.*
import kotlinx.android.synthetic.main.header_layout.view.*
import org.koin.android.ext.android.inject


class AboutMeSignUpFragment : DataBindingFragment<FragmentAboutMeSignUpBinding>() {

    companion object{
        var TAG = "AboutMeSignUpFragment"
    }

    private val sharedPref by inject<SharedPref>()

    private var progressDialog: CustomProgressDialog? = null

    private val aboutMeVM by inject<AboutMeVM>()
    var title = ""
    override fun layoutId(): Int = R.layout.fragment_about_me_sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        viewBinding.vm = aboutMeVM
        viewBinding.lifecycleOwner = this
        initComponents()

    }

    private fun initComponents() {
        aboutMeVM.token = sharedPref.getString(AppConstants.USER_TOKEN)

        title = arguments?.getString("title", "") ?: ""
        viewBinding.header.tvTitle.text = title
        initObservables()
        setupListeners()
        setUserData()
    }


    private fun setUserData() {
        try {
//            editProfileVM.list.clear()
            val userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )

            val gson = Gson()
            val json = gson.toJson(userObject)
            Log.e(TAG, "userObject "+json)

            if(userObject != null){

                if(!userObject.description.equals("")){
                    aboutMeVM.etAboutMe.value = userObject.description
                }

                if(!userObject.ethnicity.equals("")){
                    Log.e(TAG, "userObject.educationLevel "+userObject.ethnicity)
                    aboutMeVM.originEthnicity.value = userObject.ethnicity.substring(0, 1).toUpperCase() + userObject.ethnicity.substring(1).toLowerCase()
                }
                if(!userObject.educationLevel.equals("")){
                    aboutMeVM.educationLevel.value = userObject.educationLevel.substring(0, 1).toUpperCase() + userObject.educationLevel.substring(1).toLowerCase()
                }
                if(!userObject.work.equals("")){
                    aboutMeVM.work.value = userObject.work.substring(0, 1).toUpperCase() + userObject.work.substring(1).toLowerCase()
                }
                if(!userObject.datingPreference.equals("")){
                    aboutMeVM.datingPreference.value = userObject.datingPreference.substring(0, 1).toUpperCase() + userObject.datingPreference.substring(1).toLowerCase()
                }
                if(!userObject.religiousBelief.equals("")){
                    aboutMeVM.religiousBeliefs.value = userObject.religiousBelief.substring(0, 1).toUpperCase() + userObject.religiousBelief.substring(1).toLowerCase()
                }

//            if(!userObject.religiousBelief.equals("")){
//                aboutMeVM.religiousBeliefs.value = userObject.religiousBelief.substring(0, 1).toUpperCase() + userObject.religiousBelief.substring(1).toLowerCase()
//            }

                if(!userObject.height.equals("")){
                    aboutMeVM.feet.value = userObject.height.split(" ")[0].toUpperCase()
                    aboutMeVM.inches.value = userObject.height.split(" ")[0].toUpperCase()
                }


                aboutMeVM.doYouSmoke.value = userObject.doyousmoke.toUpperCase()
                if(userObject.doyousmoke.equals("YES" , true)){
                    viewBinding.rbSmokeYes.isChecked = true
                    viewBinding.rbSmokeNo.isChecked = false
                    viewBinding.rbSmokeSomeTimes.isChecked = false
                }else if(userObject.doyousmoke.equals("NO" , true)){
                    viewBinding.rbSmokeYes.isChecked = false
                    viewBinding.rbSmokeNo.isChecked = true
                    viewBinding.rbSmokeSomeTimes.isChecked = false
                }else if(userObject.doyousmoke.equals("SOMETIMES" , true)){
                    viewBinding.rbSmokeYes.isChecked = false
                    viewBinding.rbSmokeNo.isChecked = false
                    viewBinding.rbSmokeSomeTimes.isChecked = true
                }


                aboutMeVM.doYouDrink.value = userObject.doyoudrink.toUpperCase()
                if(userObject.doyoudrink.equals("YES" , true)){
                    viewBinding.rbDrinkYes.isChecked = true
                    viewBinding.rbDrinkNo.isChecked = false
                    viewBinding.rbDrinkSomeTimes.isChecked = false
                }else if(userObject.doyoudrink.equals("NO" , true)){
                    viewBinding.rbDrinkYes.isChecked = false
                    viewBinding.rbDrinkNo.isChecked = true
                    viewBinding.rbDrinkSomeTimes.isChecked = false
                }else if(userObject.doyoudrink.equals("SOMETIMES" , true)){
                    viewBinding.rbDrinkYes.isChecked = false
                    viewBinding.rbDrinkNo.isChecked = false
                    viewBinding.rbDrinkSomeTimes.isChecked = true
                }




                aboutMeVM.interesedIn.value = userObject.interestedIn
                if(userObject.interestedIn.equals("male" , true)){
                    viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
                    viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white)
                    viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white)
                }else if(userObject.interestedIn.equals("female" , true)){
                    viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white)
                    viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
                    viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white)
                }else if(userObject.interestedIn.equals("both" , true)){
                    viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white)
                    viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white)
                    viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
                }
            }





            viewBinding.rbSmokeYes.setOnClickListener(View.OnClickListener {
                viewBinding.rbSmokeYes.isChecked = true
                viewBinding.rbSmokeNo.isChecked = false
                viewBinding.rbSmokeSomeTimes.isChecked = false
                aboutMeVM.doYouSmoke.value = "YES"
            })

            viewBinding.rbSmokeNo.setOnClickListener(View.OnClickListener {
                viewBinding.rbSmokeYes.isChecked = false
                viewBinding.rbSmokeNo.isChecked = true
                viewBinding.rbSmokeSomeTimes.isChecked = false
                aboutMeVM.doYouSmoke.value = "NO"
            })

            viewBinding.rbSmokeSomeTimes.setOnClickListener(View.OnClickListener {
                viewBinding.rbSmokeYes.isChecked = false
                viewBinding.rbSmokeNo.isChecked = false
                viewBinding.rbSmokeSomeTimes.isChecked = true
                aboutMeVM.doYouSmoke.value = "SOMETIMES"
            })





            viewBinding.rbDrinkYes.setOnClickListener(View.OnClickListener {
                viewBinding.rbDrinkYes.isChecked = true
                viewBinding.rbDrinkNo.isChecked = false
                viewBinding.rbDrinkSomeTimes.isChecked = false
                aboutMeVM.doYouDrink.value = "YES"
            })

            viewBinding.rbDrinkNo.setOnClickListener(View.OnClickListener {
                viewBinding.rbDrinkYes.isChecked = false
                viewBinding.rbDrinkNo.isChecked = true
                viewBinding.rbDrinkSomeTimes.isChecked = false
                aboutMeVM.doYouDrink.value = "NO"
            })

            viewBinding.rbDrinkSomeTimes.setOnClickListener(View.OnClickListener {
                viewBinding.rbDrinkYes.isChecked = false
                viewBinding.rbDrinkNo.isChecked = false
                viewBinding.rbDrinkSomeTimes.isChecked = true
                aboutMeVM.doYouDrink.value = "SOMETIMES"
            })








//            aboutMeVM.female.value = activity.resources.getDrawable(R.drawable.ic_gender_female)
////            bg_round_edge_white_purple_border
////            bg_round_edge_white


//            Glide.with(requireContext()).load(userObject.image)
//                .placeholder(R.drawable.user_place_holder).into(viewBinding.ivPorfilePic)
//
//            viewBinding.tvName.text = userObject.firstName + " " + userObject.lastName
//            viewBinding.etFirstName.setText(userObject.firstName)
//            viewBinding.etLastName.setText(userObject.lastName)
//            viewBinding.etPhone.setText(userObject.phoneNo)
//            viewBinding.etEmail.setText(userObject.email)
//            editProfileVM.gender.value = userObject.gender.substring(0, 1).toUpperCase() + userObject.gender.substring(1).toLowerCase()
//            viewBinding.tvAddress.setText(userObject.address)
//
//            editProfileVM.firstName.value = userObject.firstName
//            editProfileVM.lastName.value = userObject.lastName
//            editProfileVM.address.value = userObject.address
//            editProfileVM.profilePicUrl = userObject.image
//            editProfileVM.city.value = userObject.city
//            editProfileVM.state.value = userObject.state
//            editProfileVM.country.value = userObject.country
//            editProfileVM.latitude.value = userObject.latitude.toDouble()
//            editProfileVM.longitude.value = userObject.longitude.toDouble()
//            viewBinding.ccp.setCountryForPhoneCode(userObject.countryCode.replace("+", "").toInt())
//            for (doc in userObject.docImage) {
//                editProfileVM.list.add(ImageModel(doc.source, doc.type, doc.thumb))
//            }
//            if (editProfileVM.list.size < 10) {
//                editProfileVM.list.add(ImageModel("add", "image", ""))
//            }
//            setAdapter()

        } catch (e: JsonParseException) {

        }
    }




    private fun initObservables() {

        aboutMeVM.errorMessage.observe(viewLifecycleOwner) {
            if (it != "") {
                toast(it)
            }
        }

        aboutMeVM.optionChoosen.observe(viewLifecycleOwner) {
            showDropDownDialog(it)
        }

        aboutMeVM.loginResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "setupProfileResponse")
        }

        Log.e(TAG , "onViewCreated11")
    }



    private fun getLiveData(response: Resource<ResultModel<UserModel>>?, type: String) {

        Log.e(TAG , "onViewCreated12")

        when (response?.status) {
            Resource.Status.LOADING -> {
                progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "setupProfileResponse" -> {
                        val data = response.data as ResultModel<UserModel>
                        onSetupProfileResponse(data)
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

    private fun onSetupProfileResponse(data: ResultModel<UserModel>) {

        Log.e(TAG, "onSetupProfileResponse "+data)

        val gson = Gson()
        val json = gson.toJson(data)
        Log.e(TAG, "userObjectAA "+json)


        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {

               // Log.e(TAG, "accessToken "+it.data?.accessToken!!)

                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)

                sharedPref.setString(AppConstants.USER_ID, it.data._id)
                sharedPref.setString(AppConstants.USER_OBJECT, Gson().toJson(it.data))
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            } else {
                toast(data.message)
            }
        }
    }

    private fun showDropDownDialog(type: String?) {
        var list: Array<String>
        type?.let {
            when (it) {
                "ethnicity" -> {
                    list = resources.getStringArray(R.array.ethnicity_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.origin_or_ethnicity))
                        .setItems(list) { _, which ->
                            aboutMeVM.originEthnicity.value = list[which]
                        }.show()
                }
                "work" -> {
                    list = resources.getStringArray(R.array.work_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.work))
                        .setItems(list) { _, which ->
                            aboutMeVM.work.value = list[which]

                        }.show()
                }
                "dating_pref" -> {
                    list = resources.getStringArray(R.array.dating_pref_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.dating_pref))
                        .setItems(list) { _, which ->
                            aboutMeVM.datingPreference.value = list[which]

                        }.show()
                }
                "religious_beliefs" -> {
                    list = resources.getStringArray(R.array.religious_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.religious_beliefs))
                        .setItems(list) { _, which ->
                            aboutMeVM.religiousBeliefs.value = list[which]

                        }.show()
                }
                "childern" -> {
                    list = resources.getStringArray(R.array.childern_array)
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.childern))
                            .setItems(list) { _, which ->
                                aboutMeVM.childern.value = list[which]

                            }.show()
                }
                "select_feet" -> {
                    list = resources.getStringArray(R.array.feet_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.feet))
                        .setItems(list) { _, which ->
                            aboutMeVM.feet.value = list[which]
                        }.show()

                }
                "select_inches" -> {
                    list = resources.getStringArray(R.array.inches_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.inch))
                        .setItems(list) { _, which ->
                            aboutMeVM.inches.value = list[which]
                        }.show()
                }
                "education" -> {
                    list = resources.getStringArray(R.array.education_array)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.education_lavel))
                        .setItems(list) { _, which ->
                            aboutMeVM.educationLevel.value = list[which]
                        }.show()
                }
                else -> {

                }

            }
        }
    }

    private fun setupListeners() {

        viewBinding.header.ivBack.setOnClickListener {
            requireActivity().hideKeyboard()
            findNavController().popBackStack()
        }
        viewBinding.btnSubmit.setOnClickListener {
//            if (title == "Edit Info") {
//                findNavController().popBackStack()
//            } else {
//                /* startActivity(Intent(requireContext(), HomeActivity::class.java))
//                 requireActivity().finish()*/
                aboutMeVM.onClick("submit")
//            }
        }

        viewBinding.ivMale.setOnClickListener {
            viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
            viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white)
            viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white)
            aboutMeVM.interesedIn.value = "Male"
        }

        viewBinding.ivFemale.setOnClickListener {
            viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
            viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white)
            viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white)
            aboutMeVM.interesedIn.value = "Female"
        }

        viewBinding.ivBothGender.setOnClickListener {
            viewBinding.ivBothGender.setBackgroundResource(R.drawable.bg_round_edge_white_purple_border)
            viewBinding.ivFemale.setBackgroundResource(R.drawable.bg_round_edge_white)
            viewBinding.ivMale.setBackgroundResource(R.drawable.bg_round_edge_white)
            aboutMeVM.interesedIn.value = "Both"
        }

        viewBinding.rgDrink.setOnCheckedChangeListener { p0, p1 ->
            if (rbDrinkNo.isChecked) aboutMeVM.doYouDrink.value = "No"
            else if (rbDrinkYes.isChecked) aboutMeVM.doYouDrink.value = "Yes"
            else {
                aboutMeVM.doYouDrink.value = "Sometimes"
            }
        }

        viewBinding.rgSmoke.setOnCheckedChangeListener { p0, p1 ->
            if (rbDrinkNo.isChecked) aboutMeVM.doYouSmoke.value = "No"
            else if (rbDrinkYes.isChecked) aboutMeVM.doYouSmoke.value = "Yes"
            else {
                aboutMeVM.doYouSmoke.value = "Sometimes"
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}