package com.padedatingapp.ui.onboarding.fragments.upload_photo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.UploadPhotoRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
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

class UploadPhotoVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val uploadPhotoRepo: UploadPhotoRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()
    var uploadFileResponse = SingleLiveEvent<Resource<ResultModel<ImageUploadResponse>>>()
    var file: File? = null
    var token = ""
    var profilePicUrl = ""
    fun callUpdatePicApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(uploadPhotoRepo.setUpProfile("Bearer $token", body))
        }
    }


    fun callUploadFile( source: MultipartBody.Part?,thumb: MultipartBody.Part?,type:  RequestBody) {
        coroutinesManager.ioScope.launch {
            uploadFileResponse.postValue(Resource.loading(null))
            uploadFileResponse.postValue(uploadPhotoRepo.uploadFile("Bearer $token", source,thumb,type))
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
}