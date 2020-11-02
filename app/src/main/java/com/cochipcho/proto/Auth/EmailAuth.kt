package com.cochipcho.proto.Auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object EmailAuth {



    private lateinit var auth: FirebaseAuth

    fun login(email: String, password: String, completion: (Boolean) -> Unit) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->

            Timber.i("$email")
            Timber.i("$password")

            if (result.isSuccessful) {
                Timber.i("succeeded")
                completion(true)
            } else {
                Timber.i("failed")
                completion(false)
            }
        }
            .addOnFailureListener {
                Timber.i("Failure reason: ${it.localizedMessage}")
            }
    }


}