package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.UsernameResponse
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun registerUser(requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.registerUser(requestBody) }
    }

    suspend fun sendOtp(requestBody: RequestBody): Resource<ResultModel<OtpData>> {
        return handleException { padeApi.sendOtp(requestBody) }
    }

    suspend fun setUpProfile(token: String, requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.profileSetUp(token, requestBody) }
    }

    suspend fun checkUsername(
        token: String,
        requestBody: RequestBody
    ): Resource<ResultModel<UsernameResponse>> {
        return handleException { padeApi.checkUsername(token, requestBody) }
    }
}