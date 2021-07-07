package com.padedatingapp.api

import com.padedatingapp.api.remote.AppException

class Resource<out T> private constructor(val status: Status, val data: T?, val exception: AppException?) {
    enum class Status {
        SUCCESS, ERROR, LOADING, CANCEL
    }
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(exception: AppException? = null): Resource<T> {
            return Resource(
                Status.ERROR,
               null,
                exception
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }

        fun <T> cancel(data: T? = null, exception: AppException? = null): Resource<T> {
            return Resource(
                Status.CANCEL,
                data,
                exception
            )
        }
    }

    fun getErrorMessage() = exception?.getErrorMessage()
    fun getErrorCode() = exception?.getErrorCode()
    fun getErrorData() = exception?.getErrorData()
    fun isNetworkError() = exception?.isNetworkError()
}