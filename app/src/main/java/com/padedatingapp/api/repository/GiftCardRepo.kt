package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.AllGiftCard
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiftCardRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun giftCards(token: String, requestBody: RequestBody): Resource<Result<AllGiftCard>> {
        return handleException { padeApi.giftCards(token, requestBody) }
    }
}