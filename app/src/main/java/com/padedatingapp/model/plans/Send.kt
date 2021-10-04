package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Send(
    val count: Int,
    val type: String
): Parcelable