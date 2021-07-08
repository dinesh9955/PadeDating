package com.padedatingapp.ui.onboarding.fragments.otp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.OtpRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OtpVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val otpRepo: OtpRepo
) : ViewModel() {
    var verifyOtpResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var sendOtpResponse = SingleLiveEvent<Resource<ResultModel<OtpData>>>()
    var countryCode = ""
    var verificationType = "phone"
    var otp = SingleLiveEvent<String>()
    var _errorMessage = SingleLiveEvent<String>()
    var email = SingleLiveEvent<String>()
    var phoneNo = SingleLiveEvent<String>()
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    fun validate() {
        when {
            otp.value.toString().length < 4 -> {
                _errorMessage.postValue(resourceProvider.getString(R.string.please_enter_valid_otp))
            }
            else -> callVerifyOtpApi()
        }
    }

    fun callSendOtpApi() {
        val jsonObj = JsonObject()
        when (verificationType) {
            "phone" -> {
                jsonObj.addProperty("phoneNo", phoneNo.value)
                jsonObj.addProperty("countryCode", countryCode)
            }
            "email" -> {
                jsonObj.addProperty("email", email.value)
            }
        }
        Log.d("REGISTER_RQST_BODY_DATA", "sendOtp: $jsonObj")

        coroutinesManager.ioScope.launch {
            sendOtpResponse.postValue(Resource.loading(null))
            sendOtpResponse.postValue(
                otpRepo.sendOtp(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }

    private fun callVerifyOtpApi() {
        val jsonObj = JsonObject()

        when (verificationType) {
            "phone" -> {
                jsonObj.addProperty("phoneNo", phoneNo.value)
                jsonObj.addProperty("countryCode", countryCode)
                jsonObj.addProperty("otpCode", otp.value.toString())
            }
            "email" -> {
                jsonObj.addProperty("email", email.value)
                jsonObj.addProperty("otpCode", otp.value.toString())
            }
        }
        Log.d("REGISTER_RQST_BODY_DATA", "Verification Type:  $verificationType  verifyOtp: $jsonObj")

        coroutinesManager.ioScope.launch {
            verifyOtpResponse.postValue(Resource.loading(null))
            verifyOtpResponse.postValue(
                otpRepo.verifyOtp(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }
}