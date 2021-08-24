package com.padedatingapp.model.otp

data class OtpForgotMain(
    val `data`: String,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)