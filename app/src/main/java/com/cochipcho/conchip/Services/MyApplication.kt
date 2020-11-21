package com.cochipcho.conchip.Services

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

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

        val EMAIL_AUTH = "email"
        val FACEBOOK_AUTH = "facebook"
        val APPLE_AUTH = "apple"
        val GOOGLE_AUTH = "google"
        val ANDROID_OS = "android"
        val IOS_OS = "iOS"

        val signupDate: Int
            get() {
                val signupDateLong = System.currentTimeMillis()/1000
                val signupDate = signupDateLong.toInt()
                return signupDate
            }
    }

    override fun onCreate() {
        super.onCreate()

    }
}