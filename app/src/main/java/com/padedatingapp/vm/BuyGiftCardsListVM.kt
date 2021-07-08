package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.GiftCardRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import java.io.File

class BuyGiftCardsListVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val aboutMeRepo: GiftCardRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<AllGiftCard>>>()
    var file: File? = null
    var token = ""

    var errorMessage =  MutableLiveData("")


    fun callUpdateProfileApi() {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.giftCards("Bearer $token"))
        }
    }


//
//    fun onClick(type: String) {
//        when(type){
//
//            "ethnicity"->{optionChoosen.value = "ethnicity"}
//            "work"->{optionChoosen.value = "work"}
//            "education"->{optionChoosen.value = "education"}
//            "dating_pref"->{optionChoosen.value = "dating_pref"}
//            "religious_beliefs"->{optionChoosen.value = "religious_beliefs"}
//            "select_feet"->{optionChoosen.value = "select_feet"}
//            "select_inches"->{optionChoosen.value = "select_inches"}
//            "submit"->{
//                when{
//                    originEthnicity.value.toString().trim().isEmpty() -> {
//                        errorMessage.value = resourceProvider.getString(R.string.please_select_origin_ethnicity)
//                    }
//                    educationLevel.value.toString().trim().isEmpty() -> {
//                        errorMessage.value = resourceProvider.getString(R.string.please_enter_education_level)
//                    }
//
//                    work.value.toString().trim().isEmpty() -> {
//                        errorMessage.value = resourceProvider.getString(R.string.please_select_work)
//                    }
//
//                    datingPreference.value.toString().trim().isEmpty() -> {
//                        errorMessage.value = resourceProvider.getString(R.string.please_select_dating_pref)
//                    }
//
//                    religiousBeliefs.value.toString().trim().isEmpty() -> {
//                        errorMessage.value = resourceProvider.getString(R.string.please_select_religous_beliefs)
//                    }
//
//                    else ->{
//                        val jsonObj = JsonObject()
//                        jsonObj.addProperty("height", "${feet.value} ${inches.value}")
//                        jsonObj.addProperty("ethnicity", originEthnicity.value)
//                        jsonObj.addProperty("educationLevel", educationLevel.value)
//                        jsonObj.addProperty("work", work.value)
//                        jsonObj.addProperty("datingPreference", datingPreference.value)
//                        jsonObj.addProperty("religiousBelief", religiousBeliefs.value)
//                        jsonObj.addProperty("doyoudrink", doYouDrink.value)
//                        jsonObj.addProperty("doyousmoke", doYouSmoke.value)
//                        jsonObj.addProperty("interestedIn", interesedIn.value)
//                        jsonObj.addProperty("profileStatus", 3)
//
//                        Log.d("ABOUT_ME_RQST_BODY_DATA", "validateInputs: $jsonObj")
//                        callUpdateProfileApi(
//                                jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
//                        )
//                    }
//                }
//
//            }
//
//
//        }
//    }
}