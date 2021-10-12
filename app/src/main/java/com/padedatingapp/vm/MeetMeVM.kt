package com.padedatingapp.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.padedatingapp.R
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.AboutMeRepo
import com.padedatingapp.api.repository.HomeRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.*
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MeetMeVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val aboutMeRepo: HomeRepo
) : ViewModel() {
    var meetMeResponse = SingleLiveEvent<Resource<MeetMe>>()
    var likeModelResponse = SingleLiveEvent<Resource<LikeModel>>()
    var userModelResponse = SingleLiveEvent<Resource<ResultModel<UserModel>>>()


    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callMeetMeApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            meetMeResponse.postValue(Resource.loading(null))
            meetMeResponse.postValue(aboutMeRepo.meetMe("Bearer $token", body))
        }
    }



    fun callMeetMeLikeApi(id : String, body: RequestBody) {
        coroutinesManager.ioScope.launch {
            likeModelResponse.postValue(Resource.loading(null))
            likeModelResponse.postValue(aboutMeRepo.meetMeLike(id, "Bearer $token", body))
        }
    }


    fun callProfileApi(body: String) {
        coroutinesManager.ioScope.launch {
            userModelResponse.postValue(Resource.loading(null))
            userModelResponse.postValue(aboutMeRepo.oneProfile("Bearer $token", body))
        }
    }



//    fun callProfileApi(body: String) {
//        coroutinesManager.ioScope.launch {
//            userModelResponse.postValue(Resource.loading(null))
//            userModelResponse.postValue(aboutMeRepo.profile("Bearer $token", body))
//        }
//    }

}