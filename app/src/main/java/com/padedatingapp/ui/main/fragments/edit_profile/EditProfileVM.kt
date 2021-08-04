package com.padedatingapp.ui.main.fragments.edit_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.iid.FirebaseInstanceId
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.EditProfileRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.ImageModel
import com.padedatingapp.model.ImageUploadResponse
import com.padedatingapp.model.ResultModel
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

class EditProfileVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val editProfileRepo: EditProfileRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var uploadFileResponse = SingleLiveEvent<Resource<ResultModel<ImageUploadResponse>>>()
    var file: File? = null
    var token = ""
    var profilePicUrl = ""
    var firstName = MutableLiveData<String>("")
    var lastName = MutableLiveData<String>("")
    var gender = MutableLiveData<String>("")
    var country = MutableLiveData<String>("")
    var city = MutableLiveData<String>("")
    var address = MutableLiveData<String>("")
    var _errorMessage = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()
    var state = MutableLiveData<String>("")
    val list = ArrayList<ImageModel>()

    fun callUpdatePicApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(editProfileRepo.setUpProfile("Bearer $token", body))
        }
    }


    fun callUploadFile( source: MultipartBody.Part?,thumb: MultipartBody.Part?,type:  RequestBody) {
        coroutinesManager.ioScope.launch {
            uploadFileResponse.postValue(Resource.loading(null))
            uploadFileResponse.postValue(editProfileRepo.uploadFile("Bearer $token", source,thumb,type))
        }
    }

    fun onClick(type: String) {
       when(type){
           "skip" ->{
               val jsonObj = JSONObject()
               val jsonArray = JSONArray()
               jsonObj.put("profileStatus", 2)
               Log.d("REGISTER_RQST_BODY_DATA", "Skip case for upload profile: $jsonObj")
               callUpdatePicApi(
                   jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull()))
           }
       }
    }

    fun validate() {
        when {
            firstName.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_first_name)
            }
            lastName.value.toString().trim().isEmpty() -> {
                _errorMessage.value = resourceProvider.getString(R.string.please_enter_last_name)
            }
            else ->{
                val jsonObj = JSONObject()
                val jsonArray = JSONArray()
                jsonObj.put("firstName", firstName.value)
                jsonObj.put("lastName", lastName.value)
                jsonObj.put("gender", gender.value)
                jsonObj.put("city", city.value)
                jsonObj.put("country", country.value)
                jsonObj.put("state", state.value)
                jsonObj.put("address", address.value)
                jsonObj.put("latitude", latitude.value.toString())
                jsonObj.put("longitude", longitude.value.toString())
                jsonObj.put("profileStatus", 3)
               // jsonObj.put("deviceType", "ANDROID")
                jsonObj.put("deviceToken", FirebaseInstanceId.getInstance().getToken())
                for (url in list) {
                    if (url.source != "add") {
                        val obj = JSONObject()
                        obj.put("source", url.source)
                        obj.put("type", url.type)
                        obj.put("thumb", url.thumb)
                        jsonArray.put(obj)
                    }
                }
                jsonObj.put("image", profilePicUrl)
                jsonObj.put("docImage", jsonArray)

                Log.d("REGISTER_RQST_BODY_DATA", "Edit profile: $jsonObj")
                callUpdatePicApi(
                    jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                )
            }
        }}
}