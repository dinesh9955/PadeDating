package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.LikeModel
import com.padedatingapp.model.MeetMe
import com.padedatingapp.model.MyMatches
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun chatUserList(token: String, requestBody: RequestBody): Resource<MeetMe> {
        return handleException { padeApi.chatUserList(token, requestBody) }
    }


    suspend fun chatHistory(token: String, requestBody: RequestBody): Resource<MeetMe> {
        return handleException { padeApi.chatHistory(token, requestBody) }
    }



}