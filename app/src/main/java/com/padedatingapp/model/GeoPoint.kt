package com.padedatingapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoPoint(
    val coordinates: List<Double>,
    val type: String
):Parcelable