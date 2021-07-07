package com.padedatingapp.extensions

import com.padedatingapp.api.Resource
import com.padedatingapp.api.remote.AppException
import retrofit2.HttpException

suspend fun <T> handleException(apiCall: suspend () -> T): Resource<T> {

    return try {
        Resource.success(apiCall.invoke())
    } catch (throwable: Throwable) {
        if (throwable is HttpException) {
            throwable.response()?.let {
                var str  = StringBuilder(it.errorBody()!!.string())
                return Resource.error(AppException(throwable, str))
            }
        }
        Resource.error(AppException(throwable))
    }
}