package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ChatRepo
import com.padedatingapp.api.repository.HomeRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ChatUserVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val aboutMeRepo: ChatRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<ChatUsers>>()
    var token = ""

    var errorMessage =  MutableLiveData("")



    fun chatUserListApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.chatUserList("Bearer $token", body))
        }
    }


}