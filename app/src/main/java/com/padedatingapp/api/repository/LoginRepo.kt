package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.OtpData
import com.padedatingapp.model.Result
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.UsernameResponse
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun login(requestBody: RequestBody): Resource<Result<UserModel>> {
        return handleException { padeApi.login(requestBody) }
    }
}