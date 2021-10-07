package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ChatRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.reasons.ReasonModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ChatVM(
        private val resourceProvider: ResourceProvider,
        private val coroutinesManager: CoroutinesManager,
        private val aboutMeRepo: ChatRepo
) : ViewModel() {
    var deleteMessageResponse = SingleLiveEvent<Resource<ChatDelete>>()
    var loginResponse = SingleLiveEvent<Resource<ChatUsers>>()
    var callUserResponse = SingleLiveEvent<Resource<CallUser>>()
    var blockUserResponse = SingleLiveEvent<Resource<BlockUserModel>>()
    var reasonModel = SingleLiveEvent<Resource<ReasonModel>>()


    var meetMeResponse = SingleLiveEvent<Resource<ResultModel<MeetMeData>>>()

    var token = ""

    var errorMessage =  MutableLiveData("")


//    fun chatHistoryApi(receiverID: String?) {
//        coroutinesManager.ioScope.launch {
//            loginResponse.postValue(Resource.loading(null))
//            loginResponse.postValue(aboutMeRepo.chatHistory("Bearer $token", receiverID!!))
//        }
//    }



    fun chatHistoryApi(receiverID: String?) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.chatHistory("Bearer $token", receiverID!!))
        }
    }


    fun deleteChatApi(receiverID: String?) {
        coroutinesManager.ioScope.launch {
            deleteMessageResponse.postValue(Resource.loading(null))
            deleteMessageResponse.postValue(aboutMeRepo.deleteChat("Bearer $token", receiverID!!))
        }
    }



    fun callApi(receiverID: RequestBody) {
        coroutinesManager.ioScope.launch {
            callUserResponse.postValue(Resource.loading(null))
            callUserResponse.postValue(aboutMeRepo.call("Bearer $token", receiverID!!))
        }
    }

    fun blockApi(receiverID: String) {
        coroutinesManager.ioScope.launch {
            blockUserResponse.postValue(Resource.loading(null))
            blockUserResponse.postValue(aboutMeRepo.blockUser("Bearer $token", receiverID!!))
        }
    }


    fun unblockApi(receiverID: String) {
        coroutinesManager.ioScope.launch {
            blockUserResponse.postValue(Resource.loading(null))
            blockUserResponse.postValue(aboutMeRepo.blockUser("Bearer $token", receiverID!!))
        }
    }


    fun callMeetMeApi(body: String) {
        coroutinesManager.ioScope.launch {
            meetMeResponse.postValue(Resource.loading(null))
            meetMeResponse.postValue(aboutMeRepo.oneUser("Bearer $token", body))
        }
    }

    fun reasonApi() {
        coroutinesManager.ioScope.launch {
            reasonModel.postValue(Resource.loading(null))
            reasonModel.postValue(aboutMeRepo.reasonApi("Bearer $token"))
        }
    }



}