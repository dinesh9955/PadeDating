package com.padedatingapp.model

data class Doc(
    val __v: Int,
    val _id: String,
    val cardName: String,
    val cardsBought: Int,
    val cost: Int,
    val createdAt: String,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val updatedAt: String,
    val validFrom: String,
    val validTill: String
)