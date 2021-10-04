package com.padedatingapp.model.plans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doc(
    val __v: Int,
    val _id: String,
    val countryName: String,
    val createdAt: String,
    val isDeleted: Boolean,
    val likes: Likes,
    val matches: Matches,
    val name: String,
    val picture: Picture,
    val price: Price,
    val read: Read,
    val received: Received,
    val send: Send,
    val type: Int,
    val updatedAt: String,
    val video: Video
): Parcelable