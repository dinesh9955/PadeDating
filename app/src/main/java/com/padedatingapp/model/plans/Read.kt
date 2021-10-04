package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Read(
    val count: Int,
    val type: String
): Parcelable