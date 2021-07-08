package com.padedatingapp.model

data class ResultModel<T>(val statusCode: Int, var success: Boolean = false, val message: String, val data: T?)

