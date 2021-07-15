package com.padedatingapp.model.chat

data class ChatUsersData(
    val __v: Int,
    val _id: String,
    val isDeleted: Boolean,
    val isRead: Boolean,
    val matches: List<String>,
    val modMsg: String,
    val readAt: String,
    val sentAt: String,
    val sentBy: SentBy,
    val sentTo: SentTo,
    val user1: String,
    val user2: String
)