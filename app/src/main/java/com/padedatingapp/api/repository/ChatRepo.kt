package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.*
import com.padedatingapp.model.blockUser.BlockModel
import com.padedatingapp.model.blockUser.BlockUserModel
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.notification.NotificationModel
import com.padedatingapp.model.privacyPolicy.PrivacyPolicyModel
import com.padedatingapp.model.reasons.ReasonModel
import com.padedatingapp.model.reportUser.ReportUserModel
import com.padedatingapp.model.slider.SliderModel
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

    suspend fun reportUser(token: String, receiverID: RequestBody): Resource<ReportUserModel> {
        return handleException { padeApi.reportUser(token, receiverID) }
    }


    suspend fun blockUser(token: String, receiverID: String): Resource<BlockUserModel> {
        return handleException { padeApi.blockUser(token, receiverID) }
    }

    suspend fun unblockUser(token: String, receiverID: String): Resource<BlockUserModel> {
        return handleException { padeApi.unblockUser(token, receiverID) }
    }

    suspend fun notifications(token: String, receiverID: RequestBody): Resource<NotificationModel> {
        return handleException { padeApi.notification(token, receiverID) }
    }

    suspend fun deleteChat(token: String, receiverID: String): Resource<ChatDelete> {
        return handleException { padeApi.deleteChat(token, receiverID) }
    }

    suspend fun oneUser(token: String, requestBody: String): Resource<ResultModel<MeetMeData>> {
        return handleException { padeApi.oneUser(token, requestBody) }
    }


    suspend fun reasonApi(token: String): Resource<ReasonModel> {
        return handleException { padeApi.reason(token) }
    }


    suspend fun blockUsersApi(token: String): Resource<BlockModel> {
        return handleException { padeApi.blockUsers(token) }
    }

    suspend fun privacyPolicyApi(token: String): Resource<PrivacyPolicyModel> {
        return handleException { padeApi.privacyPolicy(token) }
    }


    suspend fun myMatches(token: String, requestBody: RequestBody): Resource<MyMatches> {
        return handleException { padeApi.myMatches(token) }
    }


    suspend fun sliderImages(): Resource<SliderModel> {
        return handleException { padeApi.sliderImages() }
    }



}