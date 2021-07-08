package com.padedatingapp.ui.onboarding.fragments.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.LoginRepo
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

class LoginVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val loginRepo: LoginRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var sendOtpResponse = SingleLiveEvent<Resource<ResultModel<OtpData>>>()

    var countryCode = ""
    var verificationType = "phone"
    var isEmailLogin = false
    var _errorMessage = MutableLiveData<String>()
    var isRememberMe = MutableLiveData<Boolean>()
    var email = MutableLiveData<String>()
    var phoneNo = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    fun validate() {
        when {

            isEmailLogin -> {
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
                    password.value.toString().trim().isEmpty() -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.please_enter_valid_password)
                    }
                    else -> {
                        val jsonObj = JsonObject()
                        jsonObj.addProperty("email", email.value)
                        jsonObj.addProperty("password", password.value)
                        Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                        callLoginUserApi(
                            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                        )
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
                    password.value.toString().trim().isEmpty() -> {
                        _errorMessage.value =
                            resourceProvider.getString(R.string.please_enter_valid_password)
                    }
                    else-> {
                        val jsonObj = JsonObject()
                        jsonObj.addProperty("phoneNo", phoneNo.value)
                        jsonObj.addProperty("countryCode", countryCode)
                        jsonObj.addProperty("password", password.value)
                        Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                        callLoginUserApi(
                            jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                        )
                    }
                }
            }
        }
    }

    private fun callLoginUserApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(loginRepo.login(body))
        }
    }
}