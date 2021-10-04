package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.HomeRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.plans.PlanModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class BuyPremiumVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: HomeRepo
) : ViewModel() {
    var loginResponse = SingleLiveEvent<Resource<PlanModel>>()
    var token = ""

    var errorMessage =  MutableLiveData("")



    fun callPlansApi(body: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponse.postValue(Resource.loading(null))
            loginResponse.postValue(aboutMeRepo.plans("Bearer $token", body))
        }
    }


}