package com.padedatingapp.model.call

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class CallUser(
    val `data`: Data,
    val message: String,
    val statusCode: Int,
    val success: Boolean
): Serializable