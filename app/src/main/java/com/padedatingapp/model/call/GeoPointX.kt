package com.padedatingapp.model.call

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable


data class GeoPointX(
    val coordinates: @RawValue List<Double>,
    val type: String
): Serializable