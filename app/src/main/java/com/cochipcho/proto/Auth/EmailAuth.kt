package com.cochipcho.proto.Auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object EmailAuth {



    private lateinit var auth: FirebaseAuth

    fun login(email: String, password: String, completion: (Boolean) -> Unit) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                completion(true)
            } else {
                completion(false)
            }
        }
    }


}