package com.padedatingapp.model.notification

data class Data(
    val docs: List<Doc>,
    val itemCount: Int,
    val pageCount: Int,
    val unread: Int
)