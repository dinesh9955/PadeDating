package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.waveModel.WaveCardResponse
import com.padedatingapp.model.wavepayment.WavePaymentResponse
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiftCardRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun giftCards(token: String): Resource<ResultModel<AllGiftCard>> {
        return handleException { padeApi.giftCards(token) }
    }

    suspend fun paymentAPI(token: String, toRequestBody: RequestBody): Resource<BlockUserModel> {
        return handleException { padeApi.payment(token, toRequestBody) }
    }

    suspend fun waveCard(token: String, toRequestBody: RequestBody): Resource<WaveCardResponse> {
        return handleException { padeApi.waveCard(token, toRequestBody) }
    }

    suspend fun wavePayment(token: String, toRequestBody: RequestBody): Resource<WavePaymentResponse> {
        return handleException { padeApi.wavePayment(token, toRequestBody) }
    }


}