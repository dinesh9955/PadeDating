package com.padedatingapp.model.call

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class Data(
    var apikey: String,
    var callType: String,
    val sessionId: String,
    val token: String,
    val user1: User1,
    val user2: User2
): Serializable