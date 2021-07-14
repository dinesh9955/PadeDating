package com.flexhelp.model.chat_history

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

//	@field:SerializedName("receiver")
//	val receiver: Sender? = null,
//
//	@field:SerializedName("sender")
//	val sender: Sender? = null,

	@field:SerializedName("receiver")
	val receiver: String? = null,

	@field:SerializedName("sender")
	val sender: Boolean? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("message")
	val message: String? = "",

	@field:SerializedName("status")
	var status: String? = null,

	@field:SerializedName("isSeen")
	val isSeen: Boolean? = null,

	@field:SerializedName("bookingId")
	val bookingId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null ,

	@field:SerializedName("type")
	var type : String = "" ,

	@field:SerializedName("media")
	var media : String = "" ,

	var typeText : Int = 0,

	@field:SerializedName("amount")
	var amount : Double = 0.0,

	@field:SerializedName("negotiateAmount")
	var negotiateAmount : Double = 0.0
)