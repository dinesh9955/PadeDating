package com.padedatingapp.ui.onboarding.fragments.about_me

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.AboutMeRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AboutMeVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: AboutMeRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var file: File? = null
    var token = ""

    var etAboutMe = MutableLiveData("")
   // var etJobTitle = MutableLiveData("")
    var religiousBeliefs = MutableLiveData("")
    var childern = MutableLiveData("")
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

    var etCm = MutableLiveData("")

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
           "childern"->{optionChoosen.value = "childern"}
           "select_feet"->{optionChoosen.value = "select_feet"}
           "select_inches"->{optionChoosen.value = "select_inches"}
           "submit"->{
               when{

                   etAboutMe.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.about_me)
                   }

//                   originEthnicity.value.toString().trim().isEmpty() -> {
//                       errorMessage.value = resourceProvider.getString(R.string.please_select_origin_ethnicity)
//                   }

                   datingPreference.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_dating_pref)
                   }

                   educationLevel.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_enter_education_level)
                   }

//                   work.value.toString().trim().isEmpty() -> {
//                       errorMessage.value = resourceProvider.getString(R.string.please_select_work)
//                   }



                   work.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_job_title)
                   }

                   religiousBeliefs.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_religous_beliefs)
                   }

                   childern.value.toString().trim().isEmpty() -> {
                       errorMessage.value = resourceProvider.getString(R.string.please_select_childern)
                   }

                   else ->{
                       val jsonObj = JsonObject()
                       jsonObj.addProperty("description", "${etAboutMe.value}")
                       jsonObj.addProperty("height", "${feet.value}")
                       jsonObj.addProperty("heightInCms", "${inches.value}")
                     //  jsonObj.addProperty("ethnicity", originEthnicity.value)
                       jsonObj.addProperty("educationLevel", educationLevel.value)
                      // jsonObj.addProperty("work", work.value)
                       jsonObj.addProperty("work", work.value)
                       jsonObj.addProperty("datingPreference", datingPreference.value)
                       jsonObj.addProperty("religiousBelief", religiousBeliefs.value)
                       jsonObj.addProperty("childern", childern.value)
                       jsonObj.addProperty("doyoudrink", doYouDrink.value)
                       jsonObj.addProperty("doyousmoke", doYouSmoke.value)
                       jsonObj.addProperty("interestedIn", interesedIn.value)
                       jsonObj.addProperty("profileStatus", 3)
                       jsonObj.addProperty("deviceType", "ANDROID")
                       jsonObj.addProperty("deviceToken", FirebaseInstanceId.getInstance().getToken())

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