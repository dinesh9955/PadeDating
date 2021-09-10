package com.padedatingapp.model

import com.padedatingapp.model.call.Data
import com.padedatingapp.model.call.User1
import com.padedatingapp.model.call.User2
import java.io.Serializable

class CallData : Serializable{
    var apikey: String = ""
    var sessionId: String = ""
    var token: String = ""
    var callType: String = ""
    var user1FirstName: String = ""
    var user1LastName: String = ""
    var user1Image: String = ""
    var user2FirstName: String = ""
    var user2LastName: String = ""
    var user2Image: String = ""
    var callFrom: String = ""
}