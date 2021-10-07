package com.padedatingapp.model.reasons

data class ReasonModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)