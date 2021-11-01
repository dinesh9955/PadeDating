package com.padedatingapp.ui.onboarding.fragments.create_account

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.abc
import com.padedatingapp.api.Resource
import com.padedatingapp.api.ResponseStatus
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.custom_views.CustomProgressDialog
import com.padedatingapp.databinding.FragmentCreateAccountBinding
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.UsernameResponse
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.formatDate
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.togglePasswordVisibility
import kotlinx.android.synthetic.main.fragment_new_account.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.util.*


class CreateAccountFragment : DataBindingFragment<FragmentCreateAccountBinding>() {

    companion object {
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
        var TAG = "CreateAccountFragment"
    }

    private val sharedPref by inject<SharedPref>()

    private var progressDialog: CustomProgressDialog? = null

    private val createAccountVM by inject<CreateAccountVM>()

    override fun layoutId(): Int = R.layout.fragment_create_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vm = createAccountVM
        viewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireContext())
        initComponents()



    }

    private fun initComponents() {
        createAccountVM.countryCode = ccp.selectedCountryCodeWithPlus
        createAccountVM.token = sharedPref.getString(AppConstants.USER_TOKEN, "en")
        viewBinding.tvTitle.text = getString(R.string.sign_up)


//        Log.e(NewAccountFragment.TAG, "sentSocialResponseF "+sharedPref.getString(AppConstants.SOCIAL_FN))
//        Log.e(NewAccountFragment.TAG, "sentSocialResponseL "+sharedPref.getString(AppConstants.SOCIAL_LN))

        createAccountVM.firstName.value = sharedPref.getString(AppConstants.SOCIAL_FN, "en")
        createAccountVM.lastName.value = sharedPref.getString(AppConstants.SOCIAL_LN, "en")



        getArgumentsData()
        setUpListeners()
        initObservables()
    }

    private fun initObservables() {
        createAccountVM.checkUsernameResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "checkUsernameResponse")
        }

        createAccountVM.setupProfileResponse.observe(viewLifecycleOwner) {
            getLiveData(it, "setupProfileResponse")
        }

        viewBinding.ivHideShowPass.setOnClickListener {
            togglePasswordVisibility(requireContext(), viewBinding.etPassword, viewBinding.ivHideShowPass)
        }

        viewBinding.ivHideShowConfPass.setOnClickListener {
            togglePasswordVisibility(requireContext(), viewBinding.etConfPassword, viewBinding.ivHideShowConfPass)
        }

        createAccountVM._errorMessage.observe(viewLifecycleOwner) {
            if (it != "") toast(it)
        }

        viewBinding.etUserName.addTextChangedListener(object : TextWatcher {
            var debouncePeriod: Long = 500
            val coroutineScope = lifecycle.coroutineScope
            var textChangeJob: Job? = null

            override fun afterTextChanged(it: Editable?) {

                if (it.toString().trim() != "") {
                    if (it.toString().length >= 3) {
                        viewBinding.tvIsUsernameAvailable.isVisible = false
                        textChangeJob = coroutineScope.launch {
                            delay(debouncePeriod)
                            val jsonObj = JsonObject()
                            jsonObj.addProperty("username", it.toString())
                            Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                            createAccountVM.callCheckUsernameApi(
                                    jsonObj.toString()
                                            .toRequestBody("application/json".toMediaTypeOrNull())
                            )
                        }
                    } else {
                        viewBinding.tvIsUsernameAvailable.isVisible = true
                        viewBinding.tvIsUsernameAvailable.text = getString(R.string.not_available)
                        viewBinding.tvIsUsernameAvailable.setTextColor(
                                ContextCompat.getColor(
                                        requireContext(),
                                        R.color.colorRed
                                )
                        )
                    }
                } else {
                    viewBinding.tvIsUsernameAvailable.text = getString(R.string.not_available)
                    viewBinding.tvIsUsernameAvailable.setTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorRed
                            )
                    )
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textChangeJob?.cancel()
            }

        })
    }

    private fun getLiveData(response: Resource<ResultModel<*>>?, type: String) {
        when (response?.status) {
            Resource.Status.LOADING -> {
                if (type != "checkUsernameResponse")
                    progressDialog?.show()
            }
            Resource.Status.SUCCESS -> {
                progressDialog?.dismiss()

                when (type) {
                    "setupProfileResponse" -> {
                        var data = response.data as ResultModel<UserModel>
                        onSetupProfileResponse(data)
                    }

                    "checkUsernameResponse" -> {
                        var data = response.data as ResultModel<UsernameResponse>
                        onCheckUsernameResponse(data)
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
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                sharedPref.setString(AppConstants.USER_TOKEN, it.data?.accessToken!!)
                findNavController().navigate(CreateAccountFragmentDirections.actionToUploadPhoto())
            } else {
                toast(data.message)
            }
        }


    }

    private fun onCheckUsernameResponse(data: ResultModel<UsernameResponse>) {
        data?.let {
            if (data.statusCode == ResponseStatus.STATUS_CODE_SUCCESS && data.success) {
                if (it.data!!.isAvailable) {
                    viewBinding.tvIsUsernameAvailable.isVisible = true
                    createAccountVM.isUsernameAvailable.set(true)
                    viewBinding.tvIsUsernameAvailable.text = getString(R.string._available)
                    viewBinding.tvIsUsernameAvailable.setTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.quantum_googgreen
                            )
                    )

                } else {
                    viewBinding.tvIsUsernameAvailable.isVisible = true
                    createAccountVM.isUsernameAvailable.set(false)
                    viewBinding.tvIsUsernameAvailable.text = getString(R.string.not_available)
                    viewBinding.tvIsUsernameAvailable.setTextColor(
                            ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorRed
                            )
                    )

                }
            }
            else {
                viewBinding.tvIsUsernameAvailable.isVisible = true
                createAccountVM.isUsernameAvailable.set(false)
                viewBinding.tvIsUsernameAvailable.text = getString(R.string.not_available)
                viewBinding.tvIsUsernameAvailable.setTextColor(
                        ContextCompat.getColor(
                                requireContext(),
                                R.color.colorRed
                        )
                )
                toast(data.message)
            }
        }
    }

    private fun getArgumentsData() {
        createAccountVM.countryCode = arguments?.getString("countryCode") ?: ""
        createAccountVM.phoneNo.value = arguments?.getString("phone", "")
        createAccountVM.email.value = arguments?.getString("email")
        viewBinding.etPhone.setText(arguments?.getString("phone", ""))
        viewBinding.etEmail.setText(arguments?.getString("email"))
        if(createAccountVM.countryCode.contains("+")){
            viewBinding.ccp.setCountryForPhoneCode(createAccountVM.countryCode.replace("+", "").toInt())
        }

        if(arguments?.getString("countryCode").toString().length == 0){
            viewBinding.ccp.setFocusableInTouchMode(true);
            viewBinding.ccp.setClickable(true);
            viewBinding.ccp.setFocusable(true);
        }else{
            viewBinding.ccp.setFocusableInTouchMode(false);
            viewBinding.ccp.setClickable(false);
            viewBinding.ccp.setFocusable(false);
        }
        
        if(arguments?.getString("phone").toString().length == 0){
            viewBinding.etPhone.setFocusableInTouchMode(true);
            viewBinding.etPhone.setClickable(true);
            viewBinding.etPhone.setFocusable(true);
        }else{
            viewBinding.etPhone.setFocusableInTouchMode(false);
            viewBinding.etPhone.setClickable(false);
            viewBinding.etPhone.setFocusable(false);
        }

        if(arguments?.getString("email").toString().length == 0){
            viewBinding.etEmail.setFocusableInTouchMode(true);
            viewBinding.etEmail.setClickable(true);
            viewBinding.etEmail.setFocusable(true);
        }else{
            viewBinding.etEmail.setFocusableInTouchMode(false);
            viewBinding.etEmail.setClickable(false);
            viewBinding.etEmail.setFocusable(false);
        }



    }

    private fun setUpListeners() {
        viewBinding.ccp.setOnCountryChangeListener {
            createAccountVM.countryCode = ccp.selectedCountryCodeWithPlus
        }
        /*  viewBinding.btnCreateAccount.setOnClickListener {
              findNavController().navigate(CreateAccountFragmentDirections.actionToUploadPhoto())
          }*/
        viewBinding.tvDateOfBorth.setOnClickListener {
            showDateRangePicker()
        }
        viewBinding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.tvGender.setOnClickListener {
            requireActivity().hideKeyboard()
            showDropDownDialog()
        }
        viewBinding.tvCountry.setOnClickListener {
            requireActivity().hideKeyboard()
            showAddressOverlay()
        }
        viewBinding.tvLocation.setOnClickListener {
            requireActivity().hideKeyboard()
            showAddressOverlay()
        }
        viewBinding.rlTop.setOnClickListener {
            requireActivity().hideKeyboard()
        }

        viewBinding.cbTandC.setOnCheckedChangeListener { buttonView, isChecked ->
            createAccountVM.termsConditionCheck.set(
                    isChecked
            )
        }
    }

    private fun showAddressOverlay() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = object : Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
        ) {}
            .setHint("Address")
            .build(requireContext())
        startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE)
    }

    private fun showDropDownDialog() {
        val list = resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) { _, which ->
                viewBinding.tvGender.text = list[which]
            }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                SELECT_ADDRESS_REQUEST_CODE -> {
                    try {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        try {
                            val addresses: List<Address>
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            addresses = geocoder.getFromLocation(
                                    place.latLng?.latitude!!,
                                    place.latLng?.longitude!!,
                                    1
                            )
                            // val address: String = addresses[0].getAddressLine(0)
                            createAccountVM.latitude.value = addresses[0].latitude
                            createAccountVM.longitude.value = addresses[0].longitude
                            if (addresses[0].locality != null) {
                                createAccountVM.city.value = addresses[0].locality ?: ""
                            } else {
                                createAccountVM.city.value = ""
                            }
                            createAccountVM.state.value = addresses[0].adminArea ?: ""
                            createAccountVM.country.value = addresses[0].countryName ?: ""
                            var str = addresses[0].locality + ", " + addresses[0].countryName
                            createAccountVM.cityAndCountry.value = str
                            viewBinding.tvLocation.text = addresses[0].getAddressLine(0)
                            viewBinding.tvCountry.text = createAccountVM.cityAndCountry.value
                            requireActivity().hideKeyboard()
                        } catch (e: Exception) {
                            Log.e("CreateAccountFragment", "GeoCoder Exception: $e")
                        }
                    } catch (e: Exception) {
                    }
                }
            }
    }

    private fun showDateRangePicker() {
        val now = Calendar.getInstance()
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
        val calConstraint = CalendarConstraints.Builder().setEnd(now.timeInMillis)
            .setValidator(DateValidatorPointBackward.now()).build()
        builder.setCalendarConstraints(calConstraint)
        builder.setSelection(now.timeInMillis)
        val picker = builder.build()
        picker.show(childFragmentManager, picker.toString())
        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {

            val now2 = Calendar.getInstance()

            now2.set(formatDate(it).toString().split("/")[2].toInt(), formatDate(it).toString().split("/")[1].toInt(), formatDate(it).toString().split("/")[0].toInt())

            val max: Long =
                (now.getTimeInMillis() - now2.getTimeInMillis()) / (365 * 24 * 60 * 60 * 1000L)

            if(max >= 18){
                Log.e(abc.TAG, "days11 " + max)
                viewBinding.tvDateOfBorth.text = formatDate(it)
                createAccountVM.dob.value = viewBinding.tvDateOfBorth.text.toString()
            }else{
                Log.e(abc.TAG, "days22 " + max)
                createAccountVM._errorMessage.value = createAccountVM.resourceProvider.getString(R.string.please_enter_dob_18)
                viewBinding.tvDateOfBorth.text = ""
                createAccountVM.dob.value = ""
            }

          //  Log.e(TAG , "tvDateOfBorthCCC "+formatDate(it).toString().split("/")[2])



          //  createAccountVM.dobLast.value = viewBinding.tvDateOfBorth.text.toString().split("/")[2]

            Log.e(TAG , "tvDateOfBorth "+viewBinding.tvDateOfBorth.text)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }
}