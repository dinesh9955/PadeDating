package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.*
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AboutMeRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun setUpProfile(token: String, requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.profileSetUp(token, requestBody) }
    }

}