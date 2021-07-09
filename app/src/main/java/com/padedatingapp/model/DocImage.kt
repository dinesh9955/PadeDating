package com.padedatingapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class DocImage(
        val _id: String,
        val source: String,
        val thumb:@RawValue Any,
        val type: String
):Parcelable