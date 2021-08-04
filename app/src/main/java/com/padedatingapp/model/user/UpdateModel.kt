package com.padedatingapp.model.user

data class UpdateModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)