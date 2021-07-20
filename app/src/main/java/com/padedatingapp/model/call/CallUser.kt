package com.padedatingapp.model.call

data class CallUser(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)