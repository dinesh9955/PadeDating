package com.padedatingapp.model.waveModel

data class Data(
    val account_id: Int,
    val amount: Int,
    val app_fee: Double,
    val auth_model: String,
    val auth_url: String,
    val card: Card,
    val charge_type: String,
    val charged_amount: Int,
    val created_at: String,
    val currency: String,
    val customer: Customer,
    val device_fingerprint: String,
    val flw_ref: String,
    val fraud_status: String,
    val id: Int,
    val ip: String,
    val merchant_fee: Int,
    val narration: String,
    val payment_type: String,
    val plan: Any,
    val processor_response: String,
    val status: String,
    val tx_ref: String
)