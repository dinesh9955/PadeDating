package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtpRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun sendOtp(requestBody: RequestBody): Resource<ResultModel<OtpData>> {
        return handleException { padeApi.sendOtp(requestBody)}
    }

    suspend fun verifyOtp(requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.verifyOtp(requestBody)}
    }


}