package com.padedatingapp.api

import com.padedatingapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PadeDatingApi {

    @POST(NetworkUrls.REGISTER_USER)
    suspend fun registerUser(
        @Body body: RequestBody
    ): Result<UserModel>

    @POST(NetworkUrls.VERIFY_OTP)
    suspend fun verifyOtp(
        @Body body: RequestBody
    ): Result<UserModel>

    @POST(NetworkUrls.SEND_OTP)
    suspend fun sendOtp(
        @Body body: RequestBody
    ): Result<OtpData>


    @POST(NetworkUrls.PROFILE_SETUP)
    suspend fun profileSetUp(
        @Header("Authorization") token:String,
        @Body body: RequestBody
    ): Result<UserModel>

    @POST(NetworkUrls.CHECK_USERNAME)
    suspend fun checkUsername(
        @Header("Authorization") token:String,
        @Body body: RequestBody
    ): Result<UsernameResponse>

    @POST(NetworkUrls.LOGIN)
    suspend fun login(
        @Body body: RequestBody
    ): Result<UserModel>

    @Multipart
    @POST(NetworkUrls.UPLOAD_FILE)
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Part source: MultipartBody.Part?,
        @Part thumb: MultipartBody.Part?,
        @Part("type") type:RequestBody
    ): Result<ImageUploadResponse>

    @POST(NetworkUrls.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Body body: RequestBody
    ): Result<UserModel>

    @POST(NetworkUrls.RESET_PASSWORD)
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Result<UserModel>

    @POST(NetworkUrls.StaffByCategory)
    suspend fun staffByCategory(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Result<UserModel>


    @POST(NetworkUrls.giftCards)
    suspend fun giftCards(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Result<AllGiftCard>



}