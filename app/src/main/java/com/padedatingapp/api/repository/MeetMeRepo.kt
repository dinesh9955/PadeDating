package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetMeRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun meetMe(token: String, requestBody: RequestBody): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.profileSetUp(token, requestBody) }
    }

}