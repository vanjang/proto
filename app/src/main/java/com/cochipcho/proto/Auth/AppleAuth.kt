package com.cochipcho.proto.Auth

import android.app.Activity
import com.cochipcho.proto.R
import com.google.firebase.auth.OAuthProvider

object AppleAuth {


    val CLIENT_ID = "com.cochipcho.conchipServices"
    val REDIRECT_URI = "https://conchip-b1ba6.firebaseapp.com/__/auth/handler"
    val SCOPE = "name%20email"

    val AUTHURL = "https://appleid.apple.com/auth/authorize"
    val TOKENURL = "https://appleid.apple.com/auth/token"

//    val state = UUID.randomUUID().toString()

    val provider = OAuthProvider.newBuilder("apple.com")

    fun init(activity: Activity) {
        provider.setScopes(arrayOf("email", "name").toMutableList())

    }

}