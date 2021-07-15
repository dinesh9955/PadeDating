package com.padedatingapp.model.chat

data class ChatUsers(
        val `data`: List<ChatUsersData>,
        val message: String,
        val statusCode: Int,
        val success: Boolean
)