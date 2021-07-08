package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.MeetMe
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun meetMe(token: String, requestBody: RequestBody): Resource<MeetMe> {
        return handleException { padeApi.meetMe(token, requestBody) }
    }


    suspend fun myMatches(token: String, requestBody: RequestBody): Resource<ResultModel<MyMatches>> {
        return handleException { padeApi.myMatches(token) }
    }


}