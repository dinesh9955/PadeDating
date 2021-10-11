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
import com.padedatingapp.model.privacyPolicy.PrivacyPolicyModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch

class WebVM (
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: ChatRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<MyMatches>>()
    var privacyPolicyResponse = SingleLiveEvent<Resource<PrivacyPolicyModel>>()

    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callPrivacyPolicyApi() {
        coroutinesManager.ioScope.launch {
            privacyPolicyResponse.postValue(Resource.loading(null))
            privacyPolicyResponse.postValue(aboutMeRepo.privacyPolicyApi("Bearer $token"))
        }
    }




}