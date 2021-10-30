package com.padedatingapp.model.slider

data class SliderModel(
    val `data`: List<Data>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)