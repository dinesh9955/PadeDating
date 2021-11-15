package com.padedatingapp.model.wavepayment

data class Card(
    val country: String,
    val expiry: String,
    val first_6digits: String,
    val issuer: String,
    val last_4digits: String,
    val type: String
)