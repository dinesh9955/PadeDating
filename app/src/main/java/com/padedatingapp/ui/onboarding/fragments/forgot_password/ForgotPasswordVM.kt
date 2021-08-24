package com.padedatingapp.ui.onboarding.fragments.forgot_password

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ForgotPasswordRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.otp.OtpForgotMain
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ForgotPasswordVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val forgotPasswordRepo: ForgotPasswordRepo
) : ViewModel() {
    var verifyOtpResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var forgotPasswordResponse = SingleLiveEvent<Resource<OtpForgotMain>>()
    var sendOtpResponse = SingleLiveEvent<Resource<ResultModel<OtpData>>>()
    var countryCode = ""
    var verificationType = "phone"
    var isEmail = false
    var otp = MutableLiveData<String>()
    var _errorMessage = MutableLiveData<String>("")
    var email = MutableLiveData<String>()
    var phoneNo = MutableLiveData<String>()
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    fun validate() {
        when {

            isEmail -> {
                when {
                    email.value.toString().contains(" ") -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.email_should_not_contain_white_spaces)
                    }
                    email.value.toString().trim().isEmpty() || !email.value.toString()
                        .matches(Regex(emailPattern)) -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.please_enter_valid_email)
                    }
                    otp.value.toString().length < 4 -> {
                        _errorMessage.postValue(resourceProvider.getString(R.string.please_enter_valid_otp))
                    }
                    else -> {
                       callForgotPasswordApi()
                    }
                }
            }

            else -> {
                when {
                    phoneNo.value.toString().trim()
                        .isEmpty() -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.please_enter_valid_phone_number)

                    }
                    phoneNo.value.toString().length < 7 -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.phone_length_should_be_greater_than_7_characters)

                    }
                    otp.value.toString().length < 4 -> {
                        _errorMessage.postValue(resourceProvider.getString(R.string.please_enter_valid_otp))
                    }
                    else -> callForgotPasswordApi()
                }
            }
        }
    }


    fun validateOtp(){
        when{
            otp.value.toString().length < 4 -> {
                _errorMessage.postValue(resourceProvider.getString(R.string.please_enter_valid_otp))
            }
            else -> callVerifyOtpApi()
        }
    }

    fun callForgotPasswordApi() {
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
            forgotPasswordResponse.postValue(Resource.loading(null))
            forgotPasswordResponse.postValue(
                forgotPasswordRepo.forgotPassword(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
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
                forgotPasswordRepo.sendOtp(
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
        Log.d(
            "REGISTER_RQST_BODY_DATA",
            "Verification Type:  $verificationType  verifyOtp: $jsonObj"
        )

        coroutinesManager.ioScope.launch {
            verifyOtpResponse.postValue(Resource.loading(null))
            verifyOtpResponse.postValue(
                forgotPasswordRepo.verifyOtp(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }
}