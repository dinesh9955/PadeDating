package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.HomeRepo
import com.padedatingapp.api.repository.MeetMeRepo
import com.padedatingapp.api.repository.ProfileOtherRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.*
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody


class ProfileOtherVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val profileOtherRepo: ProfileOtherRepo

) : ViewModel() {
    var meetMeResponse = SingleLiveEvent<Resource<ResultModel<MeetMeData>>>()
    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callMeetMeApi(body: String) {
        coroutinesManager.ioScope.launch {
            meetMeResponse.postValue(Resource.loading(null))
            meetMeResponse.postValue(profileOtherRepo.oneUser("Bearer $token", body))
        }
    }





}