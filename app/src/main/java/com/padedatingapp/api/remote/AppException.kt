package com.padedatingapp.api.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

open class AppException(
    private val exception: Throwable?,
    private val errorBody: StringBuilder? = null
) : Exception() {

    constructor(message: String) : this(Throwable(message))

    fun getErrorCode(): String? =
        if (exception is RetrofitException) exception.getErrorCode() else exception?.message

    fun getErrorData(): JsonObject {
        return try {
            if(errorBody != null) {
                val jsonObject = Gson().fromJson(errorBody.toString(), JsonObject::class.java)
                jsonObject
            }
            else JsonObject()
            // Do something here
        } catch (e: JsonParseException) {
            JsonObject()
        }
    }


    fun getErrorMessage(): String? =
        if (exception is RetrofitException) exception.getErrorMessage() else exception?.message

    fun isNetworkError() =
        exception is RetrofitException && exception.getKind() == RetrofitException.Kind.NETWORK
}