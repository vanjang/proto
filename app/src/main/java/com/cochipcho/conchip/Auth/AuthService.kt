package com.cochipcho.conchip.Auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import java.lang.Exception

object AuthService {

    private lateinit var auth: FirebaseAuth

    val isLoggedInAndEmailVerified: Boolean
        get() {
            val user = auth.currentUser
            if (user != null && user?.isEmailVerified) {
                return true
            } else {
                return false
            }
        }

//    fun signin(credential: AuthCredential, completion: (AuthResult?, Exception?) -> Unit) {
//        auth.signInWithCredential(credential)
//            .addOnSuccessListener {  result ->
//                completion(result, null)
//            }
//            .addOnFailureListener {  exception ->
//                completion(null, exception)
//            }
//    }

}