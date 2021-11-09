package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.ChatRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.blockUser.BlockModel
import com.padedatingapp.model.loyalityModel.Data
import com.padedatingapp.model.loyalityModel.DataX
import com.padedatingapp.model.loyalityModel.LoyalityPointsResponse
import com.padedatingapp.model.privacyPolicy.PrivacyPolicyModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch

class LoyalityVM  ( private val resourceProvider: ResourceProvider,
private val coroutinesManager: CoroutinesManager,
private val aboutMeRepo: ChatRepo
) : ViewModel() {
    var blockUsersResponse = SingleLiveEvent<Resource<LoyalityPointsResponse>>()
    var token = ""

    fun callLoyalityPoints() {
        coroutinesManager.ioScope.launch {
            blockUsersResponse.postValue(Resource.loading(null))
            blockUsersResponse.postValue(aboutMeRepo.loyalityPointApi("Bearer $token"))
        }
    }
}


