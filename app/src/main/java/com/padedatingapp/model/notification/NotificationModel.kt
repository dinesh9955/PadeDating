package com.padedatingapp.model.notification

data class NotificationModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)