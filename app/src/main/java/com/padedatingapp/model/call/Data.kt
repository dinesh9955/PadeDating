package com.padedatingapp.model.call

data class Data(
    val apikey: String,
    val callType: String,
    val sessionId: String,
    val token: String,
    val user1: User1,
    val user2: User2
)