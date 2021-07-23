package com.padedatingapp.model.call

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class DocImage(
    val _id: String,
    val source: String,
    val thumb: String,
    val type: String
): Serializable