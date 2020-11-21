package com.cochipcho.conchip.Services

import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception

object MessageService {

    fun getFcmTokens(completion: (String?, Exception?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                completion(null,task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            completion(token, null)
        }
    }


}