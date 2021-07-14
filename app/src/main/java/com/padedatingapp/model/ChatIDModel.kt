package com.padedatingapp.model

import java.io.Serializable


open class ChatIDModel() : Serializable{
    var senderID: String = ""
    var senderName: String = ""
    var senderImage: String = ""
    var receiverID: String = ""
    var receiverName: String = ""
    var receiverImage: String = ""



        get() = field                     // getter
        set(value) { field = value }      // setter

}