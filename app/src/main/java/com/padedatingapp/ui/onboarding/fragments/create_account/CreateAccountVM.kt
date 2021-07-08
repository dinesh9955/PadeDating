package com.padedatingapp.ui.onboarding.fragments.create_account

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.SignUpRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.UsernameResponse
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern

class CreateAccountVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val signUpRepo: SignUpRepo
) : ViewModel() {

    var setupProfileResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var checkUsernameResponse = SingleLiveEvent<Resource<ResultModel<UsernameResponse>>>()
    var countryCode = ""
    var token = ""
    var firstName = MutableLiveData<String>("")
    var lastName = MutableLiveData<String>("")
    var userName = MutableLiveData<String>("")
    var phone = MutableLiveData<String>("")
    var counrtyCode = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var confirmPassword = MutableLiveData<String>("")
    var dob = MutableLiveData<String>("")
    var gender = MutableLiveData<String>("")
    var country = MutableLiveData<String>("")
    var city = MutableLiveData<String>("")
    var address = MutableLiveData<String>("")
    var _errorMessage = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var phoneNo = MutableLiveData<String>("")
    var cityAndCountry = MutableLiveData<String>("")
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var state = MutableLiveData<String>("")
    var termsConditionCheck  = ObservableField<Boolean>(false)
    var isUsernameAvailable  = ObservableField<Boolean>(false)
    var emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    var specailCharPatten: Pattern = Pattern.compile("[#?!@$%^&*-]", Pattern.CASE_INSENSITIVE)
    var UpperCasePatten: Pattern = Pattern.compile("[A-Z]")
    var lowerCasePatten: Pattern = Pattern.compile("[a-z]")
    var digitCasePatten: Pattern = Pattern.compile("[0-9]")

    fun validate() {
        when {
            firstName.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_first_name)
            }
            lastName.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_last_name)
            }
            userName.value.toString().trim().isEmpty() || !isUsernameAvailable.get()!! -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_user_name)
            }
            email.value.toString().contains(" ") -> {
                _errorMessage.value = resourceProvider.getString(R.string.email_should_not_contain_white_spaces)
            }
            email.value.toString().trim().isEmpty() || !email.value.toString()
                .matches(Regex(emailPattern)) -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_email)
            }
            phoneNo.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_phone_number)
            }
            phoneNo.value.toString().trim().length < 7 -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_phone_number)
            }
            password.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_valid_password)
            }
            password.value.toString().trim().length <= 7 -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_length_should_be_greater_than_7_characters)
            }
            !UpperCasePatten.matcher(password.value.toString()).find() || !digitCasePatten.matcher(password.value.toString()).find()
                    || !specailCharPatten.matcher(password.value.toString()).find() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_must_have_special_one_uppercase_character)
            }

            confirmPassword.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_confirm_password)
            }
            password.value.toString() != confirmPassword.value.toString() -> {
                _errorMessage.value = resourceProvider.getString(R.string.password_and_confirm_password_doesnot_match)
            }
            dob.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_select_date_of_birth)
            }
            gender.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_select_gender)
            }
            cityAndCountry.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_select_city_or_country)
            }
            address.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_select_location)
            }
           !termsConditionCheck.get()!! -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_accept_t_and_c)
            }
            else ->{
                val jsonObj = JsonObject()
                jsonObj.addProperty("firstName", firstName.value)
                jsonObj.addProperty("lastName", lastName.value)
                jsonObj.addProperty("gender", gender.value)
                jsonObj.addProperty("username", userName.value)
                jsonObj.addProperty("email", email.value)
                jsonObj.addProperty("countryCode", countryCode)
                jsonObj.addProperty("phoneNo", phoneNo.value)
                jsonObj.addProperty("dateofbirth", dob.value)
                jsonObj.addProperty("city", city.value)
                jsonObj.addProperty("country", country.value)
                jsonObj.addProperty("state", state.value)
                jsonObj.addProperty("password", password.value)
                jsonObj.addProperty("username", userName.value)
                jsonObj.addProperty("address", address.value)
                jsonObj.addProperty("latitude", latitude.value.toString())
                jsonObj.addProperty("longitude", longitude.value.toString())
                jsonObj.addProperty("profileStatus", 1)

                Log.d("REGISTER_RQST_BODY_DATA", "validateInputs: $jsonObj")
                callSetupProfileApi(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            }
        }
    }

    fun callSetupProfileApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            setupProfileResponse.postValue(Resource.loading(null))
            setupProfileResponse.postValue(signUpRepo.setUpProfile("Bearer $token",body))
        }
    }

    fun callCheckUsernameApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            checkUsernameResponse.postValue(Resource.loading(null))
            checkUsernameResponse.postValue(signUpRepo.checkUsername("Bearer $token",body))
        }
    }

}