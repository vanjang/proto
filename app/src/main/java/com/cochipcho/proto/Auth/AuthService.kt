package com.cochipcho.proto.Auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    fun signin(credential: AuthCredential, completion: (Exception?) -> Unit) {
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        GoogleAuth.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("google auth succeeded")
                completion(null)
//                startActivity(HomeActivity.getLaunchIntent(this))
            } else {
                Timber.i("google auth failed")
                val exception = task.exception
                completion(exception)
            }
        }
    }

}