package com.padedatingapp.api.repository

import com.padedatingapp.api.PadeDatingApi
import com.padedatingapp.api.Resource
import com.padedatingapp.extensions.handleException
import com.padedatingapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileRepo @Inject constructor(private val padeApi: PadeDatingApi) {

    suspend fun uploadFile(token: String,source: MultipartBody.Part?,thumb: MultipartBody.Part?,type:  RequestBody): Resource<Result<ImageUploadResponse>> {
        return handleException { padeApi.uploadFile(token,source,thumb,type) }
    }

    suspend fun setUpProfile(token: String, requestBody: RequestBody): Resource<Result<UserModel>> {
        return handleException { padeApi.profileSetUp(token, requestBody) }
    }
}