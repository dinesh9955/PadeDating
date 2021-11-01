package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ChatRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.blockUser.BlockModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.notification.NotificationModel
import com.padedatingapp.model.slider.SliderModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class BlockUserVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: ChatRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<MyMatches>>()
    var blockUsersResponse = SingleLiveEvent<Resource<BlockModel>>()
    var unblockUserResponse = SingleLiveEvent<Resource<BlockUserModel>>()
    var sliderImageResponse = SingleLiveEvent<Resource<SliderModel>>()
    var notificationModelResponse = SingleLiveEvent<Resource<NotificationModel>>()

    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callBlockUserApi() {
        coroutinesManager.ioScope.launch {
            blockUsersResponse.postValue(Resource.loading(null))
            blockUsersResponse.postValue(aboutMeRepo.blockUsersApi("Bearer $token"))
        }
    }



    fun callUnBlockApi(receiverID: String) {
        coroutinesManager.ioScope.launch {
            unblockUserResponse.postValue(Resource.loading(null))
            unblockUserResponse.postValue(aboutMeRepo.unblockUser("Bearer $token", receiverID!!))
        }
    }



    fun callsliderImageApi() {
        coroutinesManager.ioScope.launch {
            sliderImageResponse.postValue(Resource.loading(null))
            sliderImageResponse.postValue(aboutMeRepo.sliderImages())
        }
    }


    fun callNotificationApi(receiverID: RequestBody) {
        coroutinesManager.ioScope.launch {
            notificationModelResponse.postValue(Resource.loading(null))
            notificationModelResponse.postValue(aboutMeRepo.notifications("Bearer $token", receiverID!!))
        }
    }






}