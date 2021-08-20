package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.MeetMeData
import com.padedatingapp.model.ResultModel
import com.padedatingapp.model.UserModel
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatUsers
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileOtherRepo @Inject constructor(private val padeApi: PadeDatingApi) {


    suspend fun oneUser(token: String, requestBody: String): Resource<ResultModel<MeetMeData>> {
        return handleException { padeApi.oneUser(token, requestBody) }
    }


//    suspend fun oneUser(requestBody: RequestBody): Resource<ResultModel<UserModel>> {
//        return handleException { padeApi.login(requestBody) }
//    }
//
//    suspend fun chatUserList(token: String, requestBody: RequestBody): Resource<ChatUsers> {
//        return handleException { padeApi.chatUserList(token) }
//    }
//
//
//    suspend fun chatHistory(token: String, receiverID: String): Resource<ChatUsers> {
//        return handleException { padeApi.chatHistory(token, receiverID) }
//    }
//
//    suspend fun call(token: String, receiverID: RequestBody): Resource<CallUser> {
//        return handleException { padeApi.call(token, receiverID) }
//    }
//



}