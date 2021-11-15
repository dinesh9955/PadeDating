package com.padedatingapp.model.loyalityModel

data class UserPoints(
    val __v: Int,
    val _id: String,
    val accessToken: String,
    val address: String,
    val age: Int,
    val appleId: String,
    val bearing: String,
    val blockedUsers: List<Any>,
    val childern: String,
    val city: String,
    val country: String,
    val countryCode: String,
    val createdAt: String,
    val dateofbirth: String,
    val datingPreference: String,
    val description: String,
    val deviceToken: String,
    val deviceType: String,
    val docImage: List<Any>,
    val doyoudrink: String,
    val doyousmoke: String,
    val educationLevel: String,
    val email: String,
    val ethnicity: String,
    val facebookId: String,
    val firstName: String,
    val friendReferralCode: String,
    val gender: String,
    val geoPoint: GeoPoint,
    val googleId: String,
    val height: String,
    val heightInCms: String,
    val image: String,
    val instagramId: String,
    val interestedIn: String,
    val isActive: Boolean,
    val isApproved: Boolean,
    val isBlocked: Boolean,
    val isDeleted: Boolean,
    val isEmailVerified: Boolean,
    val isNewUser: Boolean,
    val isNotification: Boolean,
    val isOnline: Boolean,
    val isPhoneVerified: Boolean,
    val isSubscribed: Boolean,
    val lastName: String,
    val latitude: String,
    val longitude: String,
    val myReferralCode: String,
    val oneTimeCode: String,
    val password: String,
    val pendingAmount: String,
    val phoneNo: String,
    val profileStatus: Int,
    val religiousBelief: String,
    val state: String,
    val stripeId: Any,
    val totalPoints: Int=0,
    val updatedAt: String,
    val username: String,
    val walletAmount: String,
    val work: String
)