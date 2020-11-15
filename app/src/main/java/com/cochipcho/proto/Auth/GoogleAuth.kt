package com.cochipcho.proto.Auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.cochipcho.proto.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import java.lang.Exception


object GoogleAuth {

    lateinit var signInClient: GoogleSignInClient
    lateinit var signInOptions: GoogleSignInOptions
    lateinit var auth: FirebaseAuth
    var RC_SIGN_IN: Int = 1
    /// Called on onCreated where GSI is implemented on.

    fun init(activity: Activity) {
        auth = FirebaseAuth.getInstance()
        val webClientId = activity.getString(R.string.web_client_id)
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(activity, signInOptions)
        Timber.i("google auth init..")
    }

//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, completion: (Boolean) -> Unit) {
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Timber.i("google auth succeeded")
////                startActivity(HomeActivity.getLaunchIntent(this))
//            } else {
//                Timber.i("google auth failed")
//            }
//        }
//    }

    fun googleSignin(code: Int, intent: Intent?, completion: (Boolean) -> Unit) {
        if (code == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                if (credential != null) {
                    AuthService.signin(credential) { excetion ->
                        if (excetion != null) {
                            completion(false)
                        } else {
                            signInClient.signOut()
                            completion(true)
                        }
                    }
                } else {
                    Timber.i("account is null")
                    completion(false)
                }

            } catch (e: ApiException) {
                completion(false)
            }
        }
    }



}