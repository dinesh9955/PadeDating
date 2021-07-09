package com.padedatingapp.model

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val dislikedBy: List<Any>,
    val isMatched: Boolean,
    val likedBy: List<String>,
    val updatedAt: String,
    val users: List<String>
)