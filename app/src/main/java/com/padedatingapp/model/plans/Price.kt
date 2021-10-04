package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Price(
    val amount: Int,
    val code: String,
    val units: String
): Parcelable