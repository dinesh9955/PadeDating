package com.padedatingapp.ui.main.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.birimo.birimosports.utils.SharedPref
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingFragment
import com.padedatingapp.databinding.FragmentFiltersBinding
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.AppConstants
import com.padedatingapp.utils.hideKeyboard
import com.padedatingapp.utils.setMarqueText
import kotlinx.android.synthetic.main.fragment_filters.*
import org.koin.android.ext.android.inject
import java.util.*

class FiltersFragment : DataBindingFragment<FragmentFiltersBinding>() {
    private val sharedPref by inject<SharedPref>()

    var age = 0
    var distance = 0

    var interestIn = ""

    var placeLat : Double = 0.0
    var placeLng : Double = 0.0

//    var originEthnicity = MutableLiveData("")
//
//    var optionChoosen =  SingleLiveEvent<String>()

    companion object {
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
        var TAG = "FiltersFragment"
    }


//    private val aboutMeVM by inject<FilterVM>()


    override fun layoutId(): Int = R.layout.fragment_filters


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initComponents()
//        initObservables()
        setUserData()
    }

    private fun setUserData() {
        if (sharedPref.getString("address") != "") {
            viewBinding.tvLocation.text = sharedPref.getString("address")
        } else {
            val userObject =
                Gson().fromJson(
                    sharedPref.getString(AppConstants.USER_OBJECT),
                    UserModel::class.java
                )
            viewBinding.tvLocation.text = userObject.address
        }
        setMarqueText(viewBinding.tvLocation)
    }

    @SuppressLint("ResourceAsColor")
    private fun initComponents() {
        viewBinding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.tvLocation.setOnClickListener {
            showAddressOverlay()
        }


         viewBinding.tvDatingPrefences.setOnClickListener {
             initObservables()
        }



        viewBinding.tvDone.setOnClickListener {
            sharedPref.setString("filter", "filter")
            sharedPref.setString("interest", interestIn)
            sharedPref.setString("address", tvLocation.text.toString())
            sharedPref.setString("employment", etEmployment.text.toString())
            sharedPref.setInt("age", age)
            sharedPref.setInt("distance", distance)
            sharedPref.setString("placeLat", ""+placeLat)
            sharedPref.setString("placeLng", ""+placeLng)

            sharedPref.setString("dating_prefences", ""+viewBinding.tvDatingPrefences.text)

            findNavController().popBackStack()
        }

        viewBinding.rangeSeekbarAge.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            if (minValue != null && maxValue != null) {
                age = maxValue.toInt()
                viewBinding.tvAge.text = "${minValue.toInt()} - ${maxValue.toInt()}"
                viewBinding.textminAge.text = "${minValue.toInt()}"
                viewBinding.textmaxAge.text = "${maxValue.toInt()}"
            }
        }

        viewBinding.rangeSeekbarDistance.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            if (minValue != null && maxValue != null) {
                distance = maxValue.toInt()
                viewBinding.tvDistance.text = "${minValue.toInt()}km - ${maxValue.toInt()}km"
                viewBinding.textminDistance.text = "${minValue.toInt()}"
                viewBinding.textmaxDistance.text = "${maxValue.toInt()}"
            }
        }




        tvMen.setOnClickListener(View.OnClickListener {
            interestIn = "male"
            tvMen.setBackgroundTintList(null);
            tvWomen.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            tvBoth.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        })

        tvWomen.setOnClickListener(View.OnClickListener {
            interestIn = "female"
            tvMen.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            tvWomen.setBackgroundTintList(null);
            tvBoth.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
        })

        tvBoth.setOnClickListener(View.OnClickListener {
            interestIn = "both"
            tvMen.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            tvWomen.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            tvBoth.setBackgroundTintList(null);
        })



    }




    private fun initObservables() {
        var list: Array<String>
        list = resources.getStringArray(R.array.dating_prefences_array)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.hint_DatingPrefences))
            .setItems(list) { _, which ->
                //aboutMeVM.originEthnicity.value = list[which]
                viewBinding.tvDatingPrefences.text = list[which]
            }.show()


        Log.e(TAG, "onViewCreated11")
    }


    override fun onResume() {
        super.onResume()
        requireActivity().hideKeyboard()
    }


    private fun showAddressOverlay() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        Log.e(TAG, "fieldsAA " +fields.toString())
        val intent = object : Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ) {}
            .setHint("Address")
            .build(requireContext())
        startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
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
                            viewBinding.tvLocation.setText( addresses[0].getAddressLine(0))
                            setMarqueText(viewBinding.tvLocation)
                            placeLat = place.latLng!!.latitude
                            placeLng = place.latLng!!.longitude
                            requireActivity().hideKeyboard()
                        } catch (e: Exception) {
                            Log.e("HomeFragment", "GeoCoder Exception: $e")
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }













}