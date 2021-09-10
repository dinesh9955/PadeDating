package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.LikeModel
import com.padedatingapp.model.MeetMe
import com.padedatingapp.model.MyMatches
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun chatUserList(token: String, requestBody: RequestBody): Resource<ChatUsers> {
        return handleException { padeApi.chatUserList(token) }
    }


    suspend fun chatHistory(token: String, receiverID: String): Resource<ChatUsers> {
        return handleException { padeApi.chatHistory(token, receiverID) }
    }

    suspend fun call(token: String, receiverID: RequestBody): Resource<CallUser> {
        return handleException { padeApi.call(token, receiverID) }
    }

    suspend fun deleteChat(token: String, receiverID: String): Resource<ChatDelete> {
        return handleException { padeApi.deleteChat(token, receiverID) }
    }


}