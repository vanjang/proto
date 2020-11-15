package com.cochipcho.proto.Services

import android.app.Application
import com.cochipcho.proto.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MyApplication: Application() {
    companion object {
        var globalVar = 2
        val REFERENCE_USER = FirebaseDatabase.getInstance().getReference("users")
        val USERNAME = "username"
        val QUERIABLE = "queriable"
        val EMAIL = "email"
        val UID = "uid"
        val AUTHMETHOD = "authMethod"
        val APPLE_ID = "appleId"
        val FCM_TOKENS = "fcm_tokens"


    }

    override fun onCreate() {
        super.onCreate()

    }
}