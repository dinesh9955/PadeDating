package com.padedatingapp.model.reportUser

data class ReportUserModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)