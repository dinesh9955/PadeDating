package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.*
import com.padedatingapp.model.otp.OtpForgotMain
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgotPasswordRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun sendOtp(requestBody: RequestBody): Resource<ResultModel<OtpData>> {
        return handleException { padeApi.sendOtp(requestBody)}
    }

    suspend fun verifyOtp(requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.verifyOtp(requestBody)}
    }

    suspend fun forgotPassword(requestBody: RequestBody): Resource<OtpForgotMain> {
        return handleException { padeApi.forgotPassword(requestBody)}
    }

    suspend fun resetPassword(token:String,requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.resetPassword(token, requestBody)}
    }


}