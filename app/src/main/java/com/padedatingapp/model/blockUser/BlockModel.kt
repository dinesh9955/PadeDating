package com.padedatingapp.model.blockUser

data class BlockModel(
    val `data`: List<Data>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)