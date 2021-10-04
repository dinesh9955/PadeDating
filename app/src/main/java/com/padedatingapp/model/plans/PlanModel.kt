package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlanModel(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
): Parcelable