package com.padedatingapp.ui.onboarding.fragments.newaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.SignUpRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SignUpVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val signUpRepo: SignUpRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var sendOtpResponse = SingleLiveEvent<Resource<ResultModel<OtpData>>>()

    var countryCode = ""
    var verificationType = "phone"
    var _errorMessage = SingleLiveEvent<String>()
    var email = SingleLiveEvent<String>()
    var phoneNo = SingleLiveEvent<String>()
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    fun validate() {
        when {
            email.value.toString().contains(" ") -> {
                _errorMessage.value =
                    resourceProvider.getString(R.string.email_should_not_contain_white_spaces)
            }
            email.value.toString().trim().isEmpty() || !email.value.toString()
                .matches(Regex(emailPattern)) -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_email)
            }

            phoneNo.value.toString().trim()
                .isEmpty() -> {
                _errorMessage.value =
                    resourceProvider.getString(R.string.please_enter_valid_phone_number)

            }
            phoneNo.value.toString().length < 7 -> {
                _errorMessage.value =
                    resourceProvider.getString(R.string.phone_length_should_be_greater_than_7_characters)

            }
            else -> {
                val jsonObj = JsonObject()
                jsonObj.addProperty("email", email.value)
                jsonObj.addProperty("countryCode", countryCode)
                jsonObj.addProperty("phoneNo", phoneNo.value)

                Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                callRegisterUserApi(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            }
        }
    }

    private fun callRegisterUserApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(signUpRepo.registerUser(body))
        }
    }

    fun callSendOtpApi() {
        val jsonObj = JsonObject()
        when (verificationType) {
            "phone" -> {
                jsonObj.addProperty("phone", phoneNo.value)
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
                signUpRepo.sendOtp(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }
}