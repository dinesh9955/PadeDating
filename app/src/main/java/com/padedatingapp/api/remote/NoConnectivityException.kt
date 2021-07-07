package com.padedatingapp.api.remote

import java.io.IOException

class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "Please Check Your Internet Connection and Try Again"
    // You can send any message whatever you want from here.
}