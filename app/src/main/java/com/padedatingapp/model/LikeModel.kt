package com.padedatingapp.model

data class LikeModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)