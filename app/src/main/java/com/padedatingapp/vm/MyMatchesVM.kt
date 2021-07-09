package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.HomeRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MeetMe
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.ResultModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class MyMatchesVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val aboutMeRepo: HomeRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<MyMatches>>()
    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callMyMatchesApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.myMatches("Bearer $token", body))
        }
    }


}