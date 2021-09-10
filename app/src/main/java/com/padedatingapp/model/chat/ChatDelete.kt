package com.padedatingapp.model.chat

data class ChatDelete(
    val `data`: Any,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)