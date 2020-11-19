package com.cochipcho.conchip.Auth

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cochipcho.conchip.Activities.MainTabBarActivity
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import timber.log.Timber
import java.io.OutputStreamWriter
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

object AppleAuth {
    val auth = Firebase.auth

    lateinit var provider : OAuthProvider.Builder
    fun init() {

        provider = OAuthProvider.newBuilder("apple.com")
        provider.setScopes(listOf("email", "name"))
    }

    fun appleSigninButtonTapped(activity: Activity) {
        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener { authResult ->
                Timber.i("checkPending:onSuccess:$authResult")
                // Get the user profile with authResult.getUser() and
                // authResult.getAdditionalUserInfo(), and the ID
                // token from Apple with authResult.getCredential().
            }.addOnFailureListener { e ->
                Timber.i("checkPending:onFailure: ${e.localizedMessage}")
            }
        } else {
            auth.firebaseAuthSettings
            auth.startActivityForSignInWithProvider(activity, provider.build())

                .addOnSuccessListener { authResult ->
                    // Sign-in successful!
                    Timber.i("activitySignIn:onSuccess:${authResult.user}")
                    val user = authResult.user
                    // ...
                }
                .addOnFailureListener { e ->
                    Timber.i("activitySignIn:onFailure : ${e.localizedMessage}")
                }

            Timber.i("pending: null")
        }



    }


}