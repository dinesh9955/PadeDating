package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.*
import com.padedatingapp.model.plans.PlanModel
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun meetMe(token: String, requestBody: RequestBody): Resource<MeetMe> {
        return handleException { padeApi.meetMe(token, requestBody) }
    }


    suspend fun myMatches(token: String, requestBody: RequestBody): Resource<MyMatches> {
        return handleException { padeApi.myMatches(token) }
    }


    suspend fun meetMeLike(id: String, token: String, requestBody: RequestBody): Resource<LikeModel> {
        return handleException { padeApi.meetMeLike(id, token, requestBody) }
    }


    suspend fun plans(token: String, requestBody: RequestBody): Resource<PlanModel> {
        return handleException { padeApi.plans(token) }
    }


    suspend fun oneProfile(token: String, requestBody: String): Resource<ResultModel<UserModel>> {
        return handleException { padeApi.profile(token, requestBody) }
    }

}