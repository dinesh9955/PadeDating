package com.padedatingapp.ui.onboarding.fragments.about_me

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.AboutMeRepo
import com.padedatingapp.api.repository.LoginRepo
import com.padedatingapp.api.repository.UploadPhotoRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.ImageUploadResponse
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AboutMeVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: AboutMeRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<Result<UserModel>>>()
    var file: File? = null
    var token = ""
    var religiousBeliefs = MutableLiveData("")
    var work = MutableLiveData("")
    var educationLevel = MutableLiveData("")
    var originEthnicity = MutableLiveData("")
    var datingPreference = MutableLiveData("")
    var interesedIn = MutableLiveData("Male")
    var doYouDrink =  MutableLiveData("")
    var doYouSmoke =  MutableLiveData("")
    var errorMessage =  MutableLiveData("")
    var optionChoosen =  SingleLiveEvent<String>()
    var feet =  MutableLiveData("2")
    var inches =  MutableLiveData("0")

//    var male = MutableLiveData("")
//    var female = MutableLiveData("")
//    var both = MutableLiveData("")


    fun callUpdateProfileApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.setUpProfile("Bearer $token", body))
        }
    }

    fun onClick(type: String) {
       when(type){

           "ethnicity"->{optionChoosen.value = "ethnicity"}
           "work"->{optionChoosen.value = "work"}
           "education"->{optionChoosen.value = "education"}
           "dating_pref"->{optionChoosen.value = "dating_pref"}
           "religious_beliefs"->{optionChoosen.value = "religious_beliefs"}
           "select_feet"->{optionChoosen.value = "select_feet"}
           "select_inches"->{optionChoosen.value = "select_inches"}
           "submit"->{
               when{
                   originEthnicity.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_origin_ethnicity)
                   }
                   educationLevel.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_enter_education_level)
                   }

                   work.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_work)
                   }

                   datingPreference.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_dating_pref)
                   }

                   religiousBeliefs.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_religous_beliefs)
                   }

                   else ->{
                       val jsonObj = JsonObject()
                       jsonObj.addProperty("height", "${feet.value} ${inches.value}")
                       jsonObj.addProperty("ethnicity", originEthnicity.value)
                       jsonObj.addProperty("educationLevel", educationLevel.value)
                       jsonObj.addProperty("work", work.value)
                       jsonObj.addProperty("datingPreference", datingPreference.value)
                       jsonObj.addProperty("religiousBelief", religiousBeliefs.value)
                       jsonObj.addProperty("doyoudrink", doYouDrink.value)
                       jsonObj.addProperty("doyousmoke", doYouSmoke.value)
                       jsonObj.addProperty("interestedIn", interesedIn.value)
                       jsonObj.addProperty("profileStatus", 3)

                       Log.d("ABOUT_ME_RQST_BODY_DATA", "validateInputs: $jsonObj")
                       callUpdateProfileApi(
                           jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                       )
                   }
               }

           }


       }
    }
}