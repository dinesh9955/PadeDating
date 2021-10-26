package com.padedatingapp.model

data class MeetMe(
        val data: List<MeetMeData>,
        val message: String,
        val statusCode: Int,
        val success: Boolean,
        val totalcount: Int

)