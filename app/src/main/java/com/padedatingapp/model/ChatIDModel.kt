package com.padedatingapp.model

import java.io.Serializable


open class ChatIDModel() : Serializable{
    var type: String = ""
    var block: String = ""
    var senderID: String = ""
    var senderName: String = ""
    var senderImage: String = ""
    var receiverID: String = ""
    var receiverName: String = ""
    var receiverImage: String = ""


    var apikey: String = ""
    var token: String = ""
    var sessionId: String = ""

        get() = field                     // getter
        set(value) { field = value }      // setter

}