package com.padedatingapp.model.notification

data class DataX(
    val __v: Int,
    val _id: String,
    val callData: CallData,
    val isDeleted: Boolean,
    val isRead: Boolean,
    val modMsg: String,
    val rawMsg: String,
    val readAt: String,
    val sentAt: String,
    val sentBy: SentBy,
    val sentTo: SentTo,
    val type: String,
    val user1: User1,
    val user2: User2
)