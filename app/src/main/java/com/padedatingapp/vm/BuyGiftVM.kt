package com.padedatingapp.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padedatingapp.api.Resource
import com.padedatingapp.api.repository.GiftCardRepo
import com.padedatingapp.event.SingleLiveEvent
import com.padedatingapp.manager.CoroutinesManager
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.waveModel.WaveCardResponse
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
    var waveCardResponse = SingleLiveEvent<Resource<WaveCardResponse>>()
    var isChecked  = ObservableField<Boolean>(false)

    var file: File? = null
    var token = ""

    var errorMessage =  MutableLiveData("")


    fun paymentAPI(toRequestBody: RequestBody) {
        coroutinesManager.ioScope.launch {
            loginResponsePayment.postValue(Resource.loading(null))
            loginResponsePayment.postValue(aboutMeRepo.paymentAPI("Bearer $token", toRequestBody))
        }
    }


    fun waveCardApi(toRequestBody: RequestBody) {
        coroutinesManager.ioScope.launch {
            waveCardResponse.postValue(Resource.loading(null))
            waveCardResponse.postValue(aboutMeRepo.waveCard("Bearer $token", toRequestBody))
        }
    }

}