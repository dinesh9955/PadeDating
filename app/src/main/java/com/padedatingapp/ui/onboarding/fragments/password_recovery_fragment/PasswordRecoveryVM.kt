package com.padedatingapp.ui.onboarding.fragments.password_recovery_fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ForgotPasswordRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern

class PasswordRecoveryVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val forgotPasswordRepo: ForgotPasswordRepo
) : ViewModel() {
    var resetPasswordResponse = SingleLiveEvent<Resource<Result<UserModel>>>()
    var token = ""
    var password = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var confirmPassword = MutableLiveData<String>("")
    var _errorMessage = MutableLiveData<String>("")
    var specailCharPatten: Pattern = Pattern.compile("[#?!@$%^&*-]", Pattern.CASE_INSENSITIVE)
    var UpperCasePatten: Pattern = Pattern.compile("[A-Z]")
    var lowerCasePatten: Pattern = Pattern.compile("[a-z]")
    var digitCasePatten: Pattern = Pattern.compile("[0-9]")
    var specialChar: Pattern = Pattern.compile("[@\$%&#]")

    fun validate() {
        when {
            password.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_password)
            }
            password.value.toString().trim().length <= 7 -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_length_should_be_greater_than_7_characters)
            }
            !UpperCasePatten.matcher(password.value.toString()).find() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_must_have_atleast_one_uppercase_character)
            }

            !specailCharPatten.matcher(password.value.toString()).find() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_should_have_special_character)
            }
            !digitCasePatten.matcher(password.value.toString()).find() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_must_have_atleast_one_digit_character)
            }
            confirmPassword.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_confirm_password)
            }
            password.value.toString() != confirmPassword.value.toString() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_and_confirm_password_doesnot_match)
            }
            else -> callResetPasswordApi()
        }
    }
    private fun callResetPasswordApi() {
        val jsonObj = JsonObject()
        jsonObj.addProperty("password",password.value.toString())
       // jsonObj.addProperty("newpassword",password.value.toString())
       // jsonObj.addProperty("email",email.value.toString())
        Log.d(
            "REGISTER_RQST_BODY_DATA",
            "$jsonObj"
        )

        coroutinesManager.ioScope.launch {
            resetPasswordResponse.postValue(Resource.loading(null))
            resetPasswordResponse.postValue(
                forgotPasswordRepo.resetPassword("Bearer $token",
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            )
        }
    }
}