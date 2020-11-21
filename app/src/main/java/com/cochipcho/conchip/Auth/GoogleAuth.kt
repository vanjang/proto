package com.cochipcho.conchip.Auth

import android.app.Activity
import android.content.Intent
import com.cochipcho.conchip.Helpers.signupDate
import com.cochipcho.conchip.Models.User
import com.cochipcho.conchip.R
import com.cochipcho.conchip.Services.MyApplication
import com.cochipcho.conchip.Services.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.lang.Exception
import java.util.*


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

    fun googleSignin(code: Int, intent: Intent?, completion: (User?, Exception?) -> Unit) {
        if (code == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                Firebase.auth.signInWithCredential(credential)
                    .addOnSuccessListener { result ->
                        val additionalInfo = result.additionalUserInfo
                        val uid = result.user?.uid
                        val username = additionalInfo?.username
                        val email = result.user?.email

                        // Create user data only when it is a new user
                        if (additionalInfo != null &&
                            uid != null &&
                            additionalInfo.isNewUser
                        ) {
                            var userData = User(
                                username = username ?: "",
                                fcm_tokens = null,
                                email = email ?: "",
                                queriable = username ?: "",
                                nickname = "",
                                profileImageUrl = "",
                                uid = uid,
                                appleId = null,
                                authMethod = MyApplication.GOOGLE_AUTH,
                                os = MyApplication.ANDROID_OS,
                                signupDate = signupDate
                            )
                            UserData.createUserData(userData, uid) { exception ->
                                signInClient.signOut()
                                if (exception != null) {
                                    completion(null, exception)
                                    return@createUserData
                                }
                                // Alway signs user out as soon as sign-in finishes so that the sign-in flow window pops up next time signing in
                                completion(userData, null)
                            }
                            return@addOnSuccessListener
                        }
                        signInClient.signOut()
                        completion(null, null)
                    }
                    .addOnFailureListener { exception ->
                        completion(null, exception)
                    }
            } catch (exeption: ApiException) {
                completion(null, exeption)
            }
        }
    }


}