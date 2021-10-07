package com.padedatingapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.GiftCardRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.utils.ResourceProvider
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File

class BuyGiftVM(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val aboutMeRepo: GiftCardRepo
) : ViewModel() {
    var loginResponsePayment = SingleLiveEvent<Resource<BlockUserModel>>()

    var file: File? = null
    var token = ""

    var errorMessage =  MutableLiveData("")


    fun paymentAPI(toRequestBody: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponsePayment.postValue(Resource.loading(null))
            loginResponsePayment.postValue(aboutMeRepo.paymentAPI("Bearer $token", toRequestBody))
        }
    }

}