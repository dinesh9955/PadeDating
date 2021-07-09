package com.padedatingapp.model

data class MyMatches(
        val data: List<MeetMeData>,
        val message: String,
        val statusCode: Int,
        val success: Boolean
)