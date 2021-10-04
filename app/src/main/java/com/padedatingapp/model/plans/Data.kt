package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val doc: List<Doc>,
    val itemCount: Int
): Parcelable