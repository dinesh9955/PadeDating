package com.padedatingapp.api

import com.padedatingapp.model.*
import com.padedatingapp.model.call.CallUser
import com.padedatingapp.model.chat.ChatDelete
import com.padedatingapp.model.chat.ChatUsers
import com.padedatingapp.model.otp.OtpForgotMain
import com.padedatingapp.model.plans.PlanModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface PadeDatingApi {

    @POST(NetworkUrls.REGISTER_USER)
    suspend fun registerUser(
        @Body body: RequestBody
    ): ResultModel<UserModel>


    @POST(NetworkUrls.SOCIAL_USER)
    suspend fun socialUser(
        @Body body: RequestBody
    ): ResultModel<UserModel>


    @POST(NetworkUrls.VERIFY_OTP)
    suspend fun verifyOtp(
        @Body body: RequestBody
    ): ResultModel<UserModel>

    @POST(NetworkUrls.SEND_OTP)
    suspend fun sendOtp(
        @Body body: RequestBody
    ): ResultModel<OtpData>


    @POST(NetworkUrls.PROFILE_SETUP)
    suspend fun profileSetUp(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ResultModel<UserModel>


//    @POST(NetworkUrls.PROFILE_SETUP)
//    suspend fun profileSetUpUserData(
//        @Header("Authorization") token:String,
//        @Body body: RequestBody
//    ): UpdateModel


    @POST(NetworkUrls.CHECK_USERNAME)
    suspend fun checkUsername(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ResultModel<UsernameResponse>

    @POST(NetworkUrls.LOGIN)
    suspend fun login(
        @Body body: RequestBody
    ): ResultModel<UserModel>

    @Multipart
    @POST(NetworkUrls.UPLOAD_FILE)
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Part source: MultipartBody.Part?,
        @Part thumb: MultipartBody.Part?,
        @Part("type") type: RequestBody
    ): ResultModel<ImageUploadResponse>

    @POST(NetworkUrls.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Body body: RequestBody
    ): OtpForgotMain

    @POST(NetworkUrls.RESET_PASSWORD)
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ResultModel<UserModel>

    @POST(NetworkUrls.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): OtpForgotMain



    @POST(NetworkUrls.StaffByCategory)
    suspend fun staffByCategory(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ResultModel<UserModel>


    @POST(NetworkUrls.payment)
    suspend fun payment(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): ResultModel<AllGiftCard>


    @GET(NetworkUrls.giftCards)
    suspend fun giftCards(
        @Header("Authorization") token: String
//        @Body body: RequestBody
    ): ResultModel<AllGiftCard>



    @POST(NetworkUrls.explore)
    suspend fun meetMe(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): MeetMe


    @GET(NetworkUrls.matches)
    suspend fun myMatches(
        @Header("Authorization") token: String
//            @Query("limit") limit: String,
//            @Query("page") page: String
        // @Body body: RequestBody
    ): MyMatches



    @GET(NetworkUrls.plans)
    suspend fun plans(
        @Header("Authorization") token: String
//            @Query("limit") limit: String,
//            @Query("page") page: String
        // @Body body: RequestBody
    ): PlanModel


    @PUT(NetworkUrls.profile + "{id}")
    suspend fun meetMeLike(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): LikeModel



    @GET(NetworkUrls.chats)
    suspend fun chatUserList(
        @Header("Authorization") token: String,
//            @Body body: RequestBody
    ): ChatUsers


//    @GET(NetworkUrls.chatsuser+"")
//    suspend fun chatHistory(
//            @Header("Authorization") token: String,
//            @Query("/chats/:user") receiverID: String
//    ): ChatUsers


    @GET(NetworkUrls.chatsuser + "{user}")
    suspend fun chatHistory(
        @Header("Authorization") token: String,
        @Path("user") filter: String,
    ): ChatUsers


    @DELETE(NetworkUrls.deleteMessages + "{id}")
    suspend fun deleteChat(
        @Header("Authorization") token: String,
        @Path("id") _id: String,
    ): ChatDelete




    @GET(NetworkUrls.users + "{id}")
    suspend fun oneUser(
        @Header("Authorization") token: String,
        @Path("id") filter: String,
    ): ResultModel<MeetMeData>



    @POST(NetworkUrls.makeCall)
    suspend fun call(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): CallUser



}